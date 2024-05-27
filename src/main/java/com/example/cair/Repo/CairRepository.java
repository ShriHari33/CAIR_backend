package com.example.cair.Repo;

import com.example.cair.Entity.Cair;
import com.example.cair.Entity.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface CairRepository extends MongoRepository<Cair,String> {
    Optional<Cair> findByEmail(String email);
}