package com.edmond.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edmond.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findByIsActiveTrueAndIsDeletedFalse();

	Optional<Category> findByIdAndIsDeletedFalse(Long id);

	List<Category> findByIsDeletedFalse();

}
