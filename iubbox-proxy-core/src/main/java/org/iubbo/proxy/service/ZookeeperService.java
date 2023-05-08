package org.iubbo.proxy.service;

import org.iubbo.proxy.model.dto.MethodMetaData;
import org.iubbo.proxy.model.dto.ServiceAddrDTO;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 专门拉取zk信息的方法依赖
 *
 * @author idea
 * @date 2019/12/23
 * @version V1.0
 */
public interface ZookeeperService {

    /**
     * 拉取zk里面的服务列表
     *
     * @param zkHost
     * @return
     */
    List<String> getServiceNameList(String zkHost);

    /**
     * 根据方法名称查询RPC服务名称
     *
     * @param serviceAddrDTO
     * @return
     */
    Set<String> getMethodNameByServiceName(ServiceAddrDTO serviceAddrDTO);

    /**
     * 获取dubbo在zk里面注册的metadata数据
     *
     * @param serviceAddrDTO
     * @return
     */
    MethodMetaData getMetaData(ServiceAddrDTO serviceAddrDTO);
}
