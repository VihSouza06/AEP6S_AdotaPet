package com.aep6s.repository;

import com.aep6s.model.AdotanteModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdotanteRepository extends MongoRepository<AdotanteModel, String> {

    boolean existsByEmailIgnoreCase(String email);
}
