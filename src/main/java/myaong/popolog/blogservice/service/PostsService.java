package myaong.popolog.blogservice.service;
import myaong.popolog.blogservice.dto.response.LikeResponse;
import myaong.popolog.blogservice.dto.response.PostDetailResponse;
import myaong.popolog.blogservice.dto.response.PostsResponse;
import org.springframework.stereotype.Service;

@Service
public interface PostsService {
	PostDetailResponse getPostDetails(Long postId, Long memberId);
	PostDetailResponse getPostByUrl(String username, String title, Long memberId);
	PostsResponse getPostsOf(Long memberId, Long lastId);
	LikeResponse toggleLike(Long postId, Long memberId);
}

