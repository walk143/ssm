package com.sloera.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Component
@Service
public class ConsumerService {
    private static Logger logger = LogManager.getLogger(ConsumerService.class);
	private String nameServerAccept = "192.168.1.166:9876";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Resource
    public  Properties appConstant;


	@PostConstruct
	public void consumerAccpetMsg() throws MQClientException {

		String groupIDAccept ="consumer-group";
		//String topicAccept = "PUSH_OTHER";
		String topicAccept = "TOPIC-A";
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupIDAccept);
		consumer.setNamesrvAddr(nameServerAccept);
		consumer.setInstanceName("consumer");
		consumer.subscribe(topicAccept,"");
		System.out.println("ddddddddddddddddddddddddd");
		consumer.registerMessageListener(new MessageListenerConcurrently() {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
				System.out.println("ccccccccccccccccccccccccc");
				for (MessageExt msg : list) {
					try {
						Map<String, String> map = msg.getProperties();
						if (null != map) {
							String content = new String(msg.getBody(),"UTF-8");
							String tags = map.get("TAGS");
							switch (tags) {
								case "TagA":
									accept("A: "+content);
									break;
								case "TagB":
									accept("B: "+content);
									break;
								case "ACCEPT":
									accept(content);
									break;
								default:
									System.out.println("无匹配标签");
							}
						}
					} catch (Exception e) {
						logger.equals(e);
					}
				}
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		consumer.start();

	}
	private void accept(String content) {
		System.out.println(content);
	}
}
