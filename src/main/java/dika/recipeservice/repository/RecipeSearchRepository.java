package dika.recipeservice.repository;


import dika.recipeservice.dto.RecipeElasticDto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RecipeSearchRepository extends ElasticsearchRepository<RecipeElasticDto, Long> {

    List<RecipeElasticDto> findByTitleContaining(String title);

    List<RecipeElasticDto> findByAuthorUsernameContaining(String username);

    List<RecipeElasticDto> findByIngredientsContaining(String ingredient);

    List<RecipeElasticDto> findByDescriptionContaining(String keyword);
}
