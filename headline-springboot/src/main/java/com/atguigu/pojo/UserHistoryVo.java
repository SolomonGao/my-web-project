package com.atguigu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * TODO
 *
 * @Description:
 * @Author: Xing Gao
 * @Time: 3/7/2024 10:21 PM
 * @Version: 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserHistoryVo implements Serializable {

    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Integer uid;
}
