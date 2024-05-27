package com.example.cair.Repo;
import com.example.cair.Entity.FileUpload;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepo extends MongoRepository<FileUpload, String> {
     FileUpload findByFileName(String fileName);
}

