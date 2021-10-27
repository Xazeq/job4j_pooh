package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topics = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp result = new Resp("No data available", "204");
        if ("POST".equals(req.getHttpRequestType())) {
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> topicMap = topics.get(req.getSourceName());
            if (topicMap != null) {
                for (var consumer : topicMap.keySet()) {
                    topicMap.get(consumer).offer(req.getParam());
                }
                result = new Resp("Add to all available " + req.getSourceName()
                        + " value = " + req.getParam(), "200");
            }
        } else if ("GET".equals(req.getHttpRequestType())) {
            topics.putIfAbsent(req.getSourceName(), new ConcurrentHashMap<>());
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> topicMap = topics.get(req.getSourceName());
            topicMap.putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
            String text = topicMap.get(req.getParam()).poll();
            if (text != null) {
                result = new Resp(text, "200");
            }
        }
        return result;
    }
}
