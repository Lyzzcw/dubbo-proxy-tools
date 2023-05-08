package org.iubbo.proxy.model.dto;

import lombok.Data;

import java.util.List;

/**
 * dubbo方法类型参数在zk的存储
 *
 * @Author linhao
 * @Date created in 11:42 下午 2021/6/9
 */
@Data
public class MethodMetaData {

    private String methodName;

    private List<String> parameterTypes;

    private String returnType;
}
