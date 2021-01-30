package com.qa.basic.config;

/**
 * Created by chasen on 2021/1/30.
 */

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;

@SpringBootApplication(
        scanBasePackages = {"com.qa"},
        exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
public class SpringBootTestNGConfig {
    public SpringBootTestNGConfig() {

    }
}
