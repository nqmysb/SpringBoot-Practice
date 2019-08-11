package com.nqmysb.practice.mapper.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nqmysb.practice.dto.user.UserDTO;
import com.nqmysb.practice.entity.user.User;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liaocan
 * @since 2019-04-14
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
	
	/**
    *
    * @param page 翻页对象，可以作为 xml 参数直接使用，传递参数 Page 即自动分页
    * @return
    */

    IPage<User> query(Page<User> page , @Param("userDTO") UserDTO userDTO);

}
