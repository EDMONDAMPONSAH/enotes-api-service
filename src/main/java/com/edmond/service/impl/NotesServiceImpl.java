package com.edmond.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.edmond.dto.NotesDto;
import com.edmond.dto.NotesDto.CategoryDto;
import com.edmond.entity.FileDetails;
import com.edmond.entity.Notes;
import com.edmond.exception.ResourceNotFoundException;
import com.edmond.repository.CategoryRepository;
import com.edmond.repository.FileRepository;
import com.edmond.repository.NotesRepository;
import com.edmond.service.NotesService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotesServiceImpl implements NotesService{
	
	private final NotesRepository nRepo;
	private final ModelMapper mapper;
	private final CategoryRepository cRepo;
	private final FileRepository fRepo;
	
	@Value("${file.upload.path}")
	private String uploadPath;

	@Override
	public Boolean saveNotes(String notes,MultipartFile file) throws Exception {
		
		
		ObjectMapper ob = new ObjectMapper();
		NotesDto notesDto=ob.readValue(notes, NotesDto.class);
		
		// category validation 
		checkCategoryExists(notesDto.getCategory());
		
		Notes notesMap=mapper.map(notesDto, Notes.class);
		
		FileDetails fileDtls=saveFileDetails(file);
		if(!ObjectUtils.isEmpty(fileDtls)) {
			notesMap.setFileDetails(fileDtls);
		}else {
			notesMap.setFileDetails(null);
		}
		
		Notes saveNotes = nRepo.save(notesMap);
		if(!ObjectUtils.isEmpty(saveNotes)) {
			return true;
		}
		return false;
	}

	private FileDetails saveFileDetails(MultipartFile file) throws IOException {
		if(!ObjectUtils.isEmpty(file) && !file.isEmpty()) {
			String originalFileName=file.getOriginalFilename();
			String extension=FilenameUtils.getExtension(originalFileName);
			List<String> allowedExtensions = Arrays.asList("pdf", "png", "jpg", "xlsx");
			if(!allowedExtensions.contains(extension)) {
				throw new IllegalArgumentException("invalid file format ! upload pdf , png , jpg , or xlsx");
			}
			
			
			
			
			
			String rndString=UUID.randomUUID().toString();
			
			String uploadfileName=rndString+"."+extension; //name.pdf
			
			
			
			File saveFile=new File(uploadPath);
			if(!saveFile.exists()) {
				saveFile.mkdir();
			}
			//path : enotesapiservice/notes/java.pdf
			String storePath=uploadPath.concat(uploadfileName);
			
			
			
			// upload file
			long uploadFile=Files.copy(file.getInputStream(), Paths.get(storePath));
			if(uploadFile!=0) {
				FileDetails fileDtls=new FileDetails();
				fileDtls.setOriginalFileName(originalFileName);
				fileDtls.setDisplayFileName(getDisplayName(originalFileName));
				fileDtls.setUploadFileName(uploadfileName);
				fileDtls.setPath(storePath);
				fileDtls.setFileSize(file.getSize());
				FileDetails saveFileDetails = fRepo.save(fileDtls);
				return saveFileDetails;
			}
		}
		return null;
	}

	private String getDisplayName(String originalFileName) {
		// java_programming_tutorials.pdf
		// java_prog.pdf
		String extension=FilenameUtils.getExtension(originalFileName);
		String fileName=FilenameUtils.removeExtension(originalFileName);
		if(fileName.length()>8) {
			fileName=fileName.substring(0,7);
		}
		fileName=fileName+"."+extension;
		return fileName;
	}

	private void checkCategoryExists(CategoryDto category) throws Exception {
		cRepo.findById(category.getId()).orElseThrow(()->new ResourceNotFoundException("inavlid category"));
	}

	@Override
	public List<NotesDto> getAllNotes() {
		return nRepo.findAll().stream().map(note->mapper.map(note,NotesDto.class)).toList();
	}
	

}
