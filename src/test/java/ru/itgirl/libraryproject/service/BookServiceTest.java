package ru.itgirl.libraryproject.service;

import ch.qos.logback.classic.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import ru.itgirl.libraryproject.dto.AuthorDto;
import ru.itgirl.libraryproject.dto.BookCreateDto;
import ru.itgirl.libraryproject.dto.BookDto;
import ru.itgirl.libraryproject.dto.BookUpdateDto;
import ru.itgirl.libraryproject.model.Author;
import ru.itgirl.libraryproject.model.Book;
import ru.itgirl.libraryproject.model.Genre;
import ru.itgirl.libraryproject.repository.AuthorRepository;
import ru.itgirl.libraryproject.repository.BookRepository;
import ru.itgirl.libraryproject.repository.GenreRepository;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookServiceTest {
    @Mock
    BookRepository bookRepository;
    @Mock
    GenreRepository genreRepository;
    @Mock
    AuthorRepository authorRepository;
    @InjectMocks
    BookServiceImpl bookService;
    private AuthorDto authorDto;
    private Book book;
    @Test
    public void getByNameV1(){
        Long id = 1L;
        String name = "Нос";
        Set<Book> books = new HashSet<>();
        Genre genre = new Genre(id, name, books);
        Set<Author> authors = new HashSet<>();
        Book book = new Book(id, name, genre, authors);
        when(bookRepository.findBookByName(name)).thenReturn(Optional.of(book));

        BookDto bookDto = bookService.getByNameV1(name);

        verify(bookRepository).findBookByName(name);
        Assertions.assertEquals(bookDto.getId(), book.getId());
        Assertions.assertEquals(bookDto.getName(), book.getName());
        Assertions.assertEquals(bookDto.getGenre(), book.getGenre().getName());
    }
    @Test
    public void testGetBookByNameV1NotFound() {
        String name = "Нос";
        when(bookRepository.findBookByName(name)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> bookService.getByNameV1(name));
        verify(bookRepository).findBookByName(name);
    }
 @Test
    public void getByNameSQL(){
        Long id = 1L;
        String name = "Нос";
        Set<Book> books = new HashSet<>();
        Genre genre = new Genre(id, name, books);
        Set<Author> authors = new HashSet<>();
        Book book = new Book(id, name, genre, authors);
        when(bookRepository.findBookByNameBySql(name)).thenReturn(Optional.of(book));

        BookDto bookDto = bookService.getByNameBySQL(name);

        verify(bookRepository).findBookByNameBySql(name);
        Assertions.assertEquals(bookDto.getId(), book.getId());
        Assertions.assertEquals(bookDto.getName(), book.getName());
        Assertions.assertEquals(bookDto.getGenre(), book.getGenre().getName());
    }
    @Test
    public void testGetBookBySQLNotFound() {
        String name = "Нос";
        when(bookRepository.findBookByNameBySql(name)).thenReturn(Optional.empty());
            Assertions.assertThrows(NoSuchElementException.class, () -> bookService.getByNameBySQL(name));
        verify(bookRepository).findBookByNameBySql(name);
    }
    @Test
    public void getByCriteria(){
        Long id = 1L;
        String name = "Нос";
        Set<Book> books = new HashSet<>();
        Genre genre = new Genre(id, name, books);
        Set<Author> authors = new HashSet<>();
        Book book = new Book(id, name, genre, authors);

        Specification<Book> specification = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name);

        when(bookRepository.findOne(any(Specification.class))).thenReturn(Optional.of(book));
        BookDto bookDto = bookService.getByNameCriteria(name);
        verify(bookRepository).findOne(any(Specification.class));
        Assertions.assertEquals(bookDto.getId(), book.getId());
        Assertions.assertEquals(bookDto.getName(), book.getName());
        Assertions.assertEquals(bookDto.getGenre(), book.getGenre().getName());
    }
     @Test
    public void testGetBookByCriteriaNotFound() {
        String name = "Нос";
        Specification<Author> specification = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name);

        when(bookRepository.findOne(any(Specification.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> bookService.getByNameCriteria(name));
        verify(bookRepository).findOne(any(Specification.class));
    }
    @Test
    public void testCreateBook(){
        Long id = 1L;
        String name = "Нос";
        Genre genre = new Genre();
        Set<Author> authors = new HashSet<>();
        Book book = new Book(id, name, genre, authors);

        BookCreateDto bookCreateDto = new BookCreateDto();
        bookCreateDto.setName("Нос");

        when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);
        BookDto createdBook = bookService.createBook(bookCreateDto);

        verify(bookRepository).save(Mockito.any(Book.class));
        Assertions.assertEquals(bookCreateDto.getName(), createdBook.getName());
    }

    @Test
    public void testDeleteBook() {
        Long id = 9L;
        String delete = "Книга удалена";

        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        doNothing().when(bookRepository).deleteById(id);
        String result = bookService.deleteBook(id);
        verify(bookRepository).deleteById(id);
        Assertions.assertEquals(delete, result);
    }
    @Test
    public void testAllBooks(){
        Long id = 1L;
        String name = "Нос";
        Set<Book> books = new HashSet<>();
        Genre genre = new Genre(id, name, books);
        Set<Author> authors = new HashSet<>();
        Book book = new Book(id, name, genre, authors);

        List<Book> bookList = new ArrayList<>();
        bookList.add(book);

        when(bookRepository.findAll()).thenReturn(bookList);
        List<BookDto> expectedbookDtoList = bookList
                .stream()
                .map(this::convertEntityToDto)
                .toList();
        List<BookDto> bookDtoList = bookService.getAllBooks();
        verify(bookRepository).findAll();
        Assertions.assertEquals(expectedbookDtoList, bookDtoList);
    }
    private BookDto convertEntityToDto(Book book){
        List<AuthorDto> authorDtoList = null;
        if (book.getAuthors() != null){
            authorDtoList = book.getAuthors()
                    .stream()
                    .map(author -> AuthorDto.builder()
                            .id(author.getId())
                            .name(author.getName())
                            .surname(author.getSurname())
                            .build())
                    .toList();
        }
        BookDto bookDto = BookDto.builder()
                .id(book.getId())
                .genre(book.getGenre().getName())
                .name(book.getName())
                .authors(authorDtoList)
                .build();
        return bookDto;
    }

}
