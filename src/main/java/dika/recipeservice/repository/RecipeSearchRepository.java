package dika.recipeservice.repository;


import dika.recipeservice.dto.RecipeElasticDto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RecipeSearchRepository extends ElasticsearchRepository<RecipeElasticDto, Long> {
}
