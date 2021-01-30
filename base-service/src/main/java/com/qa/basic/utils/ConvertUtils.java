package com.qa.basic.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Created by chasen on 2021/1/30.
 */
public class ConvertUtils {
    private static Logger logger = LoggerFactory.getLogger(ConvertUtils.class);

    public ConvertUtils() {
    }

    public static String capitalizeString(String str) {
        String newStr = str.substring(0, 1).toUpperCase() + str.substring(1);
        return newStr;
    }

    public static JSONObject translateJsonWithUnderlines(JSONObject json) {
        JSONObject newJson = new JSONObject();
        Set entrySet = json.entrySet();
        Iterator var3 = entrySet.iterator();

        while(var3.hasNext()) {
            Map.Entry entry = (Map.Entry)var3.next();
            String key = (String)entry.getKey();
            if(key.endsWith("_")) {
                newJson.put(key.substring(0, key.length() - 1).replace("_", "-"), entry.getValue());
            } else {
                newJson.put(key, entry.getValue());
            }
        }

        return newJson;
    }


    public static JSONObject parseJsonRecursivelyByConfigDir(String configDir) throws IOException, ParseException {
        JSONObject parsedJson = new JSONObject();
        String parsedDir = ConvertUtils.class.getClassLoader().getResource(configDir).getPath();
        Vector files = FileUtils.filterFileBySuffixName(parsedDir, ".json");
        Iterator var4 = files.iterator();

        while(var4.hasNext()) {
            File file = (File)var4.next();
            JSONObject parsedFileJson = FileUtils.parseApiJsonWithSchemaFromStream(new FileInputStream(file));
            mergeJSONObject(parsedFileJson, parsedJson);
        }

        return parsedJson;
    }

    public static JSONObject mergeJSONObject(JSONObject srcJson, JSONObject destJson) throws IOException, ParseException {
        Set var2 = srcJson.entrySet();
        boolean duplicatedKey = false;
        Iterator var4 = var2.iterator();

        while(var4.hasNext()) {
            Map.Entry item = (Map.Entry)var4.next();
            String key = (String)item.getKey();
            if(destJson.containsKey(key)) {
                logger.error("schema api key \"{}\" duplicated", key);
                duplicatedKey = true;
            } else {
                destJson.put(key, item.getValue());
            }
        }

        if(duplicatedKey) {
            throw new RuntimeException("api keys dubplicated, please check it.");
        } else {
            return destJson;
        }
    }

    public static Object convertType(Object value, Class clazz) {
        String fullClassName = clazz.getName();
        String simpleClassName = clazz.getSimpleName();

        try {
            if(clazz == String.class) {
                return value.toString();
            }

            if(fullClassName.indexOf("java.lang.") != 0) {
                return clazz.cast(value);
            }

            Class e = Class.forName(fullClassName);
            Method method = e.getMethod("parse" + typeToMethod(simpleClassName), new Class[]{String.class});
            if(method != null) {
                Object ret = method.invoke((Object)null, new Object[]{value.toString()});
                if(ret != null) {
                    return ret;
                }
            }
        } catch (Exception var7) {
            logger.error("type conversion error!");
        }

        return null;
    }

    private static String typeToMethod(String type) {
        byte var2 = -1;
        switch(type.hashCode()) {
            case -672261858:
                if(type.equals("Integer")) {
                    var2 = 0;
                }
            default:
                switch(var2) {
                    case 0:
                        return "Int";
                    default:
                        return type;
                }
        }
    }

    public static String toPlainStr(Object obj) {
        return JacksonUtil.objToJson(obj).replaceAll("\"", "\'");
    }
}
