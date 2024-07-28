package com.example.devtiro.services;

import com.example.devtiro.TestDataUtils;
import com.example.devtiro.domain.entities.AuthorEntity;
import com.example.devtiro.domain.entities.BookEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Optional;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
public class AuthorServiceIntegrationTest {

    private final AuthorService authorService;
    private final BookService bookService;

    @Autowired
    public AuthorServiceIntegrationTest(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @Test
    public void findByNameShouldFindAuthor() throws Exception {
        AuthorEntity author = TestDataUtils.createTestAuthorA();
        authorService.save(author);
        Optional<AuthorEntity> foundAuthor = authorService.findByName(author.getName());
        Assertions.assertTrue(foundAuthor.isPresent());
    }

    @Test
    public void shouldUseExistingAuthorWhenSavingNewBook(){
        AuthorEntity author = TestDataUtils.createTestAuthorA();
        BookEntity bookA = TestDataUtils.createTestBookA(author);
        bookService.createUpdateBook(bookA.getIsbn(), bookA);

        authorService
                .findByName(author.getName())
                .ifPresent(existingAuthor -> {
                    BookEntity bookB = TestDataUtils.createTestBookB(existingAuthor);
                    bookService.createUpdateBook(bookB.getIsbn(), bookB);
                });

        int authorsCount = authorService.findAll().size();
        int booksCount = bookService.findAll().size();

        Assertions.assertEquals(authorsCount, 1);
        Assertions.assertEquals(booksCount, 2);
    }

    @Test
    public void shouldUseExistingAuthorWhenSavingNewBook2(){
        AuthorEntity author = TestDataUtils.createTestAuthorA();
        author.setId(null);
        BookEntity bookA = TestDataUtils.createTestBookA(author);
        BookEntity bookB = TestDataUtils.createTestBookB(author);
        bookService.createUpdateBook(bookA.getIsbn(), bookA);
        bookService.createUpdateBook(bookB.getIsbn(), bookB);

        int authorsCount = authorService.findAll().size();
        int booksCount = bookService.findAll().size();

        Assertions.assertEquals(authorsCount, 2);
        Assertions.assertEquals(booksCount, 2);
    }
}
