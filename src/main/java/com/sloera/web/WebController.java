package com.sloera.web;

import com.alibaba.fastjson.JSONObject;
import com.sloera.utils.ProducerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@Controller
public class WebController {
    private static Logger logger = LogManager.getLogger(WebController.class);

    @RequestMapping(value = "/producer", method = RequestMethod.GET)
    public void producer(HttpServletRequest request, HttpServletResponse response) {
        JSONObject acceptJS = new JSONObject();
        acceptJS.put("applyFrom","net");
        Message msg = null;
        try {
            msg = new Message("TOPIC-A",// topic
                    "ACCEPT",// tag
                    (acceptJS.toJSONString()).getBytes(RemotingHelper.DEFAULT_CHARSET)// body
            );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            //调用producer的send()方法发送消息
            //这里调用的是同步的方式，所以会有返回结果
            SendResult sendResult = ProducerService.getProducer().send(msg);

            //打印返回结果，可以看到消息发送的状态以及一些相关信息
            System.out.println(sendResult);
            //logger.error(sendResult);
        } catch (Exception exc) {
            logger.error(exc);
        }
    }
}
