package com.edmond.schedular;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.edmond.entity.Notes;
import com.edmond.repository.NotesRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotesSchedular {
	
	private final NotesRepository nRepo;

	@Scheduled(cron="0 0 0 * * ?")
	public void deleteNotesSchedular() {
		LocalDateTime cutOffDate = LocalDateTime.now().minusDays(7);
		List<Notes> deleteNotes=nRepo.findAllByIsDeletedAndDeletedOnBefore(true,cutOffDate);
		nRepo.deleteAll(deleteNotes);
	}
	
}
