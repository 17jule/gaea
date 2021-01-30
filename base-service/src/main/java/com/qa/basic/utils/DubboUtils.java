package com.qa.basic.utils;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chasen on 2021/1/30.
 */
@Component
public class DubboUtils implements InitializingBean {
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${dubbo.protocol}")
    private String protocol;
    @Value("${dubbo.zk.servers}")
    private String zkServers;
    @Value("${dubbo.zk.group}")
    private String zkGroup;
    private Logger logger = LoggerFactory.getLogger(DubboUtils.class);
    private ApplicationConfig application = new ApplicationConfig();
    private RegistryConfig registryConfig = new RegistryConfig();
    private Map<String, ReferenceConfig> referenceCache = new ConcurrentHashMap();

    public DubboUtils() {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.application.setName(this.applicationName);
        this.registryConfig.setProtocol(this.protocol);
        this.registryConfig.setAddress(this.zkServers);
        this.registryConfig.setGroup(this.zkGroup);
    }

    private ReferenceConfig getReferenceConfig(String interfaceName, String dubboGroup) {
        String referenceKey = interfaceName;
        ReferenceConfig referenceConfig = (ReferenceConfig)this.referenceCache.get(interfaceName);
        if(null == referenceConfig) {
            try {
                referenceConfig = new ReferenceConfig();
                referenceConfig.setApplication(this.application);
                if(!StringUtils.isEmpty(dubboGroup)) {
                    referenceConfig.setGroup(dubboGroup);
                }

                referenceConfig.setRegistry(this.registryConfig);
                Class e = Class.forName(interfaceName);
                referenceConfig.setInterface(e);
                referenceConfig.setGeneric(Boolean.valueOf(true));
                this.referenceCache.put(referenceKey, referenceConfig);
            } catch (ClassNotFoundException var6) {
                var6.printStackTrace();
            }
        }

        return referenceConfig;
    }

    public Object invoke(String interfaceName, String methodName, String dubboGroup, List<String> paramTypes, List<Object> paramList) {
        ReferenceConfig reference = this.getReferenceConfig(interfaceName, dubboGroup);
        if(null != reference) {
            GenericService genericService = (GenericService)reference.get();
            if(genericService == null) {
                this.logger.info("GenericService 不存在:{}", interfaceName);
                return null;
            } else {
                Object result = genericService.$invoke(methodName, (String[])paramTypes.toArray(new String[paramTypes.size()]), paramList.toArray(new Object[paramList.size()]));
                return result;
            }
        } else {
            return null;
        }
    }
}
