package com.edmond.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.edmond.dto.NotesDto;
import com.edmond.exception.ResourceNotFoundException;

public interface NotesService {

	public Boolean saveNotes(String notes,MultipartFile file) throws Exception;
	public List<NotesDto> getAllNotes();
}
