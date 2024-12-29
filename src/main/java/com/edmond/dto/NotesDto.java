package com.edmond.dto;

import java.util.Date;

import com.edmond.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotesDto {

	private Long id;
	private String title;
	private String description;
	private CategoryDto category;
	private Integer createdBy;
	private Date createdOn;
	private Integer updatedBy;
	private Date updatedOn;
	private FilesDto fileDetails;
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	@Setter
	public static class FilesDto {
		private Long id;
		private String originalFileName;
		private String displayFileName;
	}
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	@Setter
	public static class CategoryDto {
		private Long id;
		private String name;
	}
}
