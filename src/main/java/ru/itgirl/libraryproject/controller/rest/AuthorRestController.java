package ru.itgirl.libraryproject.controller.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.itgirl.libraryproject.dto.AuthorCreateDto;
import ru.itgirl.libraryproject.dto.AuthorDto;
import ru.itgirl.libraryproject.dto.AuthorUpdateDto;
import ru.itgirl.libraryproject.service.AuthorService;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "library-users")
public class AuthorRestController {

    private final AuthorService authorService;

    @GetMapping("/author/{id}")
    AuthorDto getAuthorById(@PathVariable("id") Long id) {
        return authorService.getAuthorById(id);
    }


    @GetMapping("/author") //V1
    AuthorDto getAuthorByName(@RequestParam("name") String name){
        return authorService.getByNameV1(name);
    }
    @GetMapping("/author/sql") //V2
    AuthorDto getAuthorByNameSQL(@RequestParam("name") String name) {return authorService.getByNameSQL(name);}
    @GetMapping("/author/criteria") //V3
    AuthorDto getAuthorByCriteria(@RequestParam("name") String name) {
        return authorService.getByCriteria(name);
    }


    @PostMapping("/author/create")
    AuthorDto createAuthor(@RequestBody @Valid AuthorCreateDto authorCreateDto) {
        return authorService.createAuthor(authorCreateDto);
    }
    @PutMapping("/author/update")
    AuthorDto updateAuthor(@RequestBody @Valid AuthorUpdateDto authorUpdateDto) {
        return authorService.updateAuthor(authorUpdateDto);
    }
    @DeleteMapping("/author/delete/{id}")
    String deleteAuthor(@PathVariable("id") Long id) {
       return authorService.deleteAuthor(id);

    }

}