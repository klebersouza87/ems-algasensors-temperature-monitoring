package com.ems.algasensors.temperature.monitoring.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorAlertInput {

    private Double minTemperature;
    private Double maxTemperature;

}
