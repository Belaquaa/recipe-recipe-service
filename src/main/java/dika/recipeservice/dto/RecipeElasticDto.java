package dika.recipeservice.dto;


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
        String description) {}

