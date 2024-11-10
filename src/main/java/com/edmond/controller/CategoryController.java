package com.edmond.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edmond.entity.Category;
import com.edmond.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

	private final CategoryService cService;

	@PostMapping("/save-category")
	public ResponseEntity<?> saveCategory(@RequestBody Category category) {
		Boolean saveCategory = cService.saveCategory(category);
		if (saveCategory) {
			return new ResponseEntity<>("successfully saved", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("failed to save", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/category")
	public ResponseEntity<?> getAllCategory() {
		List<Category> allCategory = cService.getAllCategory();
		if (CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();
		} else {
			return new ResponseEntity<>(allCategory, HttpStatus.OK);
		}
	}
}
