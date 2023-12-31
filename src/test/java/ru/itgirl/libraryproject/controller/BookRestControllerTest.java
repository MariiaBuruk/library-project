package ru.itgirl.libraryproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.itgirl.libraryproject.dto.AuthorDto;
import ru.itgirl.libraryproject.dto.BookCreateDto;
import ru.itgirl.libraryproject.dto.BookDto;
import ru.itgirl.libraryproject.dto.BookUpdateDto;
import ru.itgirl.libraryproject.model.Author;
import ru.itgirl.libraryproject.model.Book;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Rollback
@Transactional
public class BookRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private BookDto bookDto;
    private AuthorDto authorDto;
    private List<AuthorDto> authorDtoList;
    @Test
    public void testGetBookByName() throws Exception {
        String bookName = "Нос";
        BookDto bookDto = new BookDto();
        Long bookId = 3L;
        bookDto.setId(bookId);
        bookDto.setName("Нос");

        mockMvc.perform(MockMvcRequestBuilders.get("/book", bookName).param("name", "Нос"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(bookDto.getName()));
    }
    @Test
    public void testGetBookBySQL() throws Exception {
       String bookName = "Нос";
        BookDto bookDto = new BookDto();
        Long bookId = 3L;
        bookDto.setId(bookId);
        bookDto.setName("Нос");

        mockMvc.perform(MockMvcRequestBuilders.get("/book/sql", bookName).param("name", "Нос"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(bookDto.getName()));
    }
    @Test
    public void testGetBookCriteria() throws Exception {
        String bookName = "Нос";
        BookDto bookDto = new BookDto();
        Long bookId = 3L;
        bookDto.setId(bookId);
        bookDto.setName("Нос");
        Specification<Book> specification = Specification.where(new Specification<Book>() {
            @Override
            public Predicate toPredicate(Root<Book> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                return cb.equal(root.get("name"), bookName);
            }
        });
        mockMvc.perform(MockMvcRequestBuilders.get("/book/criteria", bookName).param("name", "Нос"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(bookDto.getName()));
    }
}
