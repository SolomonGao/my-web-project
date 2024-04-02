package com.atguigu.controller;

import com.atguigu.pojo.Headline;
import com.atguigu.service.HeadlineService;
import com.atguigu.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * TODO
 *
 * @Description:
 * @Author: Xing Gao
 * @Time: 3/30/2024 7:17 PM
 * @Version: 1.0
 */

@RestController
@RequestMapping("headline")
@CrossOrigin
public class HeadlineController {

    @Autowired
    private HeadlineService headlineService;

    @PostMapping("publish")
    public Result publish(@RequestBody Headline headline, @RequestHeader String token){
        Result result =  headlineService.publish(headline, token);
        return result;
    }

    @PostMapping("findHeadlineByHid")
    public Result findHeadlineByHid(String hid) {
        Result result = headlineService.findHeadlineByHid(hid);
        return result;
    }

    @PostMapping("update")
    public Result update(@RequestBody Headline headline) {
        Result result = headlineService.updateHeadline(headline);
        return result;
    }

    @PostMapping("removeByHid")
    public Result remove(Integer hid) {
        headlineService.removeById(hid);
        return Result.ok(null);
    }
}
