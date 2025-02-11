package com.edmond.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.edmond.dto.NotesDto;
import com.edmond.dto.NotesResponse;
import com.edmond.entity.FileDetails;
import com.edmond.service.NotesService;
import com.edmond.util.CommonUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/notes")
@RequiredArgsConstructor
public class NotesController {

	private final NotesService nService;

	@PostMapping("/")
	public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file)
			throws Exception {
		Boolean saveNotes = nService.saveNotes(notes, file);
		if (saveNotes) {
			return CommonUtil.createBuildResponseMessage("Notes saved successfully", HttpStatus.CREATED);
		}
		return CommonUtil.createErrorResponseMessage("Failed to save", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/download/{id}")
	public ResponseEntity<?> downloadFile(@PathVariable Long id) throws Exception {
	    FileDetails fileDetails = nService.getFileDetails(id);
	    
	    // Check if file details are valid
	    if (fileDetails == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                             .body("File not found with id: " + id);
	    }

	    byte[] data = nService.downloadFile(fileDetails);  // Ensure the downloadFile method works correctly
	    
	    // Set headers for file download
	    HttpHeaders headers = new HttpHeaders();
	    String contentType = CommonUtil.getContentType(fileDetails.getOriginalFileName());
	    headers.setContentType(MediaType.parseMediaType(contentType));
	    headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());

	    // Return the file data with proper headers for download
	    return ResponseEntity.ok()
	                         .headers(headers)
	                         .body(data);
	}


	@GetMapping("/")
	public ResponseEntity<?> getAllNotes() {
		List<NotesDto> notes = nService.getAllNotes();
		if (CollectionUtils.isEmpty(notes)) {
			return ResponseEntity.noContent().build();
		}
		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}
	
	@GetMapping("/user-notes")
	public ResponseEntity<?> getAllNotesByUser(@RequestParam(name="pageNo",defaultValue="0")Integer pageNo,
			@RequestParam(name="pageSize",defaultValue="2")Integer pageSize) {
		Integer userId=2;
		NotesResponse notes = nService.getAllNotesByUser(userId,pageNo,pageSize);
		/*
		 * if (CollectionUtils.isEmpty(notes)) { return
		 * ResponseEntity.noContent().build(); }
		 */
		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}
	
	@GetMapping("/delete/{id}")
	public ResponseEntity<?> deleteNotes(@PathVariable Long id) throws Exception{
		nService.softDeleteNotes(id);
		return CommonUtil.createBuildResponseMessage("Deleted Successfully", HttpStatus.OK);
	}

	@GetMapping("/restore/{id}")
	public ResponseEntity<?> restoreNotes(@PathVariable Long id) throws Exception{
		nService.restoreNotes(id);
		return CommonUtil.createBuildResponseMessage("Notes Restored Successfully", HttpStatus.OK);
	}
	
	@GetMapping("/recycleBin")
	public ResponseEntity<?> getuserRecycleBinNotes() throws Exception{
		Integer userId=2;
		List<NotesDto>notes=nService.getUserRecycleBinNotes(userId);
		if(CollectionUtils.isEmpty(notes)) {
			return CommonUtil.createBuildResponseMessage("Notes not available in recycle bin", HttpStatus.OK);
		}
		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> hardDeleteNotes(@PathVariable Long id) throws Exception{
		nService.hardDeleteNotes(id);
		return CommonUtil.createBuildResponseMessage("Deleted Successfully", HttpStatus.OK);
	}
	
	@GetMapping("/emptyRecycleBin")
	public ResponseEntity<?> emptyRecycleBin() throws Exception{
		Integer userId=2;
		nService.emptyRecycleBin(userId);
		return CommonUtil.createBuildResponseMessage("Emptied Recycle Bin Successfully", HttpStatus.OK);
	}
}
