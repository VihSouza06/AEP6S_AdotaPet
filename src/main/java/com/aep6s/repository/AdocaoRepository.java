package com.aep6s.repository;

import com.aep6s.model.AdocaoModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AdocaoRepository extends MongoRepository<AdocaoModel, String> {

    List<AdocaoModel> findByAdotanteId(String adotanteId);

    boolean existsByAdotanteId(String adotanteId);
}
