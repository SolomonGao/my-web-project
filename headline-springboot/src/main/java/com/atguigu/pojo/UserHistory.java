package com.atguigu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * TODO
 *
 * @Description:
 * @Author: Xing Gao
 * @Time: 3/7/2024 10:54 AM
 * @Version: 1.0
 */

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserHistory implements Serializable {
    private Integer uid;
    private Date viewDate;
    private Integer hid;
    private String title;
    private Integer type;
}
