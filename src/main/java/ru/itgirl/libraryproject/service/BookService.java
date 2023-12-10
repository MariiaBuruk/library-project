package ru.itgirl.libraryproject.service;

import ru.itgirl.libraryproject.dto.BookCreateDto;
import ru.itgirl.libraryproject.dto.BookDto;
import ru.itgirl.libraryproject.dto.BookUpdateDto;

import java.util.List;

public interface BookService {
    BookDto getByNameV1(String name); //V1
    BookDto getByNameBySQL(String name); //V2
    BookDto getByNameCriteria(String name); //V3

    BookDto createBook(BookCreateDto bookCreateDto);
    BookDto updateBook(BookUpdateDto bookUpdateDto);
    String deleteBook(Long id);

    List<BookDto> getAllBooks();
}
