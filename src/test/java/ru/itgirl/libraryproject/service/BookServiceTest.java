package ru.itgirl.libraryproject.service;

import ch.qos.logback.classic.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.itgirl.libraryproject.dto.BookCreateDto;
import ru.itgirl.libraryproject.dto.BookDto;
import ru.itgirl.libraryproject.model.Author;
import ru.itgirl.libraryproject.model.Book;
import ru.itgirl.libraryproject.model.Genre;
import ru.itgirl.libraryproject.repository.BookRepository;

import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BookServiceTest {
    @Mock
    BookRepository bookRepository;
    @InjectMocks
    BookServiceImpl bookService;
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
    public void testCreateBook(){
        Long id = 1L;
        String name = "Нос";
        Set<Book> books = new HashSet<>();
        Genre genre = new Genre(id, name, books);
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
    public void testAllBooks(){
        List<Book> books = bookRepository.findAll();
        verify(bookRepository).findAll();
    }
}
