//package com.usu.satu.mapper;
//
//import com.usu.satu.model.StudentStatus;
//import org.mapstruct.BeanMapping;
//import org.mapstruct.MappingTarget;
//import org.mapstruct.NullValuePropertyMappingStrategy;
//
//import java.util.HashMap;
//
//public interface StatusMapper {
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void addStatus(HashMap<String,String> hashMap, @MappingTarget StudentStatus studentStatus, String nim);
//}
