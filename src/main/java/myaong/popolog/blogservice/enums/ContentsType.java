package myaong.popolog.blogservice.enums;

public enum ContentsType {

	POST, COMMENT;

	public static ContentsType valueOfLower(String name) {
		return ContentsType.valueOf(name.toUpperCase());
	}
}
