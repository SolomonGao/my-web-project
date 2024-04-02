package com.atguigu.controller;

import com.atguigu.pojo.PortalVo;
import com.atguigu.pojo.Type;
import com.atguigu.service.HeadlineService;
import com.atguigu.service.TypeService;
import com.atguigu.utils.Result;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO
 *
 * @Description:
 * @Author: Xing Gao
 * @Time: 3/30/2024 5:55 PM
 * @Version: 1.0
 */
@RestController
@RequestMapping("portal")
@CrossOrigin
public class PortalController {

    @Autowired
    private TypeService typeService;
    @Autowired
    private HeadlineService headlineService;

    @GetMapping("findAllTypes")
    public Result findAllType(){
        List<Type> list = typeService.list();
        return Result.ok(list);
    }

    @PostMapping("findNewsPage")
    public Result findNewsPage(@RequestBody PortalVo portalVo){
        Result result = headlineService.findNewsPage(portalVo);
        return result;
    }

    @PostMapping("showHeadlineDetail")
    public Result showHeadlineDetail(@RequestParam(value = "postId") Integer hid, @RequestParam(value = "userId") Integer uid){
        Result result = headlineService.showDetail(hid, uid);
        return result;
    }
}
