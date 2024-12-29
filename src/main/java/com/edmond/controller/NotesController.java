package com.edmond.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.edmond.dto.NotesDto;
import com.edmond.service.NotesService;
import com.edmond.util.CommonUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/notes")
@RequiredArgsConstructor
public class NotesController {

	private final NotesService nService;

	@PostMapping("/")
	public ResponseEntity<?> saveNotes(@RequestParam String notes,@RequestParam(required=false) MultipartFile file) throws Exception {
		Boolean saveNotes = nService.saveNotes(notes,file);
		if (saveNotes) {
			return CommonUtil.createBuildResponseMessage("Notes saved successfully", HttpStatus.CREATED);
		}
		return CommonUtil.createErrorResponseMessage("Failed to save", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/")
	public ResponseEntity<?> getAllNotes() {
		List<NotesDto> notes = nService.getAllNotes();
		if (CollectionUtils.isEmpty(notes)) {
			return ResponseEntity.noContent().build();
		}
		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}

}
