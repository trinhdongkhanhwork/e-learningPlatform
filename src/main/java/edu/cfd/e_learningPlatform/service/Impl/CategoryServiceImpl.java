package edu.cfd.e_learningPlatform.service.Impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.cfd.e_learningPlatform.dto.CategoryDto;
import edu.cfd.e_learningPlatform.entity.Category;
import edu.cfd.e_learningPlatform.mapstruct.CategoryMapper;
import edu.cfd.e_learningPlatform.repository.CategoryRepository;
import edu.cfd.e_learningPlatform.repository.CourseRepository;
import edu.cfd.e_learningPlatform.service.CategoryService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private CategoryMapper categoryMapper;
    private CourseRepository courseRepository;

    @Override
    public List<CategoryDto> getAllCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size); // Sử dụng phân trang (Pageable)
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        // Chuyển đổi danh sách từ Page<Category> sang List<CategoryDto>
        return categoryMapper.ListCategoryToCategoryDtoList(categoryPage.getContent());
    }

    // Thêm phương thức đếm khóa học thuộc một thể loại
    public long countCoursesByCategory(Long categoryId) {
        return courseRepository.countByCategoryId(categoryId); // Tính số lượng khóa học thuộc categoryId
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy category với ID: " + id));
        return categoryMapper.CategoryToCategoryDto(category);
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.CategoryDtoToCategory(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.CategoryToCategoryDto(savedCategory);
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy category với ID: " + id));
        existingCategory.setCategoryName(categoryDto.getCategoryName());
        if (categoryDto.getCoverImage() != null) {
            existingCategory.setCoverImage(categoryDto.getCoverImage());
        }
        Category updatedCategory = categoryRepository.save(existingCategory);
        return categoryMapper.CategoryToCategoryDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy category với ID: " + id));
        categoryRepository.delete(category);
    }

}
