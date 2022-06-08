package com.example.bookstore.dto;

import com.example.bookstore.entity.Genre;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenreDto {
    private String name;
    private String description;

    public static GenreDto mapGenreToDto(Genre genre){
        GenreDto genreDto = new GenreDto();
        genreDto.setDescription(genre.getGenreDescription());
        genreDto.setName(genre.getName());
        return genreDto;
    }
    public static Genre mapDtoToGenre(GenreDto genreDto){
        Genre genre = new Genre();
        genre.setGenreDescription(genreDto.getDescription());
        genre.setName(genreDto.getName());
        return genre;
    }
}
