package com.zz.core.rabbitmq;

import com.zz.core.constant.CoreConstant;
import com.zz.core.dto.LogMessage;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * rabbitmq 客户端，默认配置在应用yml 配置
 */
@Component
public class AmqpClient {
    private final AmqpAdmin amqpAdmin;
    private final AmqpTemplate amqpTemplate;


    @Autowired
    public AmqpClient(AmqpAdmin amqpAdmin, AmqpTemplate amqpTemplate) {
        this.amqpAdmin = amqpAdmin;
        this.amqpTemplate = amqpTemplate;
    }


    /***
     * 发送消息到默认队列
     * @param message
     */
    public void sendMsg(String message) {
        this.amqpTemplate.convertAndSend(message);
    }

    /***
     * 通过routingKey发送消息到默认队列
     * @param message
     */
    public void sendMsg(String routingKey, String message) {
        this.amqpTemplate.convertAndSend(routingKey, message);
    }

    /***
     * 发送日志
     * @param message
     */
    public void sendLog(LogMessage message) {
        this.amqpTemplate.convertAndSend(CoreConstant.OP_LOG_EXCHANGE_NAME, CoreConstant.OP_LOG_ROUTING_KEY, message);
    }


    /***
     * 需要即时应答返回
     * @param message
     * @return
     */
    public Object sendMsgNeedReceive(String message) {
        return this.amqpTemplate.convertSendAndReceive(message);
    }

    /***
     * 需要即时应答返回
     * @param message
     * @return
     */
    public Object sendMsgNeedReceive(String routingKey, String message) {
        return this.amqpTemplate.convertSendAndReceive(message);
    }

    /***
     * 需要即时应答返回
     * @param message
     * @return
     */
    public Object sendMsgNeedReceive(String exchange, String routingKey, Object message) {
        return this.amqpTemplate.convertSendAndReceive(exchange, routingKey, message);
    }

}
