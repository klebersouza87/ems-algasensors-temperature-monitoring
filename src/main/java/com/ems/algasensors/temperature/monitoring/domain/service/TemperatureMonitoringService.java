package com.ems.algasensors.temperature.monitoring.domain.service;

import com.ems.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import com.ems.algasensors.temperature.monitoring.domain.model.SensorId;
import com.ems.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.ems.algasensors.temperature.monitoring.domain.model.TemperatureLog;
import com.ems.algasensors.temperature.monitoring.domain.model.TemperatureLogId;
import com.ems.algasensors.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import com.ems.algasensors.temperature.monitoring.domain.repository.TemperatureLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemperatureMonitoringService {

    private final TemperatureLogRepository temperatureLogRepository;
    private final SensorMonitoringRepository sensorMonitoringRepository;

    @Transactional
    public void processTemperatureReading(TemperatureLogData temperatureLogData) {

        sensorMonitoringRepository.findById(new SensorId(temperatureLogData.getSensorId()))
                .ifPresentOrElse(
                        sensor -> handleSensorMonitoring(temperatureLogData, sensor),
                        () -> log.info("Temperature reading ignored. No monitoring configuration found for SensorId: {}", temperatureLogData.getSensorId()));

    }

    private void handleSensorMonitoring(TemperatureLogData temperatureLogData, SensorMonitoring sensor) {
        if (sensor.isEnabled()) {
            sensor.setLastTemperature(temperatureLogData.getValue());
            sensor.setUpdatedAt(OffsetDateTime.now());
            sensorMonitoringRepository.save(sensor);

            TemperatureLog temperatureLog = TemperatureLog.builder()
                    .id(new TemperatureLogId(temperatureLogData.getId()))
                    .registeredAt(temperatureLogData.getRegisteredAt())
                    .value(temperatureLogData.getValue())
                    .sensorId(new SensorId(temperatureLogData.getSensorId()))
                    .build();

            temperatureLogRepository.save(temperatureLog);
            log.info("Temperature reading processed. SensorId: {}, Temperature: {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
        } else {
            log.info("Temperature reading ignored. Monitoring is disabled for SensorId: {}", temperatureLogData.getSensorId());
        }
    }

}
