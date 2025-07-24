package dika.recipeservice.dto;


import dika.recipeservice.model.Recipe;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Document(indexName = "recipes")
public record RecipeElasticDto(
        @Id
        Long id,
        @Field(type = FieldType.Text, analyzer = "standard")
        String authorUsername,
        @Field(type = FieldType.Text, analyzer = "standard")
        String title,
        @Field(type = FieldType.Text, analyzer = "standard")
        String ingredients,
        @Field(type = FieldType.Text, analyzer = "standard")
        String description,
        @Field(type = FieldType.Text, analyzer = "standard")
        String fullText) {

    private static String buildFullText(Recipe recipe) {
        StringBuilder sb = new StringBuilder();
        if (recipe.getTitle() != null) sb.append(recipe.getTitle()).append(" ");
        if (recipe.getDescription() != null) sb.append(recipe.getDescription()).append(" ");
        if (recipe.getIngredients() != null) sb.append(recipe.getIngredients()).append(" ");
        return sb.toString().trim();
    }
}
