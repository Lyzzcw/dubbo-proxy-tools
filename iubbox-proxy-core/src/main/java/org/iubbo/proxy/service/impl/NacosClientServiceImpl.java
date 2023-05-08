package org.iubbo.proxy.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.ServiceInfo;
import com.alibaba.nacos.client.naming.NacosNamingService;
import com.alibaba.nacos.client.naming.net.NamingProxy;
import com.alibaba.nacos.common.util.HttpMethod;
import org.iubbo.proxy.model.dto.ServiceInfoResult;
import org.iubbo.proxy.service.NacosClientService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author idea
 * @version V1.0
 * @date 2020/7/2
 */
@Service
public class NacosClientServiceImpl implements NacosClientService {

    private static final String reqNacosUrl = "/nacos/v1/ns/catalog/services";

    @Override
    public List<String> getServiceNameList(String nacosHost, String namespace, String group) throws NacosException, IllegalAccessException {
        Properties properties = new Properties();
        properties.setProperty("serverAddr", nacosHost);
        NamingService namingService = NamingFactory.createNamingService(properties);
        NacosNamingService nacosNamingService = (NacosNamingService) namingService;
        Field[] fields = nacosNamingService.getClass().getDeclaredFields();
        Field serverProxyField = null;
        for (Field field : fields) {
            if (field.getName().equals("serverProxy")) {
                field.setAccessible(true);
                serverProxyField = field;
                break;
            }
        }
        NamingProxy namingProxy = (NamingProxy) serverProxyField.get(namingService);
        Map<String, String> reqParams = new HashMap<>(5);
        //这一项必须有，否则会有未存活节点也展示
        reqParams.put("hasIpCount","true");
        if(!StringUtils.isEmpty(namespace)){
            reqParams.put("namespaceId", namespace);
        }
        if(!StringUtils.isEmpty(group)){
            reqParams.put("groupNameParam", group);
        }
        reqParams.put("pageNo", "1");
        reqParams.put("pageSize", "1000");
        String result = namingProxy.callServer(reqNacosUrl, reqParams, nacosHost);
        ServiceInfoResult serviceInfoResult = JSON.parseObject(result, ServiceInfoResult.class);
        List<ServiceInfo> serviceInfos = serviceInfoResult.getServiceList();
        List<String> resultList = new ArrayList<>(serviceInfos.size());
        for (ServiceInfo serviceInfo : serviceInfos) {
            String serviceName = serviceInfo.getName();
            if (!StringUtils.isEmpty(serviceName) && serviceName.contains("providers")) {
                serviceName = serviceName.replaceAll("providers:", "");
                serviceName = serviceName.substring(0,serviceName.indexOf(":"));
                resultList.add(serviceName);
            }
        }
        return resultList;
    }

}
