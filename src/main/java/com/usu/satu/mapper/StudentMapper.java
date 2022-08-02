package com.usu.satu.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.usu.satu.dto.StudentRequest;
import com.usu.satu.model.Student;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface StudentMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStudent(StudentRequest student, @MappingTarget Student entity, String path) throws JsonProcessingException;
}
