package com.aep6s.repository;

import com.aep6s.enums.StatusAdocao;
import com.aep6s.model.AnimalModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AnimalRepository extends MongoRepository<AnimalModel, String> {

    List<AnimalModel> findByStatus(StatusAdocao status);

    List<AnimalModel> findByEspecieIgnoreCase(String especie);
}

