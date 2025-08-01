package dika.recipeservice.service;


import dika.recipeservice.dto.RecipeCreateDto;


public interface RabbitService {

    void sendCreate(RecipeCreateDto user);
}
