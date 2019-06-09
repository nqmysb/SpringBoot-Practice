package com.nqmysb.practice.entity.user;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * <p>
 * 用户实体对象
 * </p>
 *
 * @author liaocan
 * @since 2019-04-14
 */



@Data
@TableName("YHXXB")
@KeySequence(value = "SEQ_YHXXB", clazz = String.class)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    
    /*
     * 用户账号
     */
    @TableField("YHZH")
    private String userid;
    
    /*
     * 用户名称
     */
    @TableField("YHMC")
    private String username;
    
    /*
     * 用户密码
     */
    @TableField("YHMM")
    private String password;
    
    /*
     * 用户邮箱
     */
    @TableField("LXYX")
    private String email;
    
    /*
     * 联系电话
     */
    @TableField("LXDH")
    private String mobile;
    
    /*
     * 状态
     */
    @TableField("ZT")
    private String status;



 
}
