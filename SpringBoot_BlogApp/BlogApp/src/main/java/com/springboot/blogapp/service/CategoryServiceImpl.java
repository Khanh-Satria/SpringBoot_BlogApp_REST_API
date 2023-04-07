package com.springboot.blogapp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.springboot.blogapp.exception.ResourceNotFoundException;
import com.springboot.blogapp.model.Category;
import com.springboot.blogapp.payload.CategoryDto;
import com.springboot.blogapp.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService{

	
	private CategoryRepository categoryRepository;
	
	private ModelMapper mapper;
	
	
	public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper mapper) {
		super();
		this.categoryRepository = categoryRepository;
		this.mapper = mapper;
	}



	@Override
	public CategoryDto addCategory(CategoryDto categoryDto){
		
		Category category = mapToEntity(categoryDto);
		Category categoryNew = categoryRepository.save(category);
		return mapToDTO(categoryNew);
		
	}
	
	
	@Override
	public CategoryDto getCategory(Long categoryId) {
		
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
		
		return  mapToDTO(category);
		
	
	}
	
	
	@Override
	public List<CategoryDto> getAllCategories(){
		
		List<Category> categories = categoryRepository.findAll();	
		
		return categories.stream()
				.map((category) -> mapToDTO(category))
				.collect(Collectors.toList());
	}
	
	
	
	@Override
	public CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto) {
		
		Category category = categoryRepository.findById(categoryId)
												.orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
		
		category.setName(categoryDto.getName());
		category.setDescription(categoryDto.getDescription());
		Category categoryUpdate =  categoryRepository.save(category);
		
		return mapToDTO(categoryUpdate);
		
	}
	
	
	@Override
	public void deleteCategory(Long categoryId) {
		
		Category category = categoryRepository.findById(categoryId)
												.orElseThrow(() -> new ResourceNotFoundException("category", "id", categoryId));
		
		
		categoryRepository.delete(category);
	}
	
	private Category mapToEntity(CategoryDto categoryDto) {
		
		Category category = mapper.map(categoryDto, Category.class);
		return category;
	}
	
	private CategoryDto mapToDTO(Category category) {
		
		CategoryDto categoryDto = mapper.map(category, CategoryDto.class);
		return categoryDto;
	}
	
}
