package com.atguigu.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.atguigu.pojo.PortalVo;
import com.atguigu.pojo.UserHistory;
import com.atguigu.utils.JwtHelper;
import com.atguigu.utils.Result;
import com.atguigu.utils.ResultCodeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.pojo.Headline;
import com.atguigu.service.HeadlineService;
import com.atguigu.mapper.HeadlineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
* @author 73823
* @description 针对表【news_headline】的数据库操作Service实现
* @createDate 2024-03-29 17:53:40
*/
@Service
public class HeadlineServiceImpl extends ServiceImpl<HeadlineMapper, Headline>
    implements HeadlineService{

    @Autowired
    private HeadlineMapper headlineMapper;

    @Autowired
    private JwtHelper jwtHelper;

    @Override
    public Result findNewsPage(PortalVo portalVo) {
        LambdaQueryWrapper<Headline> headlineLambdaQueryWrapper = new LambdaQueryWrapper<>();
        headlineLambdaQueryWrapper.like(!StringUtils.isEmpty(portalVo.getKeyWords()), Headline::getTitle, portalVo.getKeyWords())
                .eq(portalVo.getType() != null, Headline::getType, portalVo.getType());


        IPage<Map> page = new Page<>(portalVo.getPageNum(), portalVo.getPageSize());
        headlineMapper.selectMyPage(page, portalVo);

        List<Map> records = page.getRecords();
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
    public Result showDetail(Integer hid, Integer uid) {

        //1.实现根据id的查询(多表
        Map headLineDetail = headlineMapper.selectDetailMap(hid);
        //2.拼接头条对象(阅读量和version)进行数据更新
        Headline headline = new Headline();
        headline.setHid(hid);
        headline.setPageViews((Integer) headLineDetail.get("pageViews")+1); //阅读量+1
        headline.setVersion((Integer) headLineDetail.get("version")); //设置版本
        headlineMapper.updateById(headline);

        if (uid != -1) {
            UserHistory history = headlineMapper.findHistory(hid, uid);
            if (history == null){
                headlineMapper.addHistory(hid, uid);
            } else {
                headlineMapper.updateHistory(hid, uid);
            }
        }

        Map<String,Object> pageInfoMap=new HashMap<>();
        pageInfoMap.put("headline",headLineDetail);
        return Result.ok(pageInfoMap);
    }

    @Override
    public Result publish(Headline headline, String token) {

        int userId = jwtHelper.getUserId(token).intValue();

        headline.setPublisher(userId);
        headline.setPageViews(0);
        headline.setCreateTime(new Date());
        headline.setUpdateTime(new Date());

        int insert = headlineMapper.insert(headline);

        if (insert > 0){
            return Result.ok(null);
        }

        return Result.build(null, ResultCodeEnum.NOTLOGIN);

    }

    @Override
    public Result findHeadlineByHid(String hid) {
        Headline headline = headlineMapper.selectById(hid);
        Map<String, Headline> data = new HashMap<>();
        data.put("headline", headline);
        return Result.ok(data);

    }

    @Override
    public Result updateHeadline(Headline headline) {
        Integer version = headlineMapper.selectById(headline.getHid()).getVersion();

        headline.setVersion(version);
        headline.setUpdateTime(new Date());

        headlineMapper.updateById(headline);
        return Result.ok(headline);
    }
}




