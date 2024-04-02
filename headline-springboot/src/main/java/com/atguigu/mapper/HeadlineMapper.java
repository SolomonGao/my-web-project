package com.atguigu.mapper;

import com.atguigu.pojo.Headline;
import com.atguigu.pojo.PortalVo;
import com.atguigu.pojo.UserHistory;
import com.atguigu.pojo.UserHistoryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* @author 73823
* @description 针对表【news_headline】的数据库操作Mapper
* @createDate 2024-03-29 17:53:40
* @Entity com.atguigu.pojo.Headline
*/
public interface HeadlineMapper extends BaseMapper<Headline> {

    IPage<Map> selectMyPage(IPage<Map> page, @Param("portalVo") PortalVo portalVo);

    Map selectDetailMap(@Param("hid") Integer hid);

    IPage<Map> selectMyHis(IPage<Map> page, @Param("historyVo") UserHistoryVo userHistoryVo);

    int addHistory(@Param("hid") int hid,@Param("uid") int uid);

    UserHistory findHistory(@Param("hid") Integer hid,@Param("uid") Integer uid);

    int updateHistory(@Param("hid") Integer hid,@Param("uid") Integer uid);
}




