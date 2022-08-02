package com.usu.satu.service;

import com.usu.satu.dto.AchieveRequest;
import com.usu.satu.mapper.AchieveMapper;
import com.usu.satu.model.Achievement;
import com.usu.satu.repository.AchieveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AchieveService {

    @Autowired
    AchieveRepository achieveRepository;

    @Autowired
    AchieveMapper achieveMapper;

    public Achievement saveAchievement(AchieveRequest achieveRequest) {
        Achievement achievement = new Achievement();

        achieveMapper.updateAchieve(achieveRequest, achievement, "/achievements");
        achieveRepository.save(achievement);

        return achievement;
    }

    public Achievement editAchievement(String id, AchieveRequest achieveRequest){
        Optional<Achievement> data = achieveRepository.findByIdAndIsDeletedIsFalse(id);
        if(data.isPresent()){
            Achievement achievement = data.get();

            achieveMapper.updateAchieve(achieveRequest, achievement, "/achievements");
            achieveRepository.save(achievement);

            return achievement;
        } else {
            return null;
        }
    }

    public Achievement removeAchievement(String id){
        Optional<Achievement> data = achieveRepository.findByIdAndIsDeletedIsFalse(id);
        if(data.isPresent()){
            Achievement achievement = data.get();
            achievement.setDeleted(true);
            achieveRepository.save(achievement);

            return achievement;
        } else {
            return null;
        }
    }

    public Achievement validationAchieve(String id){
        Optional<Achievement> data = achieveRepository.findByIdAndIsDeletedIsFalse(id);
        if(data.isPresent()){
            Achievement achievement = data.get();
            achievement.setValid(true);
            achieveRepository.save(achievement);

            return achievement;
        } else {
            return null;
        }
    }

    public List<Achievement> getAllAchievements(){
        return achieveRepository.findAllByIsDeletedIsFalse();
    }

    public List<Achievement> getAchieveNim(String nim){
        return achieveRepository.findAllByNimAndIsDeletedIsFalse(nim);
    }

    public Optional<Achievement> getAchieveID(String id){
        return achieveRepository.findByIdAndIsDeletedIsFalse(id);
    }
}
