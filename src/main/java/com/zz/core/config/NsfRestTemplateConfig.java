package com.zz.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.List;


@Configuration
public class NsfRestTemplateConfig {

    @Autowired
    private RestTemplate restTemplate;

    @Bean("xacRestTemplate")
    public RestTemplate restTemplate() {
        com.zz.core.config.RestTemplateConfig.genCofigRestTemplate(restTemplate);
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        Iterator<ClientHttpRequestInterceptor> iterator = interceptors.iterator();
        while (iterator.hasNext()) {
            ClientHttpRequestInterceptor interceptor = iterator.next();
            if (interceptor.getClass().getName().indexOf("ServiceAuthorityInterceptor") >-1 ) {
                iterator.remove();
            }
        }
        return restTemplate;
    }
}
