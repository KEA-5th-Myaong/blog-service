package myaong.popolog.blogservice.service;

import myaong.popolog.blogservice.dto.request.PostCreateRequest;
import myaong.popolog.blogservice.dto.request.ReportRequest;
import myaong.popolog.blogservice.dto.response.*;
import org.springframework.web.multipart.MultipartFile;

public interface PostsService {
	PostDetailResponse getPostDetails(Long postId, Long memberId);
	PostDetailResponse getPostByUrl(String username, String title, Long memberId);
	PostsResponse getPostsOf(Long requesterId, Long memberId, Long lastId);
	LikeResponse toggleLike(Long postId, Long memberId);
	PostCreateResponse createPost(Long memberId, PostCreateRequest request);
	void updatePost(Long postId, Long memberId, PostCreateRequest request);
	void deletePost(Long postId, Long memberId);
	PostPicResponse uploadPostImage(Long memberId, MultipartFile image);
	PostBookmarkResponse toggleBookmark(Long memberId, Long postId);
	void reportPost(Long memberId, ReportRequest request);
}

