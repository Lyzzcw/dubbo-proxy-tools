package org.iubbo.proxy.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author idea
 * @version V1.0
 * @date 2020/7/2
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceAddrDTO {

    /**
     * 注册中心的地址
     */
    private String serviceAddr;

    /**
     * dubbo的服务名称
     */
    private String serviceName;

    /**
     * 前端传输为字符串类型
     */
    private String registerType;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 针对nacos有用
     */
    private String nacosGroup;

    private String nacosNamespace;
}
