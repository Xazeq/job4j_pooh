package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp result = new Resp("No data available", "204");
        if ("POST".equals(req.getHttpRequestType())) {
            queue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
            queue.get(req.getSourceName()).offer(req.getParam());
            result = new Resp("Add to " + req.getSourceName() + " value = " + req.getParam(), "200");
        } else if ("GET".equals(req.getHttpRequestType())) {
            ConcurrentLinkedQueue<String> paramQueue = queue.get(req.getSourceName());
            if (paramQueue != null) {
                String text = paramQueue.poll();
                result = text != null ? new Resp(text, "200") : result;
            }
        }
        return result;
    }
}
