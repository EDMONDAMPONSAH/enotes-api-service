package com.edmond.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edmond.entity.Notes;

public interface NotesRepository extends JpaRepository<Notes, Long> {

}
