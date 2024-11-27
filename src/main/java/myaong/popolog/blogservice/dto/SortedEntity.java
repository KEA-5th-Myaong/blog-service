package myaong.popolog.blogservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SortedEntity<T> {

	private T entity;
	private Long key;
}
