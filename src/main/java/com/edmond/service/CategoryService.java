package com.edmond.service;

import java.util.List;

import com.edmond.dto.CategoryDto;
import com.edmond.dto.CategoryResponse;
import com.edmond.entity.Category;
import com.edmond.exception.ResourceNotFoundException;

public interface CategoryService {

	public Boolean saveCategory(CategoryDto categoryDto);
	public List<CategoryDto> getAllCategory();
	public List<CategoryResponse> getActiveCategory();
	public CategoryDto getCategoryById(Long id) throws ResourceNotFoundException;
	public Boolean deleteCategory(Long id);
}
