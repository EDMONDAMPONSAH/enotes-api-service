package com.edmond.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.edmond.dto.CategoryDto;
import com.edmond.dto.CategoryResponse;
import com.edmond.entity.Category;
import com.edmond.exception.ExistDataException;
import com.edmond.exception.ResourceNotFoundException;
import com.edmond.repository.CategoryRepository;
import com.edmond.service.CategoryService;
import com.edmond.util.Validation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository cRepo;
	private final ModelMapper mapper;
	private final Validation validation;
	@Override
	public Boolean saveCategory(CategoryDto categoryDto) {

		/*
		 * Category category = new Category(); category.setName(categoryDto.getName());
		 * category.setDescription(categoryDto.getDescription());
		 * category.setIsActive(categoryDto.getIsActive());
		 */

		// Validation checking
		validation.categoryValidation(categoryDto);
		
		// check category exist or not
		Boolean exist=cRepo.existsByName(categoryDto.getName().trim());
		if(exist) {
			throw new ExistDataException("Category already exist");
		}
		

		Category category = mapper.map(categoryDto, Category.class);

		if (ObjectUtils.isEmpty(category.getId())) {
			category.setIsDeleted(false);
			category.setCreatedBy(1);
			category.setCreatedOn(new Date());
		} else {
			updateCategory(category);
		}
		Category saveCategory = cRepo.save(category);
		if (ObjectUtils.isEmpty(saveCategory)) {
			return false;
		}

		return true;
	}

	private void updateCategory(Category category) {
		Optional<Category> findById = cRepo.findById(category.getId());
		if (findById.isPresent()) {
			Category existCategory = findById.get();
			category.setCreatedBy(existCategory.getCreatedBy());
			category.setCreatedOn(existCategory.getCreatedOn());
			category.setUpdatedBy(1);
			// category.setIsDeleted(existCategory.getIsDeleted());
			category.setUpdatedOn(new Date());
		}
	}

	@Override
	public List<CategoryDto> getAllCategory() {
		List<Category> categories = cRepo.findByIsDeletedFalse();
		List<CategoryDto> categoryDtoList = categories.stream().map(cat -> mapper.map(cat, CategoryDto.class)).toList();
		return categoryDtoList;
	}

	@Override
	public List<CategoryResponse> getActiveCategory() {
		List<Category> categories = cRepo.findByIsActiveTrueAndIsDeletedFalse();
		List<CategoryResponse> activeCategoryList = categories.stream()
				.map(cat -> mapper.map(cat, CategoryResponse.class)).toList();
		return activeCategoryList;
	}

	@Override
	public CategoryDto getCategoryById(Long id) throws ResourceNotFoundException {
		Category category = cRepo.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
		return mapper.map(category, CategoryDto.class);
	}

	@Override
	public Boolean deleteCategory(Long id) {
		Optional<Category> findByCategory = cRepo.findById(id);
		if (findByCategory.isPresent()) {
			Category category = findByCategory.get();
			category.setIsDeleted(true);
			cRepo.save(category);
			return true;
		}
		return false;
	}

}
