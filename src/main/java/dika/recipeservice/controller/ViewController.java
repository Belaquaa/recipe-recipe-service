package dika.recipeservice.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/recipes")
public class ViewController {

    @GetMapping("/create")
    public String showCreateForm() {
        return "recipeCreatePage";
    }
}
