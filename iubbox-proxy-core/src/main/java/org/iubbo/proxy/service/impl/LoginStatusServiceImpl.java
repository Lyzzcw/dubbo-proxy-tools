package org.iubbo.proxy.service.impl;

import org.iubbo.proxy.dao.LoginStatusDao;
import org.iubbo.proxy.model.po.LoginStatusPO;
import org.iubbo.proxy.service.LoginStatusService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author linhao
 * @Date created in 8:07 下午 2021/6/22
 */
@Service
public class LoginStatusServiceImpl implements LoginStatusService {

    @Resource
    private LoginStatusDao loginStatusDao;

    @Override
    public boolean deleteByUserId(long userId) {
        return loginStatusDao.deleteByUserId(userId)>0;
    }

    @Override
    public boolean isUserExist(String userToken) {
        return loginStatusDao.selectByUserToken(userToken)!=null;
    }

    @Override
    public LoginStatusPO getByUserToken(String userToken){
        return loginStatusDao.selectByUserToken(userToken);
    }

    @Override
    public boolean insertOne(LoginStatusPO loginStatusPO) {
        return loginStatusDao.insert(loginStatusPO)>0;
    }

    @Override
    public boolean deleteByUserToken(String userToken) {
        return loginStatusDao.deleteByUserToken(userToken)>0;
    }
}
