package com.qa.basic.base;

import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

/**
 * Created by chasen on 2021/1/30.
 */
@Listeners({ExtentTestNGITestListener.class, ICasesCountListener.class})
public class ExtentTestNGSpringContextTests extends AbstractTestNGSpringContextTests {
    public ExtentTestNGSpringContextTests() {
    }

    @BeforeSuite
    public synchronized void beforeSuite() {
    }

    @AfterSuite
    public synchronized void afterSuite() {
        this.logger.info(ICasesCountListener.getCasesCountInfo());
    }

    public String prefixSchema(String apiName, String schema) {
        return schema + "." + apiName;
    }
}
