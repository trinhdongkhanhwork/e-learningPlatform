package edu.cfd.e_learningPlatform.mapstruct;
import edu.cfd.e_learningPlatform.dto.CategoryDto;
import edu.cfd.e_learningPlatform.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category CategoryDtoToCategory(CategoryDto category);

    @Mapping(source = "categoryName", target = "categoryName")
    @Mapping(source = "coverImage", target = "coverImage")
    CategoryDto CategoryToCategoryDto(Category category);


    List<CategoryDto> ListCategoryToCategoryDtoList(List<Category> categoryList);
}
