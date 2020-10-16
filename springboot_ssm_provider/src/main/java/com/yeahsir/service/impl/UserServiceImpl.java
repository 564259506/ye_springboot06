package com.yeahsir.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.yeahsir.dao.UserMapper;
import com.yeahsir.entity.User;
import com.yeahsir.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Service(interfaceClass = UserService.class)
//@Service(version = "1.0.0", timeout = 50000)
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public synchronized List<User> getUserByPage(Map<String, Object> map) {
		// map = 1. 起始下标  2. 分页大小

		List<User> list = null;

		// 1. 去redis缓存中找
		String inJsonStr = redisTemplate.opsForValue().get("userList");
		if(!StringUtils.isEmpty(inJsonStr)){
			// 2. 找到了就返回
			System.out.println("redis查询");
			list = JSON.parseArray(inJsonStr, User.class);

			// 通过List集合进行分页
			int pageSize = (int) map.get("pageSize");
			int currPage = (int) map.get("currPage");
			List<User> currentPageList = new ArrayList<>();
			if (list != null && list.size() > 0) {
				int currIdx = (currPage > 1 ? (currPage - 1) * pageSize : 0);
				for (int i = 0; i < pageSize && i < list.size() - currIdx; i++) {
					User data = list.get(currIdx + i);
					currentPageList.add(data);
				}
			}
			return currentPageList;
		}


		// 3. 没有找到就查MySQL数据库
		list = userMapper.getUserByPage(map);

		// 4. 存到redis中
//		redisTemplate.opsForValue().set("pageUserInfo", JSON.toJSONString(list));
		//注意，这里我偷懒了，redis中应该储存的是所有的结果，而我这里取到的只是分页的2条数据
		Map<String, Object> myMap = new HashMap<>();
		myMap.put("startIndex", 0);//模拟一下所有数据吧
		myMap.put("pageSize", 100);//模拟一下所有数据吧
		List<User> list1 = userMapper.getUserByPage(myMap);
		redisTemplate.opsForValue().set("userList", JSON.toJSONString(list1));


		System.out.println("查询MysQL");
		// 返回响应的集合数据
		return list;
	}

	@Override
	public int getUserTotal() {
		return userMapper.getUserTotal();
	}

	@Override
	public int insert(User user) {
		return userMapper.insert(user);
	}

	@Override
	public int insertSelective(User user) {
		return userMapper.insertSelective(user);
	}

	@Override
	public User selectByPrimaryKey(Integer id) {
		return userMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(User record) {
		return userMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return userMapper.deleteByPrimaryKey(id);
	}
}
