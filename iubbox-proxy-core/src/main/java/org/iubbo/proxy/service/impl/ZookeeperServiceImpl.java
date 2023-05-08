

package org.iubbo.proxy.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.iubbo.proxy.model.dto.MethodMetaData;
import org.iubbo.proxy.model.dto.ServiceAddrDTO;
import org.iubbo.proxy.service.ZookeeperService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author idea
 * @version V1.0
 * @date 2019/12/24
 */
@Service
@Slf4j
public class ZookeeperServiceImpl implements ZookeeperService {


    @Value("${zk.dubbo.root}")
    private String zkDubboRoot;

    @Value("${zk.dubbo.method.root}")
    private String zkDubboMethodRoot;

    @Value("${zk.meta.data.addr}")
    private String zkMetaDataAddr;

    @Value("${zookeeper.base.sleep.times}")
    private int baseSleepTimes;

    @Value("${zookeeper.max.retry.times}")
    private int maxRetryTimes;


    private final static String METHOD_LABEL = "methods";

    private final static String APPLICATION_LABEL = "application";

    //url中的空格符号
    private final static String URL_BLANK_LABEL = "%3D";

    //url中的=符号
    private final static String URL_EQUAL_LABEL = "%2C";

    //url中的分号
    private final static String URL_SEMICOLON_LABEL = "%26";

    private CuratorFramework client;

    @Override
    public List<String> getServiceNameList(String zkHost) {
        return this.getServices(zkHost);
    }

    /**
     * 需要保证client对象已经启动了
     *
     * @param serviceAddrDTO
     * @return
     */
    private String getApplicationName(ServiceAddrDTO serviceAddrDTO) {
        if (client == null) {
            return StringUtils.EMPTY;
        }
        String applicationName = StringUtils.EMPTY;
        String urlPath = String.format(zkDubboMethodRoot, serviceAddrDTO.getServiceName());
        List<String> urlList = null;
        try {
            urlList = client.getChildren().forPath(urlPath);
            //当有多个application注册到zk的时候，以最后一个application name为准
            for (String url : urlList) {
                int begin = url.indexOf(APPLICATION_LABEL) + APPLICATION_LABEL.length();
                url = url.substring(begin).replaceAll(URL_BLANK_LABEL, StringUtils.EMPTY);
                int end = url.indexOf(URL_SEMICOLON_LABEL);
                applicationName = url.substring(0, end);
            }
        } catch (Exception e) {
            log.error("[getApplicationName] exception is ", e);
        }
        return applicationName;
    }


    @Override
    public Set<String> getMethodNameByServiceName(ServiceAddrDTO serviceAddrDTO) {
        //多服务节点的方法名称汇总
        Set<String> methodNameSet = new LinkedHashSet<>();
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(this.getBaseSleepTimes(), this.getMaxRetryTimes());
        try {
            client = CuratorFrameworkFactory.newClient(serviceAddrDTO.getServiceAddr(), retryPolicy);
            client.start();
            String methodPath = String.format(zkDubboMethodRoot, serviceAddrDTO.getServiceName());
            List<String> urlList = client.getChildren().forPath(methodPath);
            for (String url : urlList) {
                int begin = url.indexOf(METHOD_LABEL) + METHOD_LABEL.length();
                url = url.substring(begin).replaceAll(URL_BLANK_LABEL, StringUtils.EMPTY);
                int end = url.indexOf(URL_SEMICOLON_LABEL);
                String methodStr = url.substring(0, end);
                String[] methodName = methodStr.split(URL_EQUAL_LABEL);
                for (String name : methodName) {
                    methodNameSet.add(name);
                }
            }
        } catch (Exception e) {
            log.error("[getMethodNameByServiceName] exception is ", e);
        } finally {
            try {
                client.close();
            } catch (Exception e) {
                log.error("[getMethodNameByServiceName] close exception is ", e);
            }
        }
        return methodNameSet;
    }

    @Override
    public MethodMetaData getMetaData(ServiceAddrDTO serviceAddrDTO) {
        try {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(this.getBaseSleepTimes(), this.getMaxRetryTimes());
            client = CuratorFrameworkFactory.newClient(serviceAddrDTO.getServiceAddr(), retryPolicy);
            client.start();
            String applicationName = this.getApplicationName(serviceAddrDTO);
            String metaDataDefault = serviceAddrDTO.getServiceName() + "/provider/" + applicationName;
            byte[] bytes = client.getData().forPath(String.format(zkMetaDataAddr, metaDataDefault));
            String json = new String(bytes);
            JSONObject jsonObject = JSON.parseObject(json);
            JSONArray methodJsonArr = (JSONArray) jsonObject.get("methods");
            Iterator<Object> jsonObjectIterator = methodJsonArr.iterator();
            while (jsonObjectIterator.hasNext()) {
                JSONObject item = (JSONObject) jsonObjectIterator.next();
                List<String> parameterTypesList = (List<String>) item.get("parameterTypes");
                String methodName = (String) item.get("name");
                if(!methodName.equals(serviceAddrDTO.getMethodName())){
                    continue;
                }
                String returnType = (String) item.get("returnType");
                MethodMetaData methodMetaData = new MethodMetaData();
                methodMetaData.setMethodName(methodName);
                methodMetaData.setParameterTypes(parameterTypesList);
                methodMetaData.setReturnType(returnType);
                return methodMetaData;
            }
        } catch (Exception e) {
            log.error("[getMetaData] exception is {}", e);
        } finally {
            try {
                client.close();
            } catch (Exception e) {
                log.error("[getMetaData] close exception is ", e);
            }
        }
        return null;
    }


    /**
     * 获取zk上边的服务名字信息
     *
     * @return
     */
    private List<String> getServices(String addr) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(this.getBaseSleepTimes(), this.getMaxRetryTimes());
        client = CuratorFrameworkFactory.newClient(addr, retryPolicy);
        client.start();
        try {
            List<String> nodeList = client.getChildren().forPath(zkDubboRoot);
            return nodeList;
        } catch (Exception e) {
            log.error("[getService] exception is {}", e);
            return Collections.emptyList();
        } finally {
            try {
                client.close();
            } catch (Exception e) {
                log.error("[getServices] close exception is ", e);
            }
        }
    }

    public int getBaseSleepTimes() {
        return baseSleepTimes;
    }

    public int getMaxRetryTimes() {
        return maxRetryTimes;
    }

    public static void main(String[] args) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);
        client.start();
        try {
            byte[] bytes = client.getData().forPath("/dubbo/metadata/localserver.service.IOrderProvider/provider/order-provider");
            String json = new String(bytes);
            JSONObject jsonObject = JSON.parseObject(json);
            JSONArray methodJsonArr = (JSONArray) jsonObject.get("methods");
            Iterator<Object> jsonObjectIterator = methodJsonArr.iterator();
            while (jsonObjectIterator.hasNext()) {
                JSONObject item = (JSONObject) jsonObjectIterator.next();
                List<String> parameterTypesList = (List<String>) item.get("parameterTypes");
                String methodName = (String) item.get("name");
                String returnType = (String) item.get("returnType");
                MethodMetaData methodMetaData = new MethodMetaData();
                methodMetaData.setMethodName(methodName);
                methodMetaData.setParameterTypes(parameterTypesList);
                methodMetaData.setReturnType(returnType);
                System.out.println(methodMetaData);
            }
//            MethodMetaData methodMetaData = JSON.parseObject(methodObjectStr,MethodMetaData.class);
//            System.out.println(methodMetaData);
            client.close();
        } catch (Exception e) {
            log.error("[getService] exception is {}", e);
        }
    }
}
