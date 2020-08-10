package com.itlaoqi.rabbitmq.workqueue;

import com.itlaoqi.rabbitmq.utils.RabbitConstant;
import com.itlaoqi.rabbitmq.utils.RabbitUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

public class SMSSender2 {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        /**
         * 如果不写basicQos(1),则自动MQ会将所有请求平均发送给所有消费者
         * basicQos，MQ不再对消费者一次发送多个请求，而是消费者处理完一个消息后
         * （确认后），在从队列中获取一个新的
         */
        channel.basicQos(1);
        channel.basicConsume(RabbitConstant.QUEUE_SMS,false,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //super.handleDelivery(consumerTag, envelope, properties, body);
                String jsonSMS = new String(body);
                System.out.println("SMSSender2-短信发送成功:" + jsonSMS);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                channel.basicAck(envelope.getDeliveryTag() , false);
            }
        });
    }
}
