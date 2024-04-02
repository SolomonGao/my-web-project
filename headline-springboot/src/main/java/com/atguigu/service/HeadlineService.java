package com.atguigu.service;

import com.atguigu.pojo.Headline;
import com.atguigu.pojo.PortalVo;
import com.atguigu.utils.Result;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 73823
* @description 针对表【news_headline】的数据库操作Service
* @createDate 2024-03-29 17:53:40
*/
public interface HeadlineService extends IService<Headline> {

    Result findNewsPage(PortalVo portalVo);

    Result showDetail(Integer hid, Integer uid);

    Result publish(Headline headline, String token);

    Result findHeadlineByHid(String hid);

    Result updateHeadline(Headline headline);
}
