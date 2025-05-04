package edu.cfd.e_learningPlatform.controller;
import edu.cfd.e_learningPlatform.dto.CategoryDto;
import edu.cfd.e_learningPlatform.service.CategoryService;
import edu.cfd.e_learningPlatform.service.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final S3Service s3Service;

    public CategoryController(CategoryService categoryService, S3Service s3Service) {
        this.categoryService = categoryService;
        this.s3Service = s3Service;
    }

    @GetMapping("/getCategorys")
    public ResponseEntity<List<CategoryDto>> getCategorys(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<CategoryDto> categories = categoryService.getAllCategories(page, size);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        CategoryDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    public ResponseEntity<CategoryDto> createCategory(
            @RequestParam("categoryName") String categoryName,
            @RequestParam(value = "coverImage", required = false) MultipartFile coverImage) throws IOException {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryName(categoryName);

        if (coverImage != null && !coverImage.isEmpty()) {
            String imageUrl = s3Service.uploadImage(coverImage);
            categoryDto.setCoverImage(imageUrl);
        }

        CategoryDto createdCategory = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long id,
            @RequestParam("categoryName") String categoryName,
            @RequestParam(value = "coverImage", required = false) MultipartFile coverImage) throws IOException {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryName(categoryName);

        if (coverImage != null && !coverImage.isEmpty()) {
            String imageUrl = s3Service.uploadImage(coverImage);
            categoryDto.setCoverImage(imageUrl);
        }

        CategoryDto updatedCategory = categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}