package edu.cfd.e_learningPlatform.service;



import edu.cfd.e_learningPlatform.dto.CategoryDto;
import edu.cfd.e_learningPlatform.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories(int page, int size);
    public long countCoursesByCategory(Long categoryId);
}
