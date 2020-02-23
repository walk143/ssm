package com.sloera.utils;


import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Properties;

@Component
@Service
public class ProducerService {
    private static String nameServerAccept = "192.168.1.166:9876";
    private static String groupIDAccept ="producer-group";
    private String topicAccept = "PUSH_OTHER";
    private static DefaultMQProducer producer = null;
    @Resource
    private Properties appConstant;

    @PostConstruct
    public void initProducerService() throws MQClientException {
        String flag = appConstant.getProperty("rocketmq.status");
        System.out.println("producer.flag:"+flag);
        if ("on".equals(flag)) {
            producer = new DefaultMQProducer(groupIDAccept);
            producer.setNamesrvAddr(nameServerAccept);
            producer.setInstanceName("producer");
            producer.start();
        }
    }


    public static DefaultMQProducer getProducer() throws MQClientException {
        System.out.println("bbbbbbbbbbbbbbbbbbbb");
        return producer;
    }
}
