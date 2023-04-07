package com.springboot.blogapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.blogapp.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

}
