package com.example.devtiro.repositories;

import com.example.devtiro.TestDataUtils;
import com.example.devtiro.domain.entities.AuthorEntity;
import com.example.devtiro.domain.entities.BookEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookEntityRepositoryIntegrationTests {

    private BookRepository underTest;

    @Autowired
    public BookEntityRepositoryIntegrationTests(BookRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void shouldReturnOneBookWhileFindingOne(){
        AuthorEntity authorEntity = TestDataUtils.createTestAuthorA();
        BookEntity bookEntity = TestDataUtils.createTestBookA(authorEntity);
        underTest.save(bookEntity);
        Optional<BookEntity> result = underTest.findById(bookEntity.getIsbn());
        assertTrue(result.isPresent());
        assertEquals(result.get(), bookEntity);
    }

    @Test
    public void shouldReturnAllBooks(){
        AuthorEntity authorEntityA = TestDataUtils.createTestAuthorA();
        AuthorEntity authorEntityB = TestDataUtils.createTestAuthorB();

        BookEntity bookEntityA = TestDataUtils.createTestBookA(authorEntityA);
        BookEntity bookEntityB = TestDataUtils.createTestBookB(authorEntityB);
        BookEntity bookEntityC = TestDataUtils.createTestBookC(authorEntityB);
        underTest.save(bookEntityA);
        underTest.save(bookEntityB);
        underTest.save(bookEntityC);

        Iterable<BookEntity> result = underTest.findAll();
        assertThat(result).hasSize(3);
        assertThat(result).containsExactly(bookEntityA, bookEntityB, bookEntityC);
    }

    @Test
    public void shouldUpdateById(){
        AuthorEntity authorEntityA = TestDataUtils.createTestAuthorA();
        BookEntity bookEntityA = TestDataUtils.createTestBookA(authorEntityA);
        underTest.save(bookEntityA);
        bookEntityA.setTitle("UPDATED");
        underTest.save(bookEntityA);
        Optional<BookEntity> book = underTest.findById(bookEntityA.getIsbn());
        assertTrue(book.isPresent());
        assertEquals(book.get(), bookEntityA);
    }
    @Test
    public void shouldDeleteById(){
        AuthorEntity authorEntity = TestDataUtils.createTestAuthorA();
        BookEntity bookEntity = TestDataUtils.createTestBookA(authorEntity);
        underTest.save(bookEntity);
        underTest.deleteById(bookEntity.getIsbn());
        Optional<BookEntity> result = underTest.findById(bookEntity.getIsbn());
        assertFalse(result.isPresent());
    }
}
