package dika.recipeservice.service.impl;


import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import dika.recipeservice.dto.RecipeDto;
import dika.recipeservice.dto.RecipeElasticDto;
import dika.recipeservice.dto.RecipePageDto;
import dika.recipeservice.exception.SearchException;
import dika.recipeservice.mapper.RecipeMapper;
import dika.recipeservice.service.RecipeSearchService;
import dika.recipeservice.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeSearchServiceImpl implements RecipeSearchService {

    private final RecipeMapper recipeMapper;
    private final ElasticsearchTemplate elasticsearchTemplate;
    private final RecipeService recipeService;

    // поиск по полному тексту рецептов

    @Transactional(readOnly = true)
    @Override
    public RecipePageDto fullTextSearch(String searchTerm, int page, int size) {
        if (searchTerm.isEmpty()) {
            return recipeService.getAllRecipes(PageRequest.of(page, size));
        }
        try {
            // Создаем запрос MultiMatchQuery для поиска по нескольким полям
            // Используем fuzziness для обработки опечаток и ошибок ввода
            // Устанавливаем веса для полей, чтобы повысить релевантность результатов
            // Используем оператор OR для объединения условий поиска
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

            // Выполняем поиск в Elasticsearch с помощью созданного запроса
            SearchHits<RecipeElasticDto> searchHits = elasticsearchTemplate
                    .search(searchQuery, RecipeElasticDto.class);

            List<RecipeDto> recipes = searchHits.getSearchHits()
                    .stream()
                    .map(SearchHit::getContent)
                    .map(recipeMapper::toRecipeDto)
                    .collect(Collectors.toList());

            return new RecipePageDto(recipes,
                    page,
                    size,
                    searchHits.getTotalHits(),
                    calculateTotalPages(searchHits.getTotalHits(), size),
                    page < calculateTotalPages(searchHits.getTotalHits(), size) - 1,
                    page > 0);
        } catch (Exception e) {
            throw new SearchException("Error during full-text search");
        }
    }

    private int calculateTotalPages(long totalRecipes, int pageSize) {
        return (int) Math.ceil((double) totalRecipes / pageSize);
    }
}
