package com.qa;

import com.qa.basic.base.ExtentTestNGSpringContextTests;
import com.qa.basic.config.SpringBootTestNGConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by chasen on 2021/1/30.
 */

@SpringBootTest(classes = SpringBootTestNGConfig.class)
@ImportResource(locations={"classpath:/META-INF/paymentqa-api-context.xml"})
public abstract class BaseTest extends ExtentTestNGSpringContextTests {

    //some public method and variable
}
