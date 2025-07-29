package dika.recipeservice.dto;


import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;


@Builder

public record RecipePageDto(
        List<RecipeDto> recipes,
        int currentPage,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {

    public static RecipePageDto from(Page<RecipeDto> page) {
        return RecipePageDto.builder()
                .recipes(page.getContent())
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }

    public static RecipePageDto empty() {
        return RecipePageDto.builder()
                .recipes(List.of())
                .currentPage(0)
                .pageSize(0)
                .totalPages(0)
                .hasNext(false)
                .hasPrevious(false)
                .build();
    }
}
