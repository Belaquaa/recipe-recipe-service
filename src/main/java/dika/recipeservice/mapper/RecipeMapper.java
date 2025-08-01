package dika.recipeservice.mapper;


import dika.recipeservice.dto.RecipeCreateDto;
import dika.recipeservice.dto.RecipeDto;
import dika.recipeservice.dto.RecipeElasticDto;
import dika.recipeservice.dto.RecipePageDto;
import dika.recipeservice.model.Recipe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;


@Mapper(componentModel = "spring", imports = {java.util.UUID.class})
public interface RecipeMapper {

    RecipeDto toDto(Recipe recipe);

    //TODO:временно чисто для проверки работы всего остального сервиса
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "authorExternalId", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "authorUsername", defaultValue = "qwerty")
    Recipe toEntity(RecipeDto recipeDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorExternalId", expression = "java(java.util.UUID.randomUUID())")
    //TODO:временно чисто для проверки работы всего остального сервиса
    @Mapping(target = "authorUsername", constant = "Anonymous")
    //TODO:временно чисто для проверки работы всего остального сервиса
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Recipe toEntityRecipeCreateDto(RecipeCreateDto recipeDto);

    RecipeElasticDto toRecipeElasticDto(Recipe recipe);

    @Mapping(target = "instructions", ignore = true)
    @Mapping(target = "difficulty", ignore = true)
    @Mapping(target = "prepTime", ignore = true)
    @Mapping(target = "cookTime", ignore = true)
    @Mapping(target = "servings", ignore = true)
    @Mapping(target = "status", ignore = true)
    RecipeDto toRecipeDto(RecipeElasticDto recipeElasticDto);

    @Mapping(target = "recipes", source = "content")
    @Mapping(target = "currentPage", ignore = true)
    @Mapping(target = "pageSize", ignore = true)
    @Mapping(target = "hasNext", ignore = true)
    @Mapping(target = "hasPrevious", ignore = true)
    @Mapping(target = "totalElements", ignore = true)
    @Mapping(target = "totalPages", ignore = true)
    RecipePageDto toRecipePageDto(Page<RecipeDto> page);
}
