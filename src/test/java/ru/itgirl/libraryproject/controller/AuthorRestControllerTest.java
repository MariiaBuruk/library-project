package ru.itgirl.libraryproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.itgirl.libraryproject.dto.AuthorCreateDto;
import ru.itgirl.libraryproject.dto.AuthorDto;
import ru.itgirl.libraryproject.dto.AuthorUpdateDto;
import ru.itgirl.libraryproject.model.Author;
import ru.itgirl.libraryproject.repository.AuthorRepository;
import ru.itgirl.libraryproject.service.AuthorService;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@Rollback
public class AuthorRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private AuthorDto authorDto;
    @Mock
    AuthorRepository authorRepository;

    @Test
    public void testGetAuthorById() throws Exception {
        Long authorId = 1L;
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(authorId);
        authorDto.setName("Александр");
        authorDto.setSurname("Пушкин");

        mockMvc.perform(MockMvcRequestBuilders.get("/author/{id}", authorId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));
    }
    @Test
    public void testGetAuthorByName() throws Exception {
        String authorName = "Александр";
        AuthorDto authorDto = new AuthorDto();
        Long authorId = 1L;
        authorDto.setId(authorId);
        authorDto.setName("Александр");
        authorDto.setSurname("Пушкин");

        mockMvc.perform(MockMvcRequestBuilders.get("/author", authorName).param("name", "Александр"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));
    }
    @Test
    public void testGetAuthorBySQL() throws Exception {
        String authorName = "Александр";
        AuthorDto authorDto = AuthorDto.builder()
                .id(1L)
                .name("Александр")
                .surname("Пушкин")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.get("/author/sql", authorName).param("name", "Александр"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));
    }
    @Test
    public void testGetAuthorCriteria() throws Exception {
        String authorName = "Александр";
        AuthorDto authorDto = AuthorDto.builder()
                .id(1L)
                .name("Александр")
                .surname("Пушкин")
                .build();
        Specification<Author> specification = Specification.where(new Specification<Author>() {
            @Override
            public Predicate toPredicate(Root<Author> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                return cb.equal(root.get("name"), authorName);
            }
        });

        mockMvc.perform(MockMvcRequestBuilders.get("/author/criteria", authorName).param("name", "Александр"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));
    }
    @Test
    public void testCreateAuthor() throws Exception {
        String authorName = "John";
        AuthorDto authorDto = AuthorDto.builder()
                .name("John")
                .surname("Doe")
                .build();
        AuthorCreateDto authorCreateDto = new AuthorCreateDto();
        authorCreateDto.setName("John");
        authorCreateDto.setSurname("Doe");

        String requestBody = new ObjectMapper().writeValueAsString(authorCreateDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/author/create", authorCreateDto).param("name", "John")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorCreateDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorCreateDto.getSurname()));
    }
    @Test
    public void testUpdateAuthor() throws Exception {
     String authorName = "John";
        AuthorDto authorDto = AuthorDto.builder()
                .name("John")
                .surname("Doe")
                .build();
        AuthorUpdateDto authorUpdateDto = new AuthorUpdateDto();
        authorUpdateDto.setId(8L);
        authorUpdateDto.setName("John");
        authorUpdateDto.setSurname("Doe");

        String requestBody = new ObjectMapper().writeValueAsString(authorUpdateDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/author/update", authorUpdateDto).param("name", "John")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorUpdateDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorUpdateDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorUpdateDto.getSurname()));
    }

}
