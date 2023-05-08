

package org.iubbo.proxy.service.impl;

import org.iubbo.proxy.common.utils.BeanCopyUtil;
import org.iubbo.proxy.common.utils.TokenKeyUtil;
import org.iubbo.proxy.dao.UserDao;
import org.iubbo.proxy.model.dto.UserDTO;
import org.iubbo.proxy.model.po.LoginStatusPO;
import org.iubbo.proxy.model.po.UserPO;
import org.iubbo.proxy.service.LoginStatusService;
import org.iubbo.proxy.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * @author idea
 * @version V1.0
 * @date 2020/2/26
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private LoginStatusService loginStatusService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String login(String username, String password) {
        UserPO userPO = userDao.selectByUsernameAndPwd(username, password);
        if (userPO != null) {
            loginStatusService.deleteByUserId(userPO.getId());
            String randStr = UUID.randomUUID().toString();
            String clientToken = TokenKeyUtil.buildToken(randStr);
            LoginStatusPO loginStatusPO = new LoginStatusPO();
            loginStatusPO.setUserId(userPO.getId());
            loginStatusPO.setUserToken(clientToken);
            loginStatusService.insertOne(loginStatusPO);
            return randStr;
        } else {
            return null;
        }
    }

    @Override
    public List<UserDTO> selectAllUser() {
        List<UserPO> userPOS = userDao.selectAll();
        return BeanCopyUtil.copyList(userPOS, UserDTO.class);
    }

    @Override
    public UserDTO selectByUsername(String username) {
        UserPO userPO = userDao.selectByUsername(username);
        if (userPO == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userPO, userDTO);
        return userDTO;
    }

    @Override
    public UserDTO selectById(Integer id) {
        UserPO userPO = userDao.selectById(id);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userPO, userDTO);
        return userDTO;
    }

    @Override
    public boolean isUserNameExist(String username) {
        int count = userDao.selectCountByUsername(username);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public UserDTO register(String username, String password) {
        UserPO userPO = new UserPO();
        userPO.setUsername(username);
        userPO.setPassword(password);
        userDao.insert(userPO);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userPO, userDTO);
        return userDTO;
    }
}
