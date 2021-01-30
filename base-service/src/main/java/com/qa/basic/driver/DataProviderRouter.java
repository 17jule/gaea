package com.qa.basic.driver;

import com.qa.basic.utils.YamlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by chasen on 2021/1/30.
 */
@Component
public class DataProviderRouter {
    public String YAML = "yaml";
    @Value("${default.data.provider.data.format}")
    public String defaultDataProviderFormat;
    @Autowired
    public YamlLoader yamlLoader;

    public DataProviderRouter() {
    }

    public Object[][] getDataProvider(String className, String methodName) {
        return this.yamlLoader.loadYaml(className).getDataMapByKey(methodName);
    }
}
