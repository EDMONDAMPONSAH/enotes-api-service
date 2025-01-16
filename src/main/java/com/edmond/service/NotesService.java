package com.edmond.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.edmond.dto.NotesDto;
import com.edmond.dto.NotesResponse;
import com.edmond.entity.FileDetails;
import com.edmond.exception.ResourceNotFoundException;

public interface NotesService {

	public Boolean saveNotes(String notes,MultipartFile file) throws Exception;
	public List<NotesDto> getAllNotes();
	public byte[] downloadFile(FileDetails fileDetails) throws Exception;
	public FileDetails getFileDetails(Long id) throws Exception;
	public NotesResponse getAllNotesByUser(Integer userId,Integer pageNo,Integer pageSize);
}
