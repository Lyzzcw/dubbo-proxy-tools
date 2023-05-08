package org.iubbo.proxy.common.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.iubbo.proxy.model.dto.DubboInvokerParamDTO;
import org.springframework.util.StringUtils;

/**
 * @author idea
 * @version V1.0
 * @date 2020/7/3
 */
public class DubboUtils {


    private static String APP_NAME = "dubbo-invoker-proxy";

    private static Integer DEFAULT_TIME_OUT = 3000;

    private static String DEFAULT_LOADBALANCE = LoadbalanceEnum.RANDOM_TYPE.getDes();

    private static boolean DEFAULT_ASYNC = false;

    private static int DEFAULT_RETRIES = 3;


    @AllArgsConstructor
    @Getter
    public enum  LoadbalanceEnum{

        RANDOM_TYPE(0,"random"),
        ROUND_ROBIN_TYPE(1,"roundrobin"),
        LEASTACTIVE_TYPE(2,"leastactive");

        Integer code;

        String des;

    }


    static class ReferenceFactory{
        public static ReferenceConfig<GenericService> buildReferenceConfig(DubboInvokerParamDTO param) {
            if (StringUtils.isEmpty(param.getRetries())) {
                param.setRetries(DEFAULT_RETRIES);
            }
            if (StringUtils.isEmpty(param.isAsync())) {
                param.setAsync(DEFAULT_ASYNC);
            }
            if (StringUtils.isEmpty(param.getLoadbalance())) {
                param.setLoadbalance(DEFAULT_LOADBALANCE);
            }
            if (StringUtils.isEmpty(param.getTimeout())) {
                param.setTimeout(DEFAULT_TIME_OUT);
            }

            ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
            reference.setApplication(new ApplicationConfig(APP_NAME));

            reference.setInterface(param.getInterfaceName());
            reference.setVersion(param.getVersion());
            reference.setGeneric(true);
            if (!StringUtils.isEmpty(param.getGroup())) {
                reference.setGroup(param.getGroup());
            }
            //默认重试次数为3次
            if (param.getRetries() < 0) {
                reference.setRetries(param.getRetries());
            }
            reference.setTimeout(param.getTimeout());
            reference.setAsync(param.isAsync());
            reference.setActives(param.getActives());
            reference.setLoadbalance(param.getLoadbalance());
            RegistryConfig registry = new RegistryConfig();
            //暂时通过端口来氛
            if (param.getRegisterType().contains("nacos")) {
                registry.setAddress("nacos://" + param.getServiceAddr());
            } else {
                registry.setAddress("zookeeper://" + param.getServiceAddr());
            }
            reference.setRegistry(registry);
            if (!StringUtils.isEmpty(param.getUrl())) {
                reference.setUrl(param.getUrl());
            }
            return reference;
        }
    }

    public static void main(String[] args) {

        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("service-consumer");
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("nacos://127.0.0.1:8848");
        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setGeneric(true);
        referenceConfig.setInterface("com.idea.spring.cloud.alibaba.framework.interfaces.service.ITestService");
        referenceConfig.setApplication(applicationConfig);
        referenceConfig.setRegistry(registryConfig);
        System.out.println(referenceConfig.toString());
        GenericService genericService= referenceConfig.get();
        genericService.$invoke("doTest",new String[]{},new Object[]{});
        System.out.println("end");
    }
}
