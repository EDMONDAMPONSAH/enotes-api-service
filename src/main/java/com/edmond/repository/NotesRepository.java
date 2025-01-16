package com.edmond.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.edmond.entity.Notes;

public interface NotesRepository extends JpaRepository<Notes, Long> {

	Page<Notes> findByCreatedBy(Integer userId, Pageable pageable);

}
