package com.ems.algasensors.temperature.monitoring.api.config.jpa;

import io.hypersistence.tsid.TSID;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/*In all places that is possible to convert TSID attribute to Long and vice versa, this converter will be used.*/
@Converter(autoApply = true)
public class TSIDToLongJPAAttributeConverter implements AttributeConverter<TSID, Long> {

    @Override
    public Long convertToDatabaseColumn(TSID attribute) {
        return attribute.toLong();
    }

    @Override
    public TSID convertToEntityAttribute(Long dbData) {
        return TSID.from(dbData);
    }

}
