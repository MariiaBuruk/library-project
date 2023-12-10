package ru.itgirl.libraryproject.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import ru.itgirl.libraryproject.dto.AuthorCreateDto;
import ru.itgirl.libraryproject.dto.AuthorDto;
import ru.itgirl.libraryproject.dto.AuthorUpdateDto;
import ru.itgirl.libraryproject.dto.BookDto;
import ru.itgirl.libraryproject.model.Author;
import ru.itgirl.libraryproject.model.Book;
import ru.itgirl.libraryproject.repository.AuthorRepository;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthorServiceTest {
    @Mock
    AuthorRepository authorRepository;
    @InjectMocks
    AuthorServiceImpl authorService;
    @Test
    public void getAuthorById(){
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> books = new HashSet<>();
        Author author = new Author(id, name, surname, books);
        when(authorRepository.findById(id)).thenReturn(Optional.of(author));

        AuthorDto authorDto = authorService.getAuthorById(id);

        verify(authorRepository).findById(id);
        Assertions.assertEquals(authorDto.getId(), author.getId());
        Assertions.assertEquals(authorDto.getName(), author.getName());
        Assertions.assertEquals(authorDto.getSurname(), author.getSurname());
    }
    @Test
    public void testGetAuthorByIdNotFound() {
        Long id = 1L;
        when(authorRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.getAuthorById(id));
        verify(authorRepository).findById(id);
    }
    @Test
    public void getByNameV1(){
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> books = new HashSet<>();
        Author author = new Author(id, name, surname, books);
        when(authorRepository.findAuthorByName(name)).thenReturn(Optional.of(author));

        AuthorDto authorDto = authorService.getByNameV1(name);

        verify(authorRepository).findAuthorByName(name);
        Assertions.assertEquals(authorDto.getId(), author.getId());
        Assertions.assertEquals(authorDto.getName(), author.getName());
        Assertions.assertEquals(authorDto.getSurname(), author.getSurname());
    }
    @Test
    public void testGetAuthorByNameV1NotFound() {
        String name = "John";
        when(authorRepository.findAuthorByName(name)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.getByNameV1(name));
        verify(authorRepository).findAuthorByName(name);
    }
    @Test
    public void getByNameSQL(){
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> books = new HashSet<>();
        Author author = new Author(id, name, surname, books);
        when(authorRepository.findAuthorByName(name)).thenReturn(Optional.of(author));

        AuthorDto authorDto = authorService.getByNameSQL(name);

        verify(authorRepository).findAuthorByName(name);
        Assertions.assertEquals(authorDto.getId(), author.getId());
        Assertions.assertEquals(authorDto.getName(), author.getName());
        Assertions.assertEquals(authorDto.getSurname(), author.getSurname());
    }
    @Test
    public void testGetAuthorByNameSQLNotFound() {
        String name = "John";
        when(authorRepository.findAuthorByName(name)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.getByNameSQL(name));
        verify(authorRepository).findAuthorByName(name);
    }
    @Test
    public void getByCriteria(){
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> books = new HashSet<>();
        Author author = new Author(id, name, surname, books);

        Specification<Author> specification = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name);

        when(authorRepository.findOne(any(Specification.class))).thenReturn(Optional.of(author));
        AuthorDto authorDto = authorService.getByCriteria(name);
        verify(authorRepository).findOne(any(Specification.class));
        Assertions.assertEquals(authorDto.getId(), author.getId());
        Assertions.assertEquals(authorDto.getName(), author.getName());
        Assertions.assertEquals(authorDto.getSurname(), author.getSurname());
    }
     @Test
    public void testGetAuthorByCriteriaNotFound() {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> books = new HashSet<>();
        Author author = new Author(id, name, surname, books);
        Specification<Author> specification = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name);

        when(authorRepository.findOne(any(Specification.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.getByCriteria(name));
        verify(authorRepository).findOne(any(Specification.class));
    }
    @Test
    public void testCreateAuthor(){
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> book = new HashSet<>();
        Author author = new Author(id, name, surname, book);

        AuthorCreateDto authorCreateDto = new AuthorCreateDto();
        authorCreateDto.setName("John");
        authorCreateDto.setSurname("Doe");

        when(authorRepository.save(Mockito.any(Author.class))).thenReturn(author);
        AuthorDto createdAuthor = authorService.createAuthor(authorCreateDto);

        verify(authorRepository).save(Mockito.any(Author.class));
        Assertions.assertEquals(authorCreateDto.getName(), createdAuthor.getName());
    }
    @Test
    public void testUpdateAuthor() {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> book = new HashSet<>();
        Author author = new Author(id, name, surname, book);

        AuthorUpdateDto authorUpdateDto = new AuthorUpdateDto();
        authorUpdateDto.setId(1L);
        authorUpdateDto.setName("John");
        authorUpdateDto.setSurname("Doe");

        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        when(authorRepository.save(Mockito.any(Author.class))).thenReturn(author);
        AuthorDto authorDto = authorService.updateAuthor(authorUpdateDto);

        verify(authorRepository).findById(id);
        verify(authorRepository).save(Mockito.any(Author.class));
        Assertions.assertEquals(authorUpdateDto.getName(), authorDto.getName());
    }
    @Test
    public void testUpdateAuthorFailed() {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> book = new HashSet<>();
        Author author = new Author(id, name, surname, book);

        AuthorUpdateDto authorUpdateDto = new AuthorUpdateDto();
        authorUpdateDto.setId(1L);
        authorUpdateDto.setName("John");
        authorUpdateDto.setSurname("Doe");

        when(authorRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.updateAuthor(authorUpdateDto));
        verify(authorRepository).findById(id);
    }
    @Test
    public void testDeleteAuthor() {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> book = new HashSet<>();
        Author author = new Author(id, name, surname, book);

        String delete = "Автор удален";

        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        doNothing().when(authorRepository).deleteById(id);
        String result = authorService.deleteAuthor(id);
        verify(authorRepository).deleteById(id);
        Assertions.assertEquals(delete, result);
    }
    @Test
    public void testAllAuthors(){
        Long id =1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> book = new HashSet<>();
        Author author = new Author(id, name, surname, book);

        List<Author> authorList = new ArrayList<>();
        authorList.add(author);

        when(authorRepository.findAll()).thenReturn(authorList);
        List<AuthorDto> expectedAuthorDtoList = authorList
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
        List<AuthorDto> authorDtoList = authorService.getAllAuthors();

        verify(authorRepository).findAll();
        Assertions.assertEquals(expectedAuthorDtoList, authorDtoList);
    }

    private AuthorDto convertEntityToDto(Author author) {
        List<BookDto> bookDtoList = null;
        if (author.getBooks() != null) {
            bookDtoList = author.getBooks()
                    .stream()
                    .map(book -> BookDto.builder()
                            .genre(book.getGenre().getName())
                            .name(book.getName())
                            .id(book.getId())
                            .build())
                    .toList();
        }
        return AuthorDto.builder()
                .id(author.getId())
                .name(author.getName())
                .surname(author.getSurname())
                .books(bookDtoList)
                .build();
    }
}
