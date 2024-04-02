package com.atguigu.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.atguigu.mapper.HeadlineMapper;
import com.atguigu.pojo.UserHistory;
import com.atguigu.pojo.UserHistoryVo;
import com.atguigu.utils.JwtHelper;
import com.atguigu.utils.MD5Util;
import com.atguigu.utils.Result;
import com.atguigu.utils.ResultCodeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.pojo.User;
import com.atguigu.service.UserService;
import com.atguigu.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 73823
* @description 针对表【news_user】的数据库操作Service实现
* @createDate 2024-03-29 17:53:40
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private HeadlineMapper headlineMapper;

    @Override
    public Result login(User user) {
        //根据账号查询
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,user.getUsername());
        User loginUser = userMapper.selectOne(queryWrapper);

        if (loginUser == null){
            return Result.build(null, ResultCodeEnum.USERNAME_ERROR);
        }

        if (!StringUtils.isEmpty(user.getUserPwd())
                && loginUser.getUserPwd().equals(MD5Util.encrypt(user.getUserPwd()))){
            String token = jwtHelper.createToken(Long.valueOf(loginUser.getUid()));
            Map map = new HashMap();
            map.put("token", token);
            return Result.ok(map);
        }

        return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);

    }

    @Override
    public Result getUserInfo(String token) {

        if(jwtHelper.isExpiration(token)){
            return Result.build(null, ResultCodeEnum.NOTLOGIN);
        }

        int uid = jwtHelper.getUserId(token).intValue();

        User user = userMapper.selectById(uid);

        if (user != null) {
            user.setUserPwd(null);
            Map data = new HashMap<>();
            data.put("loginUser", user);
            return Result.ok(data);
        }

        return Result.build(null, ResultCodeEnum.NOTLOGIN);
    }

    @Override
    public Result checkUserName(String username) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(userLambdaQueryWrapper);

        if (user == null){
            return Result.ok(null);
        }

        return Result.build(null, ResultCodeEnum.USERNAME_USED);
    }

    @Override
    public Result regist(User user) {

        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername, user.getUsername());
        User newUser = userMapper.selectOne(userLambdaQueryWrapper);

        if (newUser != null) {
            return Result.build(null, ResultCodeEnum.USERNAME_USED);
        }

        user.setUserPwd(MD5Util.encrypt(user.getUserPwd()));
        userMapper.insert(user);
        return Result.ok(null);
    }

    @Override
    public Result findHistory(UserHistoryVo historyVo) {
        Integer pageNum = historyVo.getPageNum();
        Integer pageSize = historyVo.getPageSize();

        IPage<Map> page = new Page<>(pageNum, pageSize);

        headlineMapper.selectMyHis(page, historyVo);
        List<Map> records = page.getRecords();
        System.out.println("records = " + records);
        Map data = new HashMap();
        data.put("pageData", records);
        data.put("pageNum", page.getCurrent());
        data.put("pageSize", page.getSize());
        data.put("totalPage", page.getPages());
        data.put("totalSize", page.getTotal());

        Map pageInfo = new HashMap();
        pageInfo.put("pageInfo", data);

        return Result.ok(pageInfo);
    }

    @Override
    public Result changePasswordValidate(String username, String password) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername, username);

        User user = userMapper.selectOne(userLambdaQueryWrapper);

        if (MD5Util.encrypt(password).equals(user.getUserPwd())) {
            return Result.ok(null);
        }
        return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
    }

    @Override
    public Result changePassword(User user) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername, user.getUsername());

        user.setUserPwd(MD5Util.encrypt(user.getUserPwd()));

        userMapper.update(user, userLambdaQueryWrapper);

        return Result.ok(null);
    }
}




