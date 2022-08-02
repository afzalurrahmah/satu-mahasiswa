package com.usu.satu.mapper;

import com.usu.satu.dto.AchieveRequest;
import com.usu.satu.model.Achievement;
import com.usu.satu.repository.AchieveRepository;
import com.usu.satu.storage.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class AchieveMapperImpl implements AchieveMapper{
    @Autowired
    AchieveRepository achieveRepository;

    @Autowired
    FilesStorageService storageService;

    @Override
    public void updateAchieve(AchieveRequest achieve, Achievement entity, String path) {
        String filePath     = storageService.saveFile(achieve.getAttachment(), path, achieve.getNim(), "achieve");
        String fileName     = storageService.getFileName(achieve.getAttachment(), achieve.getNim(), "achieve");

        if ( achieve == null ) {
            return;
        }

        if ( achieve.getId() != null ) {
            entity.setId( achieve.getId() );
        }

        if ( achieve.getNim() != null ) {
            entity.setNim( achieve.getNim() );
        }
        if (achieve.getActivity() != null){
            entity.setActivity(achieve.getActivity());
        }
        if (achieve.getType() != null){
            entity.setType(achieve.getType());
        }
        if (achieve.getLevel() != null){
            entity.setLevel(achieve.getLevel());
        }
        if (achieve.getName() != null){
            entity.setName(achieve.getName());
        }
        if (achieve.getYear() != null){
            entity.setYear(achieve.getYear());
        }
        if (achieve.getOrganizer() != null){
            entity.setOrganizer(achieve.getOrganizer());
        }
        if (achieve.getRanking() != null){
            entity.setRanking(achieve.getRanking());
        }
        if (achieve.getAttachment() != null){
            entity.setAttachment(fileName);
            entity.setUrl(filePath);
        }
        entity.setDeleted( false );
    }
}
