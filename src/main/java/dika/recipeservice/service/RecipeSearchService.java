package dika.recipeservice.service;


import dika.recipeservice.dto.RecipePageDto;


public interface RecipeSearchService {

    RecipePageDto fullTextSearch(String searchTerm, int page, int size);
}
