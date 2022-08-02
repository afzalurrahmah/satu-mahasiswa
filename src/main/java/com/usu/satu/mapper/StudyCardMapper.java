package com.usu.satu.mapper;

import com.usu.satu.model.StudyCard;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface StudyCardMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStudyCard(StudyCard studyCard, @MappingTarget StudyCard entity, String periodId);
}
