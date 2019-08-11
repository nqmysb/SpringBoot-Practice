package com.nqmysb.practice.dto.base;



import lombok.Data;
import lombok.EqualsAndHashCode;

/** 
 * Class Name: UserDTO  
 * Description: 
 * 	数据传输，用来接收参数
 * @date: 2018年1月23日
 * @version: 1.0
 *
 */  
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseDTO {
	  
    
    /*
     * 当前页码
     */

    private int page;

    /*
     * 页大小
     */
    private int pageSize;

}
