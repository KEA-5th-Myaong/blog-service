package myaong.popolog.blogservice.service;

import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.Prefix;
import myaong.popolog.blogservice.common.exception.ApiCode;
import myaong.popolog.blogservice.common.exception.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ApiService {

	private final String TEMP_BUCKET_NAME = "temp-storage";
	private final String PERSISTENT_BUCKET_NAME = "persistent-storage";
	private final S3Client s3Client;

	/**
	 * URL에서 Object Storage key를 추출합니다.
	 *
	 * @param url 파일 주소
	 * @return key
	 */
	private String getKeyFrom(String url, String bucketName) {
		return url.substring(url.lastIndexOf(bucketName) + bucketName.length() + 1);
	}

	/**
	 * 콘텐츠가 DB에 저장되기 전이라면, 파일을 임시로 저장합니다.
	 * 임시 저장소에 저장된 파일은 24시간 후 자동으로 삭제됩니다.
	 * <br>
	 * prefix의 일관성 유지를 위해 Enum 객체를 받습니다. 필요 시 Prefix에 값을 추가하여 사용할 수 있습니다.
	 *
	 * @return URL of uploaded file
	 */
	public String uploadToTempStorage(Prefix prefix, MultipartFile file) {
		return uploadToStorage(TEMP_BUCKET_NAME, prefix.toString(), file);
	}

	/**
	 * 파일을 즉시 영구 저장소에 저장합니다.
	 * <br>
	 * prefix의 일관성 유지를 위해 Enum 객체를 받습니다. 필요 시 Prefix에 값을 추가하여 사용할 수 있습니다.
	 *
	 * @return URL of uploaded file
	 */
	public String uploadToPersistentStorage(Prefix prefix, MultipartFile file) {
		return uploadToStorage(PERSISTENT_BUCKET_NAME, prefix.toString(), file);
	}

	/**
	 * <strong>prefix의 일관성 유지를 위해 uploadToTempStorage(Prefix storageName, MultipartFile file) 사용이 권장됩니다.</strong>
	 *
	 * @param keyPrefix e.g., "your/prefix"
	 * @return URL of uploaded file
	 */
	public String uploadToStorage(String bucketName, String keyPrefix, MultipartFile file) {

		// 확장자 추출
		String originalFilename = file.getOriginalFilename();
		if (originalFilename == null) {
			throw new ApiException(ApiCode.INVALID_DATA);
		}
		String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

		// 접근 경로 생성
		String fileName = UUID.randomUUID() + extension;
		String key = keyPrefix + "/" + fileName;

		return uploadObject(bucketName, key, file);
	}

	/**
	 * 영구 저장소에서 파일을 삭제합니다.
	 * @param url 파일 주소
	 */
	public void deleteFromPersistentStorage(String url) {
		String key = getKeyFrom(url, PERSISTENT_BUCKET_NAME);
		deleteObject(PERSISTENT_BUCKET_NAME, key);
	}

	/**
	 * 콘텐츠를 수정 시 기존 파일들을 임시 저장소로 옮겨,
	 * 삭제된 파일들은 24시간 후 자동으로 삭제되도록 합니다.
	 *
	 * @param url 파일 주소
	 * @return URL of copied file to target bucket
	 */
	public String moveToTempStorage(String url) {
		String key = getKeyFrom(url, PERSISTENT_BUCKET_NAME);
		return moveObject(PERSISTENT_BUCKET_NAME, key, TEMP_BUCKET_NAME, key);
	}

	/**
	 * 콘텐츠가 DB에 저장될 것이 확정되면, 파일을 영구 저장소로 복제합니다.
	 *
	 * @param url 파일 주소
	 * @return URL of copied file to target bucket
	 */
	public String moveToPersistentStorage(String url) {

		String key = getKeyFrom(url, TEMP_BUCKET_NAME);

		// prefix 및 확장자 추출
		String prefix = key.substring(0, key.lastIndexOf("/"));
		String extension = key.substring(key.lastIndexOf("."));

		// 접근 경로 생성
		// UUID 중복 문제가 발생할 경우, 재시도 시엔 성공할 수 있도록 매번 새로 생성하도록 했음
		String fileName = UUID.randomUUID() + extension;
		String targetKey = prefix + "/" + fileName;

		return moveObject(TEMP_BUCKET_NAME, key, PERSISTENT_BUCKET_NAME, targetKey);
	}

	/**
	 * 파일 저장
	 *
	 * @param bucketName e.g., "storage-name"
	 * @param key e.g., "prefix/file.png"
	 * @return URL of uploaded file
	 */
	private String uploadObject(String bucketName, String key, MultipartFile file) {

		// PutObjectRequest 생성
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucketName)
				.key(key)
				.contentType(file.getContentType())
				.build();

		// 파일 업로드
		try {
			s3Client.putObject(putObjectRequest,
					RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
		} catch (IOException e) {
			throw new ApiException(ApiCode.INTERNAL_SERVER_ERROR);
		}

		return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(key).build()).toString();
	}

	/**
	 * 파일 이동. 기존 저장소의 파일은 삭제
	 *
	 * @param sourceBucketName e.g., "storage-name"
	 * @param sourceKey e.g., "prefix/file.png"
	 * @param targetBucketName e.g., "storage-name"
	 * @param targetKey e.g., "prefix/file.png"
	 * @return URL of moved file to target bucket
	 */
	private String moveObject(String sourceBucketName, String sourceKey, String targetBucketName, String targetKey) {

		String url = copyObject(sourceBucketName, sourceKey, targetBucketName, targetKey);

		// 기존 파일 삭제
		deleteObject(sourceBucketName, sourceKey);

		return url;
	}

	/**
	 * 파일 복제
	 *
	 * @param sourceBucketName e.g., "storage-name"
	 * @param sourceKey e.g., "prefix/file.png"
	 * @param targetBucketName e.g., "storage-name"
	 * @param targetKey e.g., "prefix/file.png"
	 * @return URL of copied file to target bucket
	 */
	private String copyObject(String sourceBucketName, String sourceKey, String targetBucketName, String targetKey) {

		CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
				.sourceBucket(sourceBucketName)
				.sourceKey(sourceKey)
				.destinationBucket(targetBucketName)
				.destinationKey(targetKey)
				.build();

		s3Client.copyObject(copyObjectRequest);

		return s3Client.utilities().getUrl(builder -> builder.bucket(targetBucketName).key(targetKey).build()).toString();
	}

	/**
	 * 파일 삭제
	 *
	 * @param bucketName e.g., "storage-name"
	 * @param key e.g., "prefix/file.png"
	 */
	private void deleteObject(String bucketName, String key) {

		DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
				.bucket(bucketName)
				.key(key)
				.build();

		s3Client.deleteObject(deleteObjectRequest);
	}
}
