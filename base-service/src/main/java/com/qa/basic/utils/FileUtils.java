package com.qa.basic.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.ParseException;
import java.util.*;

/**
 * Created by chasen on 2021/1/30.
 */
public class FileUtils {
    public static final String JSON_SUFFIX = ".json";
    public static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public FileUtils() {
    }

    public static boolean createFileIfNotExists(File file) throws IOException {
        boolean result = false;
        if(!file.exists() || !file.isFile()) {
            result = file.createNewFile();
        }

        return result;
    }

    public static boolean createFileIfNotExists(String filePath) throws IOException {
        File file = new File(filePath);
        return createFileIfNotExists(file);
    }

    public static boolean createDirectoryIfNotExists(File file) throws IOException {
        boolean result = false;
        if(!file.exists() || !file.isDirectory()) {
            result = file.mkdirs();
        }

        return result;
    }

    public static boolean createDirectoryIfNotExists(String filePath) throws IOException {
        File file = new File(filePath);
        return createDirectoryIfNotExists(file);
    }

    public static boolean hasFirstLine(File file) throws IOException {
        boolean result = false;
        FileReader fileReader = new FileReader(file);
        BufferedReader br = new BufferedReader(fileReader);
        if(br.readLine() != null) {
            result = true;
        }

        return result;
    }

    public static boolean hasFirstLine(String filePath) throws IOException {
        File file = new File(filePath);
        return hasFirstLine(file);
    }

    public static void writeLineWithAppending(File file, String line, boolean append) throws IOException {
        FileWriter fileWriter = new FileWriter(file, append);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println(line);
        printWriter.flush();
        printWriter.close();
        fileWriter.close();
    }

    public static void writeFileWithAppending(File file, String str, boolean append) throws IOException {
        FileWriter fileWriter = new FileWriter(file, append);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(str);
        printWriter.flush();
        printWriter.close();
        fileWriter.close();
    }

    public static void writeLineWithAppending(String filePath, String line, boolean append) throws IOException {
        File file = new File(filePath);
        writeLineWithAppending(file, line, append);
    }

    public static void writeFileWithAppending(String filePath, String line, boolean append) throws IOException {
        File file = new File(filePath);
        writeFileWithAppending(file, line, append);
    }

    public static String joinPath(String dir, String file) {
        return dir + File.separator + file;
    }

    public static Vector<File> filterFileBySuffixName(File dir, String suffix) {
        Vector list = new Vector();
        if(!dir.isDirectory()) {
            return list;
        } else {
            File[] files = dir.listFiles();
            File[] var4 = files;
            int var5 = files.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                File file = var4[var6];
                if(file.isDirectory()) {
                    list.addAll(filterFileBySuffixName(file, suffix));
                } else if(file.getName().endsWith(suffix)) {
                    list.add(file);
                }
            }

            return list;
        }
    }

    public static Vector<File> filterFileBySuffixName(String dirPath, String suffix) {
        File dir = new File(dirPath);
        return filterFileBySuffixName(dir, suffix);
    }

    public static JSONObject parseApiJsonWithSchemaFromStream(InputStream is) throws IOException, ParseException {
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        JSONObject parsedStream = null;
        try {
            inputStreamReader = new InputStreamReader(is, "UTF-8");
            JSONReader reader = new JSONReader(new BufferedReader(inputStreamReader));
            reader.startArray();
            parsedStream = JSONObject.parseObject((String) reader.readObject());
            reader.endArray();

        } catch (Exception e) {
            e.printStackTrace();
        }
        String schema = parsedStream.getString("schema");
        JSONObject apiJson = parsedStream.getJSONObject("apis");
        JSONObject parsedStreamJson = new JSONObject();
        Set var1 = apiJson.entrySet();
        boolean duplicatedKey = false;
        Iterator var8 = var1.iterator();

        while(var8.hasNext()) {
            Map.Entry item = (Map.Entry)var8.next();
            String key = (String)item.getKey();
            String newKey = schema + "." + key;
            if(parsedStreamJson.containsKey(newKey)) {
                logger.error("api key \"{}\" duplicated in schema\"{}\"", key, schema);
                duplicatedKey = true;
            } else {
                parsedStreamJson.put(newKey, apiJson.get(key));
            }
        }

        if(duplicatedKey) {
            throw new RuntimeException("api keys dubplicated, please check it.");
        } else {
            return parsedStreamJson;
        }
    }

    public static ArrayList<File> getFiles(ArrayList<File> fileArrayList, String filePath) throws FileNotFoundException, IOException {
        logger.info("文件路径：{}", filePath);
        if(fileArrayList == null) {
            fileArrayList = new ArrayList();
        }

        if(filePath != null && !filePath.isEmpty()) {
            File root = new File(filePath);
            if(root.exists()) {
                File[] files = root.listFiles();
                File[] var4 = files;
                int var5 = files.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    File file = var4[var6];
                    if(file.isDirectory()) {
                        getFiles(fileArrayList, file.getAbsolutePath());
                    } else {
                        fileArrayList.add(file);
                    }
                }
            }

            return fileArrayList;
        } else {
            return fileArrayList;
        }
    }
}
