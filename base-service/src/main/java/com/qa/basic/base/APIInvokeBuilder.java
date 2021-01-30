package com.qa.basic.base;



import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.qa.basic.entity.DubboRequestConfig;
import com.qa.basic.utils.*;
import com.qa.basic.annos.*;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Component
public class APIInvokeBuilder {
    @Autowired
    public RequestParser requestParser;
    @Autowired
    public DubboUtils dubboUtils;
    public DubboRequestConfig dubboRequestConfig = new DubboRequestConfig();

    public APIInvokeBuilder() {
    }

    public APIInvokeBuilder.DubboInvoker buildDubboAPI(Method method) throws IOException, ParseException {
        String apiName = ((DubboAPI)method.getAnnotation(DubboAPI.class)).api();
        String apiKey = ((DubboAPI)method.getAnnotation(DubboAPI.class)).schema() + "." + apiName;
        this.loadApiConfigIfNotExisted(apiKey, method);
        String interfaceName = StringUtils.isEmpty(((DubboAPI)method.getAnnotation(DubboAPI.class)).service())?this.requestParser.getDubboInterface(apiKey):((DubboAPI)method.getAnnotation(DubboAPI.class)).service();
        String methodName = StringUtils.isEmpty(((DubboAPI)method.getAnnotation(DubboAPI.class)).method())?this.requestParser.getDubboMethod(apiKey):((DubboAPI)method.getAnnotation(DubboAPI.class)).method();
        String dubboGroup = ((DubboAPI)method.getAnnotation(DubboAPI.class)).group();
        String[] paramClassTypes = ((DubboAPI)method.getAnnotation(DubboAPI.class)).paramTypes();

        this.dubboRequestConfig.setApiName(apiKey);
        this.dubboRequestConfig.setInterfaceName(interfaceName);
        this.dubboRequestConfig.setMethodName(methodName);
        this.dubboRequestConfig.setParamTypes(Arrays.asList(paramClassTypes));
        this.dubboRequestConfig.getParamList().clear();
        this.dubboRequestConfig.setDubboGroup(dubboGroup);
        return new APIInvokeBuilder.DubboInvoker();
    }

    private synchronized void loadApiConfigIfNotExisted(String apiKey, Method method) throws IOException, ParseException {
        JSONObject requestJson = this.requestParser.getRequestJson();
        if(requestJson.get(apiKey) == null) {
            String path = method.getDeclaringClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            if(!path.endsWith(".jar")) {
                throw new RuntimeException("can not find api key in json configurations");
            }

            JarFile jar = new JarFile(new File(path));
            Enumeration entries = jar.entries();

            while(entries.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry)entries.nextElement();
                String innerPath = jarEntry.getName();
                if(innerPath.startsWith("api-config") && innerPath.endsWith(".json")) {
                    InputStream inputStream = method.getDeclaringClass().getClassLoader().getResourceAsStream(innerPath);
                    JSONObject parsedFileJson = FileUtils.parseApiJsonWithSchemaFromStream(inputStream);
                    ConvertUtils.mergeJSONObject(parsedFileJson, requestJson);
                }
            }
        }

        if(requestJson.get(apiKey) == null) {
            throw new RuntimeException("can not find api key in json configurations.");
        }
    }

    public class DubboInvoker {
        public DubboInvoker() {
        }

        public Object invoke() throws IOException, TemplateException, ClassNotFoundException {
            Object result = this.exec(new HashMap[0]);
            return result;
        }

        public Object invoke(HashMap<String, Object> map) throws IOException, TemplateException, ClassNotFoundException {
            HashMap params = APIInvokeBuilder.this.requestParser.freeMarkerParamsTemplateByMap(APIInvokeBuilder.this.dubboRequestConfig.getApiName(), map);
            Object result = this.exec(new HashMap[]{params});
            return result;
        }

        public <T> T invoke(TypeReference<T> typeReference) throws IOException, TemplateException, ClassNotFoundException {
            Object result = this.exec(new HashMap[0]);
            return JacksonUtil.convertValue(result, typeReference);
        }

        public <T> T invoke(HashMap<String, Object> map, TypeReference<T> typeReference) throws IOException, TemplateException, ClassNotFoundException {
            HashMap params = APIInvokeBuilder.this.requestParser.freeMarkerParamsTemplateByMap(APIInvokeBuilder.this.dubboRequestConfig.getApiName(), map);
            Object result = this.exec(new HashMap[]{params});
            return JacksonUtil.convertValue(result, typeReference);
        }

        public Object exec(HashMap... params) throws ClassNotFoundException {
            List paramTypes = APIInvokeBuilder.this.dubboRequestConfig.getParamTypes();

            for(int dubboGroup = 0; dubboGroup < paramTypes.size(); ++dubboGroup) {
                String result = (String)paramTypes.get(dubboGroup);
                Object value = params[0].get("var" + dubboGroup);
                APIInvokeBuilder.this.dubboRequestConfig.getParamList().add("null".equals(value)?null:ConvertUtils.convertType(value, Class.forName(this.toClassType(result))));
            }

            String var6 = APIInvokeBuilder.this.dubboRequestConfig.getDubboGroup();
            Object var7 = APIInvokeBuilder.this.dubboUtils.invoke(APIInvokeBuilder.this.dubboRequestConfig.getInterfaceName(), APIInvokeBuilder.this.dubboRequestConfig.getMethodName(), var6, paramTypes, APIInvokeBuilder.this.dubboRequestConfig.getParamList());
            return var7;
        }

        public String toClassType(String typeName) {
            String classTypeName = typeName;
            if("long".equals(typeName)) {
                classTypeName = Long.class.getTypeName();
            } else if("double".equals(typeName)) {
                classTypeName = Double.class.getTypeName();
            } else if("int".equals(typeName)) {
                classTypeName = Integer.class.getTypeName();
            } else if("char".equals(typeName)) {
                classTypeName = Character.class.getTypeName();
            } else if("boolean".equals(typeName)) {
                classTypeName = Boolean.class.getTypeName();
            } else if("float".equals(typeName)) {
                classTypeName = Float.class.getTypeName();
            } else if("byte".equals(typeName)) {
                classTypeName = Byte.class.getTypeName();
            } else if("short".equals(typeName)) {
                classTypeName = Short.class.getTypeName();
            }

            return classTypeName;
        }
    }
}
