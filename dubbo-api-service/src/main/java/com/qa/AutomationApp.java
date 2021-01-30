package com.qa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by chasen on 2021/1/30.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class},
        scanBasePackages = {"com.qa"})
@ImportResource(locations={"classpath:/META-INF/paymentqa-api-context.xml"})
public class AutomationApp {
    public static void main(String[] args) {
        SpringApplication.run(AutomationApp.class, args);
    }
}
