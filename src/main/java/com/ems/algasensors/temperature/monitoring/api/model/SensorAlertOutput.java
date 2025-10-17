package com.ems.algasensors.temperature.monitoring.api.model;

import io.hypersistence.tsid.TSID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorAlertOutput {

    private TSID id;
    private Double minTemperature;
    private Double maxTemperature;

}
