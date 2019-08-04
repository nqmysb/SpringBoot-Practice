package com.nqmysb.practice.controller.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nqmysb.practice.dto.user.UserDTO;
import com.nqmysb.practice.entity.user.User;
import com.nqmysb.practice.service.user.IUserService;
import com.nqmysb.practice.utils.JsonResult;
import com.nqmysb.practice.utils.ResultCode;
import com.nqmysb.practice.vo.user.UserVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 用户管理前端控制器
 * </p>
 *
 * @author liaocan
 * @since 2019-04-14
 */
@RestController
@RequestMapping("/user")
@Api(value="用户资源", tags="用户管理")  
public class UserController {


	
	@Autowired
	IUserService userServiceImpl;
	

	@ApiOperation(value="查询用户信息列表", notes="根据查询条件查询用户信息信息", httpMethod = "POST") 
	@ApiImplicitParams({  
        @ApiImplicitParam(paramType = "query", name = "username", dataType = "string", required = false, value = "用户名称"),  
        @ApiImplicitParam(paramType = "query", name = "email", dataType = "string", required = false, value = "电子邮箱"),  
        @ApiImplicitParam(paramType = "query", name = "phone", dataType = "string", required = false, value = "电话号码")   
	})  
	@RequestMapping(value ="/query",method=RequestMethod.POST)
	public JsonResult<ArrayList<UserVO>> query(@RequestBody(required=false) UserDTO userDTO) throws Exception {
		try {
			Page<User> page = new Page<>(1, 10);
			if(null != userDTO && 0 != userDTO.getPage()){
				page.setCurrent(userDTO.getPage());
			}
			
			
			IPage<User> userList = userServiceImpl.query(page,userDTO);
			ArrayList<UserVO> resList = new ArrayList<UserVO>();

			List<User> UserList1 = userList.getRecords();
			
			for (User User : UserList1) {
				UserVO ovo = new UserVO();
				BeanUtils.copyProperties(User, ovo);
				resList.add(ovo);
			}
			
			int pageSize = (int) page.getSize();
			return JsonResult.buildSuccessResult(resList,pageSize,page.getTotal());
		
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.buildFailuredResult(ResultCode.SYS_ERROR, e.getCause().toString());
		}
	
	}
	
	
	@ApiOperation(value="保存用户信息", notes="保存用户信息", httpMethod = "POST") 
	@ApiImplicitParams({  
        @ApiImplicitParam(paramType = "query", name = "username", dataType = "string", required = false, value = "用户名称"), 
        @ApiImplicitParam(paramType = "query", name = "password", dataType = "string", required = false, value = "用户密码"),  
        @ApiImplicitParam(paramType = "query", name = "email", dataType = "string", required = false, value = "电子邮箱"),  
        @ApiImplicitParam(paramType = "query", name = "phone", dataType = "string", required = false, value = "电话号码")   
	})  
	@RequestMapping(value ="/insert",method=RequestMethod.POST)
	public JsonResult<ArrayList<UserVO>> insert(@RequestBody(required=false) UserDTO userDTO) throws Exception {
		try {
			User entity = new User();
			BeanUtils.copyProperties(userDTO, entity);
			userServiceImpl.save(entity);
			return JsonResult.buildSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.buildFailuredResult(ResultCode.SYS_ERROR, e.getCause().toString());
		}
	
	}
	
	@ApiOperation(value="修改用户信息", notes="修改用户信息", httpMethod = "POST") 
	@ApiImplicitParams({  
        @ApiImplicitParam(paramType = "query", name = "username", dataType = "string", required = false, value = "用户名称"),  
        @ApiImplicitParam(paramType = "query", name = "password", dataType = "string", required = false, value = "用户密码"),  
        @ApiImplicitParam(paramType = "query", name = "email", dataType = "string", required = false, value = "电子邮箱"),  
        @ApiImplicitParam(paramType = "query", name = "phone", dataType = "string", required = false, value = "电话号码")   
	})  
	@RequestMapping(value ="/update",method=RequestMethod.POST)
	public JsonResult<ArrayList<UserVO>> udpate(@RequestBody(required=false) UserDTO userDTO) throws Exception {
		try {
			User entity = new User();
			BeanUtils.copyProperties(userDTO, entity);
			userServiceImpl.updateById(entity);
			return JsonResult.buildSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.buildFailuredResult(ResultCode.SYS_ERROR, e.getCause().toString());
		}
	
	}
	
	@ApiOperation(value="删除用户信息", notes="删除用户信息", httpMethod = "POST") 
	@ApiImplicitParams({  
        @ApiImplicitParam(paramType = "query", name = "userid", dataType = "string", required = false, value = "用户账号"),  
	})  
	@RequestMapping(value ="/delete",method=RequestMethod.POST)
	public JsonResult<ArrayList<UserVO>> delete(@RequestBody(required=false) ArrayList<UserDTO> userDTOs) throws Exception {
		try {
			//获取主键列表
			List<String> resList = new ArrayList<String>();
			if (null != userDTOs && userDTOs.size() > 0 ){			
				for (UserDTO userDTO : userDTOs) {
					User entity = new User();
					BeanUtils.copyProperties(userDTO, entity);
					resList.add(entity.getUserid());
				}
			}else{
				return JsonResult.buildFailuredResult(ResultCode.SYS_ERROR, "服务器接受参数失败!");
			}
			//根据主键列表删除
			userServiceImpl.removeByIds(resList);
			return JsonResult.buildSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.buildFailuredResult(ResultCode.SYS_ERROR, e.getCause().toString());
		}
	
	}
	

}
