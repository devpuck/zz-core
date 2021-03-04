package com.zz.core.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.zz.core.auth.JwtTokenUtil;
import com.zz.core.auth.ZzUserContextHolder;
import com.zz.core.constant.CoreConstant;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        genCofigRestTemplate(restTemplate);

        return restTemplate;
    }

    public static void genCofigRestTemplate(RestTemplate restTemplate) {
        //换上fastjson
        List<HttpMessageConverter<?>> httpMessageConverterList = restTemplate.getMessageConverters();
        Iterator<HttpMessageConverter<?>> iterator = httpMessageConverterList.iterator();
        while (iterator.hasNext()) {
            HttpMessageConverter<?> converter = iterator.next();
            //原有的String是ISO-8859-1编码 去掉
            if (converter instanceof StringHttpMessageConverter) {
                iterator.remove();
            }
            //由于系统中默认有jackson 在转换json时自动会启用  但是我们不想使用它 可以直接移除或者将fastjson放在首位
            if (converter instanceof GsonHttpMessageConverter || converter instanceof MappingJackson2HttpMessageConverter) {
                iterator.remove();
            }
        }
        httpMessageConverterList.add(new StringHttpMessageConverter(Charset.forName("utf-8")));
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.DisableCircularReferenceDetect);
        //全局时间配置
        fastJsonConfig.setDateFormat(CoreConstant.DATE_TIME_PATTERN);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        // 设置默认的请求参数类型
        fastJsonHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));

        httpMessageConverterList.add(0, fastJsonHttpMessageConverter);

        // 添加JWT的拦截器
        restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                //将令牌放入请求header中
                request.getHeaders().add(JwtTokenUtil.getTokenName(), ZzUserContextHolder.getUser().getToken());
                // restTemplate请求标识
                request.getHeaders().add(CoreConstant.REQ_METHOD, CoreConstant.REQ_REST);
                return execution.execute(request, body);
            }
        });
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(10000);//单位为ms
        factory.setConnectTimeout(5000);//单位为ms
        return factory;
    }
}
