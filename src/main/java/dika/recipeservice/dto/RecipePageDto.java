package dika.recipeservice.dto;


import java.util.List;


public record RecipePageDto(
        List<RecipeDto> recipes,
        int currentPage,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {}
