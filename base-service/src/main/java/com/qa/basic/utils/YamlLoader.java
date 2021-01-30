package com.qa.basic.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chasen on 2021/1/30.
 */
@Component
public class YamlLoader {
    @Value("${yaml.data.provider.src.relative.dir}")
    public String ymlDir;
    @Value("${yaml.data.provider.suffix.extention}")
    public String ymlType;
    private HashMap<String, ArrayList<HashMap<String, String>>> ymlProps = null;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public YamlLoader() {
    }

    public synchronized YamlLoader loadYaml(String fileName) {
        Yaml yaml = new Yaml();

        try {
            InputStream e = this.getClass().getClassLoader().getResourceAsStream(FileUtils.joinPath(this.ymlDir, fileName + this.ymlType));
            this.ymlProps = (HashMap)yaml.load(e);
            e.close();
        } catch (IOException var4) {
            this.logger.error(var4.getMessage());
        }

        return this;
    }

    public Object[][] getDataMapByKey(String key) {
        ArrayList list = (ArrayList)this.ymlProps.get(key);
        Object[][] result = new Object[list.size()][];

        for(int i = 0; i < list.size(); ++i) {
            result[i] = new Object[]{list.get(i)};
        }

        return result;
    }
}
