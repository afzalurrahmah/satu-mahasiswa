package com.usu.satu.mapper;

import com.usu.satu.dto.AchieveRequest;
import com.usu.satu.model.Achievement;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface AchieveMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAchieve(AchieveRequest achieve, @MappingTarget Achievement entity, String path);
}
