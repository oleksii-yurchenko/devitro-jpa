package com.example.devtiro;

import com.example.devtiro.domain.dto.AuthorDto;
import com.example.devtiro.domain.dto.BookDto;
import com.example.devtiro.domain.entities.AuthorEntity;
import com.example.devtiro.domain.entities.BookEntity;

public class TestDataUtils {
    public static AuthorEntity createTestAuthorA() {
        return AuthorEntity.builder()
                .id(1L)
                .name("Abigail Rose")
                .age(80)
                .build();
    }

    public static AuthorEntity createTestAuthorB() {
        return AuthorEntity.builder()
                .id(2L)
                .name("John Smith")
                .age(70)
                .build();
    }

    public static AuthorEntity createTestAuthorC() {
        return AuthorEntity.builder()
                .id(3L)
                .name("Anna Adams")
                .age(30)
                .build();
    }

    public static AuthorDto createTestAuthorDtoA() {
        return AuthorDto.builder()
                .id(1L)
                .name("Abigail Rose")
                .age(80)
                .build();
    }

    public static AuthorDto createTestAuthorDtoB() {
        return AuthorDto.builder()
                .id(2L)
                .name("John Smith")
                .age(70)
                .build();
    }

    public static AuthorDto createTestAuthorDtoC() {
        return AuthorDto.builder()
                .id(3L)
                .name("Anna Adams")
                .age(30)
                .build();
    }

    public static BookEntity createTestBookA(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("TEST-123-123")
                .title("Broken Hope")
                .authorEntity(authorEntity)
                .build();
    }

    public static BookEntity createTestBookB(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("TEST-111-111")
                .title("Ancient Sun")
                .authorEntity(authorEntity)
                .build();
    }

    public static BookEntity createTestBookC(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("TEST-666-555")
                .title("Deep advice")
                .authorEntity(authorEntity)
                .build();
    }

    public static BookDto createTestBookDtoA(final AuthorDto authorDto) {
        return BookDto.builder()
                .isbn("TEST-123-123")
                .title("Broken Hope")
                .author(authorDto)
                .build();
    }

    public static BookDto createTestBookDtoB(final AuthorDto authorDto) {
        return BookDto.builder()
                .isbn("TEST-666-223")
                .title("Book Title")
                .author(authorDto)
                .build();
    }

    public static BookDto createTestBookDtoC(final AuthorDto authorDto) {
        return BookDto.builder()
                .isbn("TEST-777-223")
                .title("Several Moments")
                .author(authorDto)
                .build();
    }
}
