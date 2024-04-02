package com.atguigu.pojo;

import lombok.Data;

/**
 * TODO
 *
 * @Description:
 * @Author: Xing Gao
 * @Time: 3/30/2024 6:02 PM
 * @Version: 1.0
 */

@Data
public class PortalVo {

    private String keyWords;
    private Integer type;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
