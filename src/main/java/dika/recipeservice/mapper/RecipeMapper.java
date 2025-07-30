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

    //временно чисто для проверки работы всего остального сервиса
    @Mapping(target = "authorExternalId", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "authorUsername", defaultValue = "qwerty")
    Recipe toEntity(RecipeDto recipeDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorExternalId", expression = "java(java.util.UUID.randomUUID())")  //временно чисто для проверки работы всего остального сервиса
    @Mapping(target = "authorUsername", constant = "Anonymous")                    //временно чисто для проверки работы всего остального сервиса
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Recipe toEntityRecipeCreateDto(RecipeCreateDto recipeDto);

    RecipeElasticDto toRecipeElasticDto(Recipe recipe);

    RecipeDto toRecipeDto(RecipeElasticDto recipeElasticDto);

    @Mapping(target = "recipes", source = "content")
    RecipePageDto toRecipePageDto(Page<RecipeDto> page);
}
