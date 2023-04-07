package com.springboot.blogapp.payload;

import java.util.List;

import com.springboot.blogapp.model.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

	private Long id;
	private String name;
	private String description;

}
