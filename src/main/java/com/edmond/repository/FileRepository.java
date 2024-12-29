package com.edmond.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edmond.entity.FileDetails;

public interface FileRepository extends JpaRepository<FileDetails,Long>{

}
