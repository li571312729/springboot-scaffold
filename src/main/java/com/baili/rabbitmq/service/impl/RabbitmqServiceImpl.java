package com.baili.rabbitmq.service.impl;

import com.baili.rabbitmq.config.RabbitmqConfig;
import com.baili.rabbitmq.service.RabbitmqService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RabbitmqServiceImpl implements RabbitmqService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public void sendMsgByTopics(){
        /**
         * 参数：
         * 1、交换机名称
         * 2、routingKey
         * 3、消息内容
         */

            String message = "恭喜您，注册成功！";
            rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_NAME,"topic.sms",message);
            System.out.println(" [x] Sent '" + message + "'");


        }

//    public void sendEmalByTopics(){
//        /**
//         * 参数：
//         * 1、交换机名称
//         * 2、routingKey
//         * 3、消息内容
//         */
//            String message = "恭喜您，注册成功2！";
//            rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_NAME,"topic.#.sms.#",message);
//            System.out.println(" [x] Sent '" + message + "'");
//        }

}
