package com.qa.basic.bridge;

import com.qa.basic.driver.DataProviderRouter;
import com.qa.basic.utils.RequestParser;
import com.qa.basic.utils.YamlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by chasen on 2021/1/30.
 */
@Component
public class BasicBeanServiceBridge {
    public static YamlLoader yamlLoader;
    public static RequestParser requestParser;
    public static DataProviderRouter dataProviderRouter;

    public BasicBeanServiceBridge() {

    }

    @Autowired
    public void setRequestParser(RequestParser requestParser) {
        BasicBeanServiceBridge.requestParser = requestParser;
    }

    @Autowired
    public void setDataProviderRouter(DataProviderRouter dataProviderRouter) {
        BasicBeanServiceBridge.dataProviderRouter = dataProviderRouter;
    }

    @Autowired
    public void setYamlLoader(YamlLoader yamlLoader) {
        BasicBeanServiceBridge.yamlLoader = yamlLoader;
    }
}
