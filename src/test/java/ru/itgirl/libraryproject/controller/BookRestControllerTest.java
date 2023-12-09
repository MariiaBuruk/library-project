package ru.itgirl.libraryproject.controller;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.itgirl.libraryproject.dto.BookDto;
import ru.itgirl.libraryproject.model.Book;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class BookRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
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
