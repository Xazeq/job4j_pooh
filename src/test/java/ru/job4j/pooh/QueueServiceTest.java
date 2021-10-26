package ru.job4j.pooh;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class QueueServiceTest {

    @Test
    public void whenPostThenGetQueue() {
        QueueService queueService = new QueueService();
        String paramForPostMethod = "temperature=18";
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod)
        );
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.getText(), is("temperature=18"));
    }

    @Test
    public void whenTryGetBeforePostThenCode204() {
        QueueService queueService = new QueueService();
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.getStatus(), is("204"));
    }

    @Test
    public void whenPostIn2DifferentQueuesThenGetFromQueues() {
        QueueService queueService = new QueueService();
        String paramFirst = "temperature=18";
        String paramSecond = "temperature=21";
        queueService.process(
                new Req("POST", "queue", "weather", paramFirst)
        );
        queueService.process(
                new Req("POST", "queue", "weather2", paramSecond)
        );
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        Resp result2 = queueService.process(
                new Req("GET", "queue", "weather2", null)
        );
        assertThat(result.getText(), is("temperature=18"));
        assertThat(result2.getText(), is("temperature=21"));
    }

    @Test
    public void whenGetFromEmptyQueueThenCode204() {
        QueueService queueService = new QueueService();
        String paramForPostMethod = "temperature=18";
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod)
        );
        queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.getStatus(), is("204"));
    }
}