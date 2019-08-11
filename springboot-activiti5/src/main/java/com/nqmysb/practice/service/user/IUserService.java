package com.nqmysb.practice.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nqmysb.practice.dto.user.UserDTO;
import com.nqmysb.practice.entity.user.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liaocan
 * @since 2019-04-14
 */
public interface IUserService extends IService<User> {
	
	/**
	 * 
	 * Method Name:  query
	 * Description:  查询用户列表
	 * @param page, userDTO
	 * @return IPage<User>
	 * @exception 	
	 * @author liaocan
	 * @mail 593013537@qq.com
	 * @date: 2019年1月16日
	 */
	IPage<User> query(Page<User> page ,UserDTO userDTO);

}
