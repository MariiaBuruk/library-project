package ru.itgirl.libraryproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.stereotype.Service;
import ru.itgirl.libraryproject.dto.AuthorDto;
import ru.itgirl.libraryproject.dto.BookDto;
import ru.itgirl.libraryproject.dto.GenreDto;
import ru.itgirl.libraryproject.model.Genre;
import ru.itgirl.libraryproject.repository.GenreRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {

    private  final GenreRepository genreRepository;

    @Override
    public GenreDto getGenreById(Long id) {
        log.info("Вызван жанр с id {}", id);
        Optional<Genre> genre = genreRepository.findById(id);
        if (genre.isPresent()){
            GenreDto genreDto = convertToDto(genre.get());
            log.info("Жанр: {}", genreDto.toString());
            return genreDto;
        }
        else {
            log.error("Жанр с id {} не найден", id);
          throw new NoSuchElementException("No value present");
        }
    }

    private GenreDto convertToDto (Genre genre) {
        List<BookDto> bookDtoList = genre.getBooks()
                .stream()
                .map(book -> {
                            List<AuthorDto> authorDtoList = book.getAuthors()
                                    .stream()
                                    .map(author -> AuthorDto.builder()
                                            .id(author.getId())
                                            .name(author.getName())
                                            .surname(author.getSurname())
                                            .build()
                                    ).toList();

                    return BookDto.builder()
                            .authors(authorDtoList)
                            .name(book.getName())
                            .id(book.getId())
                            .genre(book.getGenre().getName())
                            .build();
                }
                ).toList();

        return GenreDto.builder()
                .books(bookDtoList)
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }
}