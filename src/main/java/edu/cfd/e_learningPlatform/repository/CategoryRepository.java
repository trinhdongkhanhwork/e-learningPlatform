package edu.cfd.e_learningPlatform.repository;

import java.util.List;

import edu.cfd.e_learningPlatform.dto.CourseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.cfd.e_learningPlatform.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(
            value = "select " + "cate.category_name as categoryName, "
                    + "cate.cover_image as coverImage, "
                    + "count(u.id) as numberUser "
                    + "from categories cate "
                    + "inner join courses cou on cou.category_id = cate.id "
                    + "inner join users u on u.id = cou.instructor_id "
                    + "group by cate.category_name, cate.cover_image "
                    + "order by numberUser desc ",
            nativeQuery = true)
    List<Object[]> finnCateNumberUser();
}
