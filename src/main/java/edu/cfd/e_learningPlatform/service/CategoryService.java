package edu.cfd.e_learningPlatform.service;

import java.util.List;

import edu.cfd.e_learningPlatform.dto.CategoryDto;

public interface CategoryService {
    List<CategoryDto> getAllCategories(int page, int size);

    public long countCoursesByCategory(Long categoryId);

    // CURD

    CategoryDto getCategoryById(Long id);

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(Long id ,CategoryDto categoryDto);

    void deleteCategory(Long id);
}
