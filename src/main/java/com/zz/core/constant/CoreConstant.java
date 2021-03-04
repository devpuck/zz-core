package com.zz.core.constant;

/**
 * 常量集合
 */
public class CoreConstant
{

    /**
     * 请求标识
     */
    public static final String UNIQUE_ID = "traceId";

    /**
     * 请求未知用户
     */
    public static final String UNKOWN = "UNKOWN";

    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * 操作日志消息exchange名称
     */
    public static final String OP_LOG_EXCHANGE_NAME = "operation-exchange";

    /**
     * 操作日志消息队列名称
     */
    public static final String OP_LOG_QUEUE_NAME = "operation-log-queue";

    /**
     * 操作日志消息标识
     */
    public static final String OP_LOG_ROUTING_KEY = "operation.log";

    /**
     * 调度任务消息队列名称
     */
    public static final String SCHEDULE_EXCHANGE_NAME = "schedule-exchange";

    /**
     * 默认时间格式
     */
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认页码为1
     */
    public static final Integer DEFAULT_PAGE_INDEX = 1;

    /**
     * 默认页大小为10
     */
    public static final Integer DEFAULT_PAGE_SIZE = 10;

    /**
     * 默认CREATED_DATE
     */
    public static final String CREATED_DATE = "CREATED_DATE";

    /**
     * restTemplate 服务调用
     */
    public static final String REQ_METHOD = "Req-Method";

    /**
     * restTemplate 服务调用
     */
    public static final String REQ_REST = "restTemplate";

    /**
     * GRPC 服务调用
     */
    public static final String REQ_GRPC = "gRpc";

    /**
     * Content-Type
     */
    public static final String CONTENT_TYPE = "Content-Type";

    /**
     * application/json;charset=UTF-8
     */
    public static final String CONTENT_JSON = "application/json;charset=UTF-8";


    /**
     * MRP运算相关任务的默认地址IP
     */
    public static final String DEFAULT_IP = "127.0.0.1";
}
