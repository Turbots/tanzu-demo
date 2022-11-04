package com.vmware.tanzu.demo.payments;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@EnableScheduling
public class PaymentConfig implements AsyncConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentConfig.class);

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(50000);
        executor.setThreadNamePrefix("payment-executor-");
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> {
            LOGGER.error("Unexpected exception when performing @Async method", throwable);
        };
    }

    @Bean("processedCounter")
    public Counter processedCounter(MeterRegistry registry) {
        return Counter.builder("tanzu.payments.processed")
                .tag("application", "payment-service")
                .description("The number of payments that were processed")
                .register(registry);
    }

    @Bean("confirmationCounter")
    public Counter confirmationCounter(MeterRegistry registry) {
        return Counter.builder("tanzu.payments.confirmed")
                .tag("application", "payment-service")
                .description("The number of payments that were confirmed")
                .register(registry);
    }
}
