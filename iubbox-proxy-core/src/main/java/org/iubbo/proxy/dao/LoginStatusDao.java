package org.iubbo.proxy.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.iubbo.proxy.model.po.LoginStatusPO;

/**
 * @Author linhao
 * @Date created in 8:09 下午 2021/6/22
 */
@Mapper
public interface LoginStatusDao extends BaseMapper<LoginStatusPO> {

    @Select("select * from t_login_status where user_token=#{userToken}")
    LoginStatusPO selectByUserToken(@Param("userToken")String userToken);

    @Delete("delete from t_login_status where user_id=#{userId}")
    int deleteByUserId(@Param("userId")long userId);

    @Delete("delete from t_login_status where user_token=#{userToken}")
    int deleteByUserToken(@Param("userToken")String userToken);
}
