package com.atguigu.service;

import com.atguigu.pojo.User;
import com.atguigu.pojo.UserHistory;
import com.atguigu.pojo.UserHistoryVo;
import com.atguigu.utils.Result;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author 73823
* @description 针对表【news_user】的数据库操作Service
* @createDate 2024-03-29 17:53:40
*/
public interface UserService extends IService<User> {

    Result login(User user);

    Result getUserInfo(String token);

    Result checkUserName(String username);

    Result regist(User user);

    Result findHistory(UserHistoryVo historyVo);

    Result changePasswordValidate(String username, String password);

    Result changePassword(User user);
}
