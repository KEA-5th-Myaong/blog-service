package myaong.popolog.blogservice.service;

import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.entity.Profile;
import myaong.popolog.blogservice.repository.BookmarkRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {

	private final BookmarkRepository bookmarkRepository;

	public Boolean existsByProfileAndBookmarkId(Long bookmarkId, Profile profile) {
		return bookmarkRepository.existsByIdAndProfile(bookmarkId, profile);
	}
}
