package com.zz.core.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class LogMessage implements Serializable {

    private static final long serialVersionUID = 7997868536654384119L;

    /**
     * traceId
     */
    private String traceId;

    /**
     * 客户端，服务
     */
    private String client;

    /**
     * 操作用户
     */
    private String userName;

    /**
     * 操作内容
     */
    private String operation;

    /**
     * 來源请求url
     */
    private String oriUrl;

    /**
     * 耗时
     */
    private Long time;

    /**
     * 操作方法
     */
    private String method;

    /**
     * 請求參數
     */
    private String request;

    /**
     * 結果
     */
    private String response;

    /**
     * 0-操作成功，1-操作失敗
     */
    private Integer status;


    /**
     * 操作者IP
     */
    private String ip;

    /**
     * 创建时间
     */
    private String createTime;
}
