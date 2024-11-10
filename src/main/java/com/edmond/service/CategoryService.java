package com.edmond.service;

import java.util.List;

import com.edmond.dto.CategoryDto;
import com.edmond.dto.CategoryResponse;
import com.edmond.entity.Category;

public interface CategoryService {

	public Boolean saveCategory(CategoryDto categoryDto);
	public List<CategoryDto> getAllCategory();
	public List<CategoryResponse> getActiveCategory();
}
