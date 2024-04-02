package com.atguigu.controller;

import com.atguigu.pojo.UserHistoryVo;
import com.atguigu.pojo.User;
import com.atguigu.service.UserService;
import com.atguigu.utils.JwtHelper;
import com.atguigu.utils.Result;
import com.atguigu.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @Description:
 * @Author: Xing Gao
 * @Time: 3/29/2024 6:00 PM
 * @Version: 1.0
 */
@CrossOrigin
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("login")
    public Result login(@RequestBody User user){

        Result result = userService.login(user);
        return result;
    }

    @GetMapping("getUserInfo")
    public Result userInfo(@RequestHeader(value = "token") String token){

        Result result = userService.getUserInfo(token);
        return result;
    }

    @PostMapping("checkUserName")
    public Result checkUserName( String username){
        Result result = userService.checkUserName(username);
        return result;
    }

    @PostMapping("regist")
    public Result regist(@RequestBody User user){
        Result result = userService.regist(user);
        return result;
    }

    @GetMapping("checkLogin")
    public Result checkLogin(@RequestHeader String token){
        boolean expiration = jwtHelper.isExpiration(token);

        if (expiration) {
            return Result.build(null, ResultCodeEnum.NOTLOGIN);
        }

        return Result.ok(null);

    }

    @PostMapping("findHistory")
    public Result findHistory(@RequestBody UserHistoryVo historyVo) {

        Result result = userService.findHistory(historyVo);
        return result;
    }

    @PostMapping("changePasswordValidate")
    public Result changePasswordValidate(String username, String password) {
        Result result = userService.changePasswordValidate(username, password);
        return result;
    }

    @PutMapping("changePassword")
    public Result changePassword(@RequestBody User user){
        Result result = userService.changePassword(user);
        return result;
    }

    @PostMapping("follow")
    public Result follow(String uid) {
        return  null;
    }

    @GetMapping("checkFollowed")
    public Result checkFollowed(String uid) {
        return null;
    }

}
