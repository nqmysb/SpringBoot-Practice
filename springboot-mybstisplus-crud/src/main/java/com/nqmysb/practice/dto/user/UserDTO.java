package com.nqmysb.practice.dto.user;

import com.nqmysb.practice.dto.base.BaseDTO;

import lombok.Data;

/** 
 * Class Name: UserDTO  
 * Description: 
 * 	数据传输，用来接收参数
 * @date: 2018年1月23日
 * @version: 1.0
 *
 */  
@Data
public class UserDTO  extends BaseDTO{
	  
    /*
     * 用户账号
     */

    private String userid;
    
    /*
     * 用户名称
     */

    private String username;
    
    /*
     * 用户密码
     */

    private String password;
    
    /*
     * 用户邮箱
     */

    private String email;
    
    /*
     * 联系电话
     */

    private String mobile;
    
    /*
     * 状态
     */

    private String status;
    
    

}
