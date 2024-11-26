package myaong.popolog.blogservice.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Prefix {

	BLOG("blog"),
	PROFILE("profile"),
	;

	private final String name;

	@Override
	public String toString() {
		return name;
	}
}
