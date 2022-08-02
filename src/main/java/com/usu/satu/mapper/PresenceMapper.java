package com.usu.satu.mapper;

import com.usu.satu.model.Presence;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface PresenceMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePresence(Presence presence, @MappingTarget Presence entity);
}
