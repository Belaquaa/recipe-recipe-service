package dika.recipeservice.mapper;


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
    Recipe toEntity(RecipeDto recipeDto);

    RecipeElasticDto toRecipeElasticDto(Recipe recipe);

    RecipeDto toRecipeDto(RecipeElasticDto recipeElasticDto);

    @Mapping(target = "recipes", source = "content")
    RecipePageDto toRecipePageDto(Page<RecipeDto> page);
}
