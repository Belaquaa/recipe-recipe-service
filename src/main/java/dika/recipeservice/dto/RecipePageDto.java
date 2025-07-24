package dika.recipeservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipePageDto {
    private List<RecipeDto> recipes;
    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;

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
