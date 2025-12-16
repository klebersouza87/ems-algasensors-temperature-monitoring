package com.ems.algasensors.temperature.monitoring.infrastructure.rabbitmq;

import com.ems.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

import static com.ems.algasensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    @SneakyThrows
    @RabbitListener(queues = QUEUE)
    public void handleMessage(@Payload TemperatureLogData temperatureLogData, @Headers Map<String, Object> headers) {
        log.info("Received message from RabbitMQ. SensorId: {}, Temperature: {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
        log.info("Headers: {}", headers);

        Thread.sleep(Duration.ofSeconds(5));
    }

}
