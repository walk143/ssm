package com.sloera.utils;


import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Component
@Service
public class ProducerService {
    private static String nameServerAccept = "127.0.0.1:9876";
    private static String groupIDAccept ="producer-group";
    private String topicAccept = "PUSH_OTHER";
    private static DefaultMQProducer producer = null;

    @PostConstruct
    public void initProducerService() throws MQClientException {
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaa");
        producer = new DefaultMQProducer(groupIDAccept);
        producer.setNamesrvAddr(nameServerAccept);
        producer.setInstanceName("producer");
        producer.start();
    }


    public static DefaultMQProducer getProducer() throws MQClientException {
        System.out.println("bbbbbbbbbbbbbbbbbbbb");
        return producer;
    }
}
