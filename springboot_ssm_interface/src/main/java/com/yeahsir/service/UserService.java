package com.yeahsir.service;

import com.yeahsir.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
	/**分页 */
	List<User> getUserByPage(Map<String, Object> map);
	/**查总记录数total*/
	int getUserTotal();
	/** 用户添加 */
	int insert(User user);
	/** 用户添加-做判空的处理 */
	int insertSelective(User user);
	/** 通过ID获取用户信息 */
	User selectByPrimaryKey(Integer id);
	/** 通过主键修改用户信息*/
	int updateByPrimaryKeySelective(User record);
	/** 通过用户ID(主键)删除用户信息*/
	int deleteByPrimaryKey(Integer id);
}
