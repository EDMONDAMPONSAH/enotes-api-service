package com.edmond.service;

import java.util.List;

import com.edmond.entity.Category;

public interface CategoryService {

	public Boolean saveCategory(Category category);
	public List<Category> getAllCategory();
}
