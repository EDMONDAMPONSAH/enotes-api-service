package com.edmond.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edmond.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
