package com.ems.algasensors.temperature.monitoring.infrastructure.rabbitmq;

import com.ems.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import com.ems.algasensors.temperature.monitoring.domain.service.SensorAlertService;
import com.ems.algasensors.temperature.monitoring.domain.service.TemperatureMonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

import static com.ems.algasensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE_ALERTING;
import static com.ems.algasensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE_PROCESS_TEMPERATURE;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    private final SensorAlertService sensorAlertService;
    private final TemperatureMonitoringService temperatureMonitoringService;

    @SneakyThrows
    @RabbitListener(queues = QUEUE_PROCESS_TEMPERATURE, concurrency = "2-3")
    public void handleProcessTemperature(@Payload TemperatureLogData temperatureLogData, @Headers Map<String, Object> headers) {
        log.info("Received message from RabbitMQ. SensorId: {}, Temperature: {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
        log.info("Headers: {}", headers);

        temperatureMonitoringService.processTemperatureReading(temperatureLogData);
        Thread.sleep(Duration.ofSeconds(5));
    }

    @SneakyThrows
    @RabbitListener(queues = QUEUE_ALERTING, concurrency = "2-3")
    public void handleAlert(@Payload TemperatureLogData temperatureLogData) {
        log.info("Received message from RabbitMQ in alert listener. SensorId: {}, Temperature: {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
        sensorAlertService.handleAlert(temperatureLogData);
    }

}
