package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories(int page, int size);

    public long countCoursesByCategory(Long categoryId);

    // CURD

    CategoryDto getCategoryById(Long id);

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(Long id ,CategoryDto categoryDto);

    void deleteCategory(Long id);
}
