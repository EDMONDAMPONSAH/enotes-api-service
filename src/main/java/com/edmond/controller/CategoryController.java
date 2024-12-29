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
import com.edmond.exception.ResourceNotFoundException;
import com.edmond.service.CategoryService;
import com.edmond.util.CommonUtil;
import com.edmond.util.Validation;

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
			return CommonUtil.createBuildResponseMessage("successfuly saved", HttpStatus.CREATED);
		//	return new ResponseEntity<>("successfully saved", HttpStatus.CREATED);
		} else {
			return CommonUtil.createErrorResponseMessage("failed to save", HttpStatus.INTERNAL_SERVER_ERROR);
			//return new ResponseEntity<>("failed to save", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/")
	public ResponseEntity<?> getAllCategory() {
		List<CategoryDto> allCategory = cService.getAllCategory();
		if (CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();
		} else {
			return CommonUtil.createBuildResponse(allCategory, HttpStatus.OK);
			//return new ResponseEntity<>(allCategory, HttpStatus.OK);
		}
	}

	@GetMapping("/active")
	public ResponseEntity<?> getActiveCategory() {
		List<CategoryResponse> allCategory = cService.getActiveCategory();
		if (CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();
		} else {
			return CommonUtil.createBuildResponse(allCategory, HttpStatus.OK);
		//	return new ResponseEntity<>(allCategory, HttpStatus.OK);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getCategoryDetailsById(@PathVariable Long id) throws ResourceNotFoundException {
		CategoryDto categoryDto = cService.getCategoryById(id);
		if(ObjectUtils.isEmpty(categoryDto)) {
			return CommonUtil.createBuildResponse("Internal Server Error", HttpStatus.NO_CONTENT);
		}
		return CommonUtil.createBuildResponse(categoryDto, HttpStatus.OK);
		//return new ResponseEntity<>(categoryDto, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCategoryById(@PathVariable Long id) {
		Boolean deleted = cService.deleteCategory(id);
		if (deleted) {
			return CommonUtil.createBuildResponse("Deleted sucessfully", HttpStatus.OK);
			//return new ResponseEntity<>("Deleted sucessfully", HttpStatus.OK);
		} else {
			return CommonUtil.createErrorResponseMessage("Category not found", HttpStatus.INTERNAL_SERVER_ERROR);
			//return new ResponseEntity<>("Category not found with id=", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
