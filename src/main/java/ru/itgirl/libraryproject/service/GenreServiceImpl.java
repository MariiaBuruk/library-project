package ru.itgirl.libraryproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itgirl.libraryproject.dto.AuthorDto;
import ru.itgirl.libraryproject.dto.BookDto;
import ru.itgirl.libraryproject.dto.GenreDto;
import ru.itgirl.libraryproject.model.Genre;
import ru.itgirl.libraryproject.repository.GenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private  final GenreRepository genreRepository;
    @Override
    public GenreDto getGenreById(Long id) {
        Genre genre = genreRepository.findById(id).orElseThrow();
        return convertToDto(genre);
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