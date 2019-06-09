package com.nqmysb.practice.vo.user;

import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;

/** 
 * Class Name: UserinfoVO  
 * Description: 
 * 用作返回数据
 * @date: 2018年1月23日
 * @version: 1.0
 *
 */  
@Data
public class UserVO {

	  
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
  