package com.edmond.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.edmond.dto.NotesDto;
import com.edmond.dto.NotesDto.CategoryDto;
import com.edmond.dto.NotesDto.FilesDto;
import com.edmond.dto.NotesResponse;
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
public class NotesServiceImpl implements NotesService {

	private final NotesRepository nRepo;
	private final ModelMapper mapper;
	private final CategoryRepository cRepo;
	private final FileRepository fRepo;

	@Value("${file.upload.path}")
	private String uploadPath;

	@Override
	public Boolean saveNotes(String notes, MultipartFile file) throws Exception {

		ObjectMapper ob = new ObjectMapper();
		NotesDto notesDto = ob.readValue(notes, NotesDto.class);
		
		notesDto.setIsDeleted(false);
		notesDto.setDeletedOn(null);

		if(!ObjectUtils.isEmpty(notesDto.getId())) {
			updateNotes(notesDto,file);
		}

		// category validation
		checkCategoryExists(notesDto.getCategory());

		Notes notesMap = mapper.map(notesDto, Notes.class);

		FileDetails fileDtls = saveFileDetails(file);
		if (!ObjectUtils.isEmpty(fileDtls)) {
			notesMap.setFileDetails(fileDtls);
		} else {
			if (ObjectUtils.isEmpty(notesDto.getId())) {
				notesMap.setFileDetails(null);
			}
			
		}

		Notes saveNotes = nRepo.save(notesMap);
		if (!ObjectUtils.isEmpty(saveNotes)) {
			return true;
		}
		return false;
	}

	private void updateNotes(NotesDto notesDto, MultipartFile file) throws Exception {
		Notes existNotes = nRepo.findById(notesDto.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Invalid notes id"));
		if(ObjectUtils.isEmpty(file)) {
			notesDto.setFileDetails(mapper.map(existNotes.getFileDetails(), FilesDto.class));
		}
	}

	private FileDetails saveFileDetails(MultipartFile file) throws IOException {
		if (!ObjectUtils.isEmpty(file) && !file.isEmpty()) {
			String originalFileName = file.getOriginalFilename();
			String extension = FilenameUtils.getExtension(originalFileName);
			List<String> allowedExtensions = Arrays.asList("pdf", "png", "jpg", "xlsx", "docx");
			if (!allowedExtensions.contains(extension)) {
				throw new IllegalArgumentException("invalid file format ! upload pdf , png , jpg , docx or xlsx");
			}

			String rndString = UUID.randomUUID().toString();

			String uploadfileName = rndString + "." + extension; // name.pdf

			File saveFile = new File(uploadPath);
			if (!saveFile.exists()) {
				saveFile.mkdir();
			}
			// path : enotesapiservice/notes/java.pdf
			String storePath = uploadPath.concat(uploadfileName);

			// upload file
			long uploadFile = Files.copy(file.getInputStream(), Paths.get(storePath));
			if (uploadFile != 0) {
				FileDetails fileDtls = new FileDetails();
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
		String extension = FilenameUtils.getExtension(originalFileName);
		String fileName = FilenameUtils.removeExtension(originalFileName);
		if (fileName.length() > 8) {
			fileName = fileName.substring(0, 7);
		}
		fileName = fileName + "." + extension;
		return fileName;
	}

	private void checkCategoryExists(CategoryDto category) throws Exception {
		cRepo.findById(category.getId()).orElseThrow(() -> new ResourceNotFoundException("inavlid category"));
	}

	@Override
	public List<NotesDto> getAllNotes() {
		return nRepo.findAll().stream().map(note -> mapper.map(note, NotesDto.class)).toList();
	}

	@Override
	public byte[] downloadFile(FileDetails fileDetails) throws Exception {
		File file = new File(fileDetails.getPath());

		// Ensure the file exists before reading
		if (!file.exists() || !file.isFile()) {
			throw new FileNotFoundException("File not found: " + fileDetails.getPath());
		}

		try (InputStream io = new FileInputStream(file)) {
			// Copy content of the file into a byte array
			return StreamUtils.copyToByteArray(io);
		} catch (IOException e) {
			// Handle IO exceptions
			throw new Exception("Error while reading the file: " + fileDetails.getPath(), e);
		}
	}

	@Override
	public FileDetails getFileDetails(Long id) throws Exception {
		FileDetails fileDtls = fRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("file not available"));
		return fileDtls;
	}

	@Override
	public NotesResponse getAllNotesByUser(Integer userId, Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Notes> pageNotes = nRepo.findByCreatedByAndIsDeletedFalse(userId, pageable);
		List<NotesDto> notesDto = pageNotes.get().map(n -> mapper.map(n, NotesDto.class)).toList();
		NotesResponse notesResponse = NotesResponse.builder().notes(notesDto).pageNo(pageNotes.getNumber())
				.pageSize(pageNotes.getSize()).totalElements(pageNotes.getTotalElements())
				.totalPages(pageNotes.getTotalPages()).isFirst(pageNotes.isFirst()).isLast(pageNotes.isLast()).build();
		return notesResponse;
	}

	@Override
	public void softDeleteNotes(Long id) throws Exception {
		Notes notes=nRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Invalid Notes id"));
		notes.setIsDeleted(true);
		notes.setDeletedOn(LocalDateTime.now());
		nRepo.save(notes);
		
	}

	@Override
	public void restoreNotes(Long id) throws Exception {
		Notes notes=nRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Invalid Notes id"));
		notes.setIsDeleted(false);
		notes.setDeletedOn(null);
		nRepo.save(notes);
		
	}

	@Override
	public List<NotesDto> getUserRecycleBinNotes(Integer userId) {
		List<Notes>recycleNotes=nRepo.findByCreatedByAndIsDeletedTrue(userId);
		List<NotesDto> notesDto=recycleNotes.stream().map(note->mapper.map(note, NotesDto.class)).toList();
		return notesDto;
	}

	@Override
	public void hardDeleteNotes(Long id) throws Exception {
		Notes notes=nRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Notes Not Found"));
		if(notes.getIsDeleted()) {
			nRepo.delete(notes);
		}else {
			throw new IllegalArgumentException("Sorry, You Can't Delete Note Directly");
		}
		
	}

	@Override
	public void emptyRecycleBin(Integer userId) {
		List<Notes> recycleNotes= nRepo.findByCreatedByAndIsDeletedTrue(userId);
		if(!CollectionUtils.isEmpty(recycleNotes)) {
			nRepo.deleteAll(recycleNotes);
		}
	}

}
