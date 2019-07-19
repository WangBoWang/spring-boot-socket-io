package com.bowang.wang.messgae.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangb
 * @version 1.0.0
 * @since 2019/7/19
 */
@Data
@NoArgsConstructor
public class PushMessage {

    /**
     * 登录用户编号
     */
    private String loginUserNum;

    /**
     * 推送内容
     */
    private String content;
}
