package com.ems.algasensors.temperature.monitoring.domain.service;

import com.ems.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import com.ems.algasensors.temperature.monitoring.domain.model.SensorId;
import com.ems.algasensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class SensorAlertService {

    private final SensorAlertRepository sensorAlertRepository;

    @Transactional
    public void handleAlert(TemperatureLogData temperatureLogData) {

        sensorAlertRepository.findById(new SensorId(temperatureLogData.getSensorId()))
                .ifPresentOrElse(alert -> {
                            if (alert.getMaxTemperature() != null && temperatureLogData.getValue().compareTo(alert.getMaxTemperature()) >= 0) {
                                log.info("Max temperature alert triggered for SensorId: {}. Temperature Received: {}, MaxThreshold: {}",
                                        temperatureLogData.getSensorId(), temperatureLogData.getValue(), alert.getMaxTemperature());
                            } else if (alert.getMinTemperature() != null && temperatureLogData.getValue().compareTo(alert.getMinTemperature()) <= 0) {
                                log.info("Min temperature alert triggered for SensorId: {}. Temperature Received: {}, MaxThreshold: {}",
                                        temperatureLogData.getSensorId(), temperatureLogData.getValue(), alert.getMaxTemperature());
                            } else {
                                log.info("No alert triggered for SensorId: {}. Temperature Received: {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
                            }
                        },
                        () -> log.info("No alert configuration found for SensorId: {}", temperatureLogData.getSensorId())
                );
    }

}
