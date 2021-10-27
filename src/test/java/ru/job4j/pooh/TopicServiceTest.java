package ru.job4j.pooh;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class TopicServiceTest {

    @Test
    public void when1SubscriberGetDataAnd2SubscriberNoData() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        assertThat(result1.getText(), is("temperature=18"));
        assertThat(result2.getText(), is("No data available"));
    }

    @Test
    public void when2SubscribersGetParam() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        assertThat(result1.getText(), is("temperature=18"));
        assertThat(result2.getText(), is("temperature=18"));
    }

    @Test
    public void whenPostAndGetThenCode204() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        assertThat(result1.getStatus(), is("204"));
    }

    @Test
    public void when2SubscribersGetParamFromDifferentTopics() {
        TopicService topicService = new TopicService();
        String paramForPublisher1 = "temperature=18";
        String paramForPublisher2 = "temperature=25";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        topicService.process(
                new Req("GET", "topic", "weather2", paramForSubscriber1)
        );
        topicService.process(
                new Req("GET", "topic", "weather2", paramForSubscriber2)
        );
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher1)
        );
        topicService.process(
                new Req("POST", "topic", "weather2", paramForPublisher2)
        );
        Resp res1Sub1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        Resp res2Sub1 = topicService.process(
                new Req("GET", "topic", "weather2", paramForSubscriber1)
        );
        Resp res1Sub2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        Resp res2Sub2 = topicService.process(
                new Req("GET", "topic", "weather2", paramForSubscriber2)
        );
        assertThat(res1Sub1.getText(), is("temperature=18"));
        assertThat(res2Sub1.getText(), is("temperature=25"));
        assertThat(res1Sub2.getText(), is("temperature=18"));
        assertThat(res2Sub2.getText(), is("temperature=25"));
    }

    @Test
    public void whenFirstSubGet2ParamAndSecondSubGet1Param() {
        TopicService topicService = new TopicService();
        String paramForPublisher1 = "temperature=18";
        String paramForPublisher2 = "temperature=25";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        topicService.process(
                new Req("GET", "topic", "weather2", paramForSubscriber1)
        );
        topicService.process(
                new Req("GET", "topic", "weather2", paramForSubscriber2)
        );
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher1)
        );
        topicService.process(
                new Req("POST", "topic", "weather2", paramForPublisher2)
        );
        Resp res1Sub1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        Resp res2Sub1 = topicService.process(
                new Req("GET", "topic", "weather2", paramForSubscriber1)
        );
        Resp res1Sub2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        Resp res2Sub2 = topicService.process(
                new Req("GET", "topic", "weather2", paramForSubscriber2)
        );
        assertThat(res1Sub1.getText(), is("temperature=18"));
        assertThat(res2Sub1.getText(), is("temperature=25"));
        assertThat(res1Sub2.getText(), is("No data available"));
        assertThat(res2Sub2.getText(), is("temperature=25"));
    }

    @Test
    public void whenPostInEmptyServiceThenCode204() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        Resp result = topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        assertThat(result.getStatus(), is("204"));
    }

    @Test
    public void whenPostThenCode200() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        Resp result = topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        assertThat(result.getStatus(), is("200"));
    }
}