package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> users = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp result = new Resp("No data available", "204");
        if ("POST".equals(req.getHttpRequestType())) {
            for (var user : users.keySet()) {
                ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> userMap = users.get(user);
                ConcurrentLinkedQueue<String> userSource = userMap.get(req.getSourceName());
                if (userSource != null) {
                    userSource.offer(req.getParam());
                }
            }
            result = new Resp("Add to all available " + req.getSourceName()
                    + " value = " + req.getParam(), "200");
        } else if ("GET".equals(req.getHttpRequestType())) {
            users.putIfAbsent(req.getParam(), new ConcurrentHashMap<>());
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> userMap = users.get(req.getParam());
            userMap.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
            String text = userMap.get(req.getSourceName()).poll();
            if (text != null) {
                result = new Resp(text, "200");
            }
        }
        return result;
    }
}
