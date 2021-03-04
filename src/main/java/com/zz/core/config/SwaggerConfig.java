package com.zz.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
//@Profile("dev")
public class SwaggerConfig {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.application.description}")
    private String appDesc;

    @Value("${zz.basePackage}")
    private String xacBasePackage;

    @Bean
    public Docket initDucket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(xacBasePackage+".controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(appName + " API 文档")
                .description(appDesc)
                .version("1.0.0")
                .license("license-秘密")
                .contact(new Contact("ZZ","","devpuck@163.com"))
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .build();
    }
}
