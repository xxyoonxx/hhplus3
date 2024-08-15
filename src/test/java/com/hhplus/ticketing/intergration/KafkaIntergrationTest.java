package com.hhplus.ticketing.intergration;

import com.hhplus.ticketing.common.kafka.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(partitions = 1,
        brokerProperties = {"listeners=PLAINTEXT://localhost:9092"},
        ports = { 9092 }
)
public class KafkaIntergrationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    private KafkaProducer kafkaProducer;

    private static final String TOPIC = "testTopic";

    private String receivedMessage;

    private CountDownLatch latch = new CountDownLatch(1);

    @Test
    void testKafkaProducerAndConsumer() throws Exception {
        // Given
        String message = "testMessage";

        // When
        kafkaProducer.publish(TOPIC, message);

        // Then
        latch.await(20, TimeUnit.SECONDS);
        assertThat(receivedMessage).isEqualTo(message);
    }

    @KafkaListener(topics = TOPIC, groupId = "testGroup")
    public void listen(String message) {
        receivedMessage = message;
        latch.countDown();
    }

}
