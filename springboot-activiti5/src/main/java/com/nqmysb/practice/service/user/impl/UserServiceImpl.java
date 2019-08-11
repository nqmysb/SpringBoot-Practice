package com.nqmysb.practice.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nqmysb.practice.dto.user.UserDTO;
import com.nqmysb.practice.entity.user.User;
import com.nqmysb.practice.mapper.user.UserMapper;
import com.nqmysb.practice.service.user.IUserService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liaocan
 * @since 2019-04-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public IPage<User> query(Page<User> page ,UserDTO userDTO) {
//		List<Organization> organizationList = this.baseMapper.getUserList(page);
		IPage<User> userList = userMapper.query(page ,userDTO);
		return userList;
	}
    
}
