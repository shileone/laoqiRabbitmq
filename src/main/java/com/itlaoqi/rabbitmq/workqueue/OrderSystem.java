package com.itlaoqi.rabbitmq.workqueue;

import com.google.gson.Gson;
import com.itlaoqi.rabbitmq.utils.RabbitConstant;
import com.itlaoqi.rabbitmq.utils.RabbitUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.tools.json.JSONUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class OrderSystem {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.confirmSelect();
        channel.addConfirmListener(new ConfirmListener() {
            public void handleAck(long l, boolean b) throws IOException {
                //第二个参数代表接收的数据是否为批量接收，一般我们用不到。
                System.out.println("消息已被Broker接收,Tag:" + l);
            }

            public void handleNack(long l, boolean b) throws IOException {
                System.out.println("消息已被Broker拒收,Tag:" + l);
            }
        });
        channel.queueDeclare(RabbitConstant.QUEUE_SMS,false,false,false,null);
        for(int i=0;i<200;i++){
            SMS sms = new SMS("乘客" + i, "13900000" + i, "您的车票已预订成功");
            String jsonSMS = new Gson().toJson(sms);
            channel.basicPublish("",RabbitConstant.QUEUE_SMS,null,jsonSMS.getBytes());
        }
        System.out.println("发送数据成功");
        channel.close();
        connection.close();

    }
}
