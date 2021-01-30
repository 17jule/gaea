package com.qa.basic.utils;

import com.alibaba.fastjson.JSONObject;
import com.qa.basic.base.CustomizedObjWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

/**
 * Created by chasen on 2021/1/30.
 */
@Component
public class RequestParser implements InitializingBean {
    @Value("${api.config.mode.by.json}")
    public boolean isApiConfiguredByJson;
    @Value("${api.config.src.relative.dir}")
    public String apiConfigDir;
    @Value("${spring.profiles.active}")
    public String namespace;
    public JSONObject requestJson;
    public String financeDomain = null;
    public String fundApiDomain = null;
    public String fundDomain = null;
    public String userDomain = null;
    public Properties properties = new Properties();

    public RequestParser() {
    }

    public void afterPropertiesSet() throws Exception {
        if(this.isApiConfiguredByJson) {
            this.requestJson = ConvertUtils.parseJsonRecursivelyByConfigDir(this.apiConfigDir);
        }

        InputStream propStream = this.getClass().getResourceAsStream(String.format("/domain-config/domain-config-%s.properties", new Object[]{this.namespace}));
        this.properties.load(propStream);
        propStream.close();
    }

    public String getApiDesc(String apiName) {
        return this.requestJson.getJSONObject(apiName).getString("desc");
    }

    public String getRequestType(String apiName) {
        return this.requestJson.getJSONObject(apiName).getString("type");
    }

    public String getParameterClass(String apiName) {
        return this.requestJson.getJSONObject(apiName).getString("parameterClass");
    }

    public JSONObject getRequestJson() {
        return this.requestJson;
    }

    public JSONObject getRequestParamJson(String apiName) {
        return this.requestJson.getJSONObject(apiName).getJSONObject("request");
    }

    public String getDubboInterface(String apiName) {
        return this.requestJson.getJSONObject(apiName).getString("interface");
    }

    public String getDubboMethod(String apiName) {
        return this.requestJson.getJSONObject(apiName).getString("method");
    }

    public HashMap<String, Object> freeMarkerParamsTemplateByMap(String apiName, Map<String, Object> map) throws IOException, TemplateException {
        JSONObject template = this.getRequestParamJson(apiName);
        StringWriter result = new StringWriter();
        HashMap newMap = new HashMap();
        map.forEach((key, value) -> {
            if(value instanceof Collection) {
                String var = value.getClass().getName() + "@" + value.hashCode();
                newMap.put(key, var);
            } else {
                newMap.put(key, value);
            }

        });
        Template t = new Template("template", new StringReader(template.toString()), new Configuration(Configuration.VERSION_2_3_0));
        t.process(newMap, result, new CustomizedObjWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));
        HashMap params = new HashMap();
        Set entrySets = JSONObject.parseObject(result.toString()).entrySet();
        Iterator var9 = entrySets.iterator();

        while(var9.hasNext()) {
            Map.Entry entrySet = (Map.Entry)var9.next();
            String key = (String)entrySet.getKey();
            String value = entrySet.getValue().toString();
            if(this.isHashCodeStr(value)) {
                String paramName = template.getString(key).substring(2, template.getString(key).length() - 1);
                params.put(key, map.get(paramName));
            } else {
                params.put(key, value);
            }
        }

        return params;
    }

    private boolean isHashCodeStr(Object value) {
        if(value instanceof String && (!(value instanceof String) || ((String)value).contains("@"))) {
            String valStr = (String)value;
            String className = valStr.substring(0, valStr.indexOf("@"));

            try {
                Class.forName(className);
                return true;
            } catch (ClassNotFoundException var5) {
                return false;
            }
        } else {
            return false;
        }
    }
}
