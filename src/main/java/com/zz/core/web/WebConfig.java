package com.zz.core.web;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.zz.core.constant.CoreConstant;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig
{

    /**
     * 配置消息转换器
     * new HttpMessageConverters(true, converters);
     * 一定要设为true才能替换否则不会替换
     * @return 返回一个消息转换的bean
     */
    @Bean
    public HttpMessageConverters fastJsonMessageConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        //需要定义一个convert转换消息的对象;
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        //添加fastJson的配置信息;
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat,
                //List字段如果为null,输出为[],而非null

                SerializerFeature.WriteNullListAsEmpty,

                //字符类型字段如果为null,输出为"",而非null

                SerializerFeature.WriteNullStringAsEmpty,

                //Boolean字段如果为null,输出为falseJ,而非null

                SerializerFeature.WriteNullBooleanAsFalse,

                //消除对同一对象循环引用的问题，默认为false（如果不配置有可能会进入死循环）

                SerializerFeature.DisableCircularReferenceDetect,

                //是否输出值为null的字段,默认为false。

                SerializerFeature.WriteMapNullValue);
        //全局时间配置
        fastJsonConfig.setDateFormat(CoreConstant.DATE_TIME_PATTERN);
        fastJsonConfig.setCharset(Charset.forName("UTF-8"));
        //处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        //在convert中添加配置信息.
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        fastConverter.setFastJsonConfig(fastJsonConfig);

        converters.add(0, fastConverter);
        return new HttpMessageConverters(converters);
    }
}
