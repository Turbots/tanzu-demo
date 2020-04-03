package com.vmware.tanzu.demo.accounts;

import brave.http.HttpTracing;
import brave.spring.web.TracingClientHttpRequestInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableBinding(PaymentChannels.class)
public class AccountsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountsApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate(HttpTracing tracing) {
        return new RestTemplateBuilder()
                .additionalInterceptors(TracingClientHttpRequestInterceptor.create(tracing))
                .build();
    }
}
