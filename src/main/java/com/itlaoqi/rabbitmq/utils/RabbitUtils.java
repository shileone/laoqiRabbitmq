package com.itlaoqi.rabbitmq.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitUtils {
    private static ConnectionFactory connectionFactory =  new ConnectionFactory();

    static{
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setUsername("guest");
        connectionFactory.setVirtualHost("/test");
    }

    public static Connection getConnection(){
        Connection conn = null;
        try{

            conn = connectionFactory.newConnection();
            return conn;
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

}
