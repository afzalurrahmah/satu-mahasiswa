package com.usu.satu.mapper;

import com.usu.satu.dto.StudentAttendance;
import com.usu.satu.model.Presence;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PresenceMapperImpl implements PresenceMapper{

    @Override
    public void updatePresence(Presence presence, Presence entity) {
        if ( presence == null ) {
            return;
        }

        if ( presence.getId() != null ){
            entity.setId( presence.getId() );
        }

        if ( presence.getClassId() != null ) {
            entity.setClassId( presence.getClassId() );
        }

        List<StudentAttendance> newList     = new ArrayList<>();
        presence.getStudent().forEach( e -> {
            StudentAttendance studentAttendance = new StudentAttendance();
            studentAttendance.setNim(e.getNim());
            studentAttendance.setPresent(e.isPresent());
            newList.add(studentAttendance);
        });

//        if (presence.getPresentAt().equalsIgnoreCase(entity.getPresentAt())){
//            entity.getStudent().clear();
//        }
        entity.setStudent(newList);

        if ( presence.getPresentAt() != null ) {
            entity.setPresentAt( presence.getPresentAt() );
        }
//        else {
//            entity.setPresentAt( entity.getPresentAt() );
//        }

        entity.setDeleted( false );
    }
}
