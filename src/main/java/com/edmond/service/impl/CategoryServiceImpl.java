package com.edmond.service.impl;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.edmond.dto.CategoryDto;
import com.edmond.dto.CategoryResponse;
import com.edmond.entity.Category;
import com.edmond.repository.CategoryRepository;
import com.edmond.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository cRepo;
	private final ModelMapper mapper;

	@Override
	public Boolean saveCategory(CategoryDto categoryDto) {

		/*
		 * Category category = new Category(); category.setName(categoryDto.getName());
		 * category.setDescription(categoryDto.getDescription());
		 * category.setIsActive(categoryDto.getIsActive());
		 */

		Category category = mapper.map(categoryDto, Category.class);

		category.setIsDeleted(false);
		category.setCreatedBy(1);
		category.setCreatedOn(new Date());
		Category saveCategory = cRepo.save(category);
		if (ObjectUtils.isEmpty(saveCategory)) {
			return false;
		}

		return true;
	}

	@Override
	public List<CategoryDto> getAllCategory() {
		List<Category> categories = cRepo.findAll();
		List<CategoryDto> categoryDtoList = categories.stream().map(cat -> mapper.map(cat, CategoryDto.class)).toList();
		return categoryDtoList;
	}

	@Override
	public List<CategoryResponse> getActiveCategory() {
		List<Category> categories = cRepo.findByIsActiveTrue();
		List<CategoryResponse> activeCategoryList = categories.stream()
				.map(cat -> mapper.map(cat, CategoryResponse.class)).toList();
		return activeCategoryList;
	}

}
