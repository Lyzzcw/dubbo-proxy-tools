package org.iubbo.proxy.model.po;


import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

/**
 * @Author linhao
 * @Date created in 8:09 下午 2021/6/22
 */
@Data
@TableName("t_login_status")
public class LoginStatusPO{

    @TableId(type = IdType.AUTO)
    private long id;

    private int userId;

    private String userToken;

    private Date createTime;

    private Date updateTime;
}
