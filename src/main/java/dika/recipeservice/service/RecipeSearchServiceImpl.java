package dika.recipeservice.service;


import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import dika.recipeservice.RecipeMapper;
import dika.recipeservice.RecipeMapperImpl;
import dika.recipeservice.dto.RecipeDto;
import dika.recipeservice.dto.RecipeElasticDto;
import dika.recipeservice.dto.RecipePageDto;
import dika.recipeservice.model.Recipe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeSearchServiceImpl {

    private final RecipeMapper recipeMapper;
    private final ElasticsearchTemplate elasticsearchTemplate;
    private final RecipeMapperImpl recipeMapperImpl;

    public RecipePageDto fullTextSearch(String searchTerm, int page, int size) {
        try {
            MultiMatchQuery multiMatchQuery = MultiMatchQuery.of(m -> m
                    .query(searchTerm)
                    .fields("title^3.0", "authorUsername^3.0", "description^2.0", "ingredients^2.5")
                    .fuzziness("AUTO")
                    .operator(Operator.Or)
            );

            NativeQuery searchQuery = NativeQuery.builder()
                    .withQuery(Query.of(q -> q.multiMatch(multiMatchQuery)))
                    .withPageable(PageRequest.of(page, size))
                    .build();

            SearchHits<RecipeElasticDto> searchHits = elasticsearchTemplate
                    .search(searchQuery, RecipeElasticDto.class);

            List<RecipeDto> recipes = searchHits.getSearchHits()
                    .stream()
                    .map(SearchHit::getContent)
                    .map(recipeMapper::toRecipeDto)
                    .collect(Collectors.toList());

            return RecipePageDto.builder()
                    .recipes(recipes)
                    .currentPage(page)
                    .pageSize(size)
                    .totalPages(calculateTotalPages(searchHits.getTotalHits(), size))
                    .hasNext(page < calculateTotalPages(searchHits.getTotalHits(), size) - 1)
                    .hasPrevious(page > 0)
                    .build();
        } catch (Exception e) {
            log.error("Ошибка при выполнении поиска: {}", e.getMessage(), e);
            return RecipePageDto.empty();
        }
    }

    private int calculateTotalPages(long totalRecipes, int pageSize) {
        return (int) Math.ceil((double) totalRecipes / pageSize);
    }
}
