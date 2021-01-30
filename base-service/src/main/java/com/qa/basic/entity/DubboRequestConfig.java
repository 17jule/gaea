package com.qa.basic.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chasen on 2021/1/30.
 */
public class DubboRequestConfig {
    private String apiName;
    private String interfaceName;
    private String methodName;
    private String dubboGroup;
    private List<String> paramTypes = new ArrayList();
    private List<Object> paramList = new ArrayList();

    public DubboRequestConfig() {
    }

    public String getApiName() {
        return this.apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getInterfaceName() {
        return this.interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getParamTypes() {
        return this.paramTypes;
    }

    public void setParamTypes(List<String> paramTypes) {
        this.paramTypes = paramTypes;
    }

    public List<Object> getParamList() {
        return this.paramList;
    }

    public void setParamList(ArrayList<Object> paramList) {
        this.paramList = paramList;
    }

    public String getDubboGroup() {
        return this.dubboGroup;
    }

    public void setDubboGroup(String dubboGroup) {
        this.dubboGroup = dubboGroup;
    }
}
