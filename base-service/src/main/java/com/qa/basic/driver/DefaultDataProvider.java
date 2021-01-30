package com.qa.basic.driver;

import com.qa.basic.bridge.BasicBeanServiceBridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * Created by chasen on 2021/1/30.
 */
public class DefaultDataProvider {
    private static Logger logger = LoggerFactory.getLogger(DefaultDataProvider.class);

    public DefaultDataProvider() {
    }

    @DataProvider(
            name = "default",
            parallel = false
    )
    public Object[][] getDefaultDataProviderByTestClassAndMethodName(Method method) throws IOException, NoSuchMethodException, SQLException, ClassNotFoundException, InterruptedException {
        return BasicBeanServiceBridge.dataProviderRouter.getDataProvider(method.getDeclaringClass().getSimpleName(), method.getName());
    }

    @DataProvider(
            name = "yaml",
            parallel = false
    )
    public Object[][] getYamlDataProviderByTestClassAndMethodName(Method method) throws IOException, NoSuchMethodException, SQLException, ClassNotFoundException, InterruptedException {
        return BasicBeanServiceBridge.yamlLoader.loadYaml(method.getDeclaringClass().getSimpleName()).getDataMapByKey(method.getName());
    }

    public static String prefixSchema(String apiName, String schema) {
        return schema + "." + apiName;
    }
}
