package com.edmond.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edmond.dto.CategoryDto;
import com.edmond.dto.CategoryResponse;
import com.edmond.entity.Category;
import com.edmond.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

	private final CategoryService cService;

	@PostMapping("/save-category")
	public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto) {
		Boolean saveCategory = cService.saveCategory(categoryDto);
		if (saveCategory) {
			return new ResponseEntity<>("successfully saved", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("failed to save", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/")
	public ResponseEntity<?> getAllCategory() {
		List<CategoryDto> allCategory = cService.getAllCategory();
		if (CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();
		} else {
			return new ResponseEntity<>(allCategory, HttpStatus.OK);
		}
	}

	@GetMapping("/active")
	public ResponseEntity<?> getActiveCategory() {
		List<CategoryResponse> allCategory = cService.getActiveCategory();
		if (CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();
		} else {
			return new ResponseEntity<>(allCategory, HttpStatus.OK);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getCategoryDetailsById(@PathVariable Long id) {
		CategoryDto categoryDto = cService.getCategoryById(id);
		if (ObjectUtils.isEmpty(categoryDto)) {
			return new ResponseEntity<>("Category not found with id=" + id, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(categoryDto, HttpStatus.OK);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCategoryById(@PathVariable Long id) {
		Boolean deleted = cService.deleteCategory(id);
		if (deleted) {
			return new ResponseEntity<>("Deleted sucessfully", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Category not found with id=", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
