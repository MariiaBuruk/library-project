package ru.itgirl.libraryproject.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.itgirl.libraryproject.dto.AuthorDto;
import ru.itgirl.libraryproject.dto.BookCreateDto;
import ru.itgirl.libraryproject.dto.BookDto;
import ru.itgirl.libraryproject.dto.BookUpdateDto;
import ru.itgirl.libraryproject.model.Book;
import ru.itgirl.libraryproject.repository.BookRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    public BookDto getByNameV1(String name) {
        log.info("Попробуйте другое имя {}", name);
        Optional<Book> book = bookRepository.findBookByName(name);
        if (book.isPresent()){
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Книга {}", name);
            return bookDto;
        }
        else {
            log.error("Книга {}, не найдена", name);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public BookDto getByNameBySQL(String name) {
        log.info("Попробуйте другое имя {}", name);
        Optional<Book> book = bookRepository.findBookByNameBySql(name);
        if (book.isPresent()){
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Книга {}", name);
            return bookDto;
        }
        else {
            log.error("Книга {}, не найдена", name);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public BookDto getByNameV3(String name){
        log.info("Попробуйте другое имя {}", name);
        Specification<Book> specification = Specification.where(new Specification<Book>() {
            @Override
            public Predicate toPredicate(Root<Book> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                return cb.equal(root.get("name"), name);
            }
        });

        Optional<Book> book = bookRepository.findOne(specification);
        if (book.isPresent()){
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Книга {}", name);
            return bookDto;
        }
        else {
            log.error("Книга {}, не найдена", name);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public BookDto createBook(BookCreateDto bookCreateDto) {
        log.info("Была создана книга {}", bookCreateDto);
        Book book = bookRepository.save(convertToDtoEntity(bookCreateDto));
        BookDto bookDto = convertEntityToDto(book);
        return bookDto;
    }

    @Override
    public BookDto updateBook(BookUpdateDto bookUpdateDto) {
        log.info("Была изменена книга {}", bookUpdateDto);
        Book book = bookRepository.findById(bookUpdateDto.getId()).orElseThrow();
        book.setName(bookUpdateDto.getName());
        book.setGenre(bookUpdateDto.getGenre());
        book.setAuthors(bookUpdateDto.getAuthors());

        Book savedBook = bookRepository.save(book);
        BookDto bookDto = convertEntityToDto(savedBook);
        return bookDto;
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        log.info("Был вызван список Книг");
        return books.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    private Book convertToDtoEntity(BookCreateDto bookCreateDto){
        return Book.builder()
                .name(bookCreateDto.getName())
                .genre(bookCreateDto.getGenre())
                .authors(bookCreateDto.getAuthors())
                .build();
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
