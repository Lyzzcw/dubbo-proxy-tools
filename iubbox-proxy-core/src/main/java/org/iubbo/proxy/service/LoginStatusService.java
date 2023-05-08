package org.iubbo.proxy.service;

import org.iubbo.proxy.model.po.LoginStatusPO;

/**
 * 登录状态服务
 *
 * @Author linhao
 * @Date created in 8:08 下午 2021/6/22
 */
public interface LoginStatusService {


    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    boolean deleteByUserId(long userId);

    /**
     * 用户是否存在
     *
     * @param userToken
     * @return
     */
    boolean isUserExist(String userToken);

    /**
     * 根据用户token查询
     *
     * @return
     */
    LoginStatusPO getByUserToken(String userToken);

    /**
     * 写入数据
     *
     * @param loginStatusPO
     * @return
     */
    boolean insertOne(LoginStatusPO loginStatusPO);

    /**
     * 删除数据
     *
     * @param userToken
     * @return
     */
    boolean deleteByUserToken(String userToken);
}
