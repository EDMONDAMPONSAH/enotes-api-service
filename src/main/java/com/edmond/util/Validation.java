package com.edmond.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.edmond.dto.CategoryDto;
import com.edmond.exception.ValidationException;

@Component
public class Validation {

	public void categoryValidation(CategoryDto categoryDto) {
		Map<String, Object> error = new LinkedHashMap<>();
		if (ObjectUtils.isEmpty(categoryDto)) {
			throw new IllegalArgumentException("category object should not be null or empty");
		} else {
			// name field validation
			if (ObjectUtils.isEmpty(categoryDto.getName())) {
				error.put("name", "name field is empty");
			} else {
				if (categoryDto.getName().length() < 10) {
					error.put("name", "name length min 10");
				}
				if (categoryDto.getName().length() > 100) {
					error.put("name", "name length max 100");
				}
			}
			// description validation
			if (ObjectUtils.isEmpty(categoryDto.getDescription())) {
				error.put("description", "description field is empty");
			}
			// isActive validation
			if (ObjectUtils.isEmpty(categoryDto.getIsActive())) {
				error.put("isActive", "isActive field is null");
			} else {
				if (categoryDto.getIsActive() != Boolean.TRUE.booleanValue() && categoryDto.getIsActive() != Boolean.FALSE.booleanValue()) {
					error.put("isActive", "invalid value isActive field");
				}
			}
		}
		if(!error.isEmpty()) {
			throw new ValidationException(error);
		}
	}
}
