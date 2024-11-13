package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.CategoryDto;
import edu.cfd.e_learningPlatform.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;

    }

    @GetMapping("/getCategorys")
    public ResponseEntity<List<CategoryDto>> getCategorys(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        List<CategoryDto> categories = categoryService.getAllCategories(page, size);
        return ResponseEntity.ok(categories);
    }

}
