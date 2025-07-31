package dika.recipeservice.controller;


import dika.recipeservice.dto.RecipeCreateDto;
import dika.recipeservice.dto.RecipeDto;
import dika.recipeservice.dto.RecipePageDto;
import dika.recipeservice.service.RecipeSearchService;
import dika.recipeservice.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipeSearchService recipeSearchService;

    @GetMapping
    public ResponseEntity<RecipePageDto> getAllRecipe(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(recipeService.getAllRecipes(PageRequest.of(page, size)));
    }

    @PostMapping
    public ResponseEntity<RecipeDto> createRecipe(@Valid @RequestBody RecipeCreateDto recipeDto) {
        return ResponseEntity.ok(recipeService.createRecipe(recipeDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDto> getRecipe(@PathVariable Long id) {
        return ResponseEntity.ok(recipeService.getRecipe(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeDto> updateRecipe( @PathVariable Long id, @Valid @RequestBody RecipeDto recipeDto) {
        return ResponseEntity.ok(recipeService.update(id, recipeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<RecipePageDto> searchRecipes(
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(recipeSearchService.fullTextSearch(query, page, size));
    }
}
