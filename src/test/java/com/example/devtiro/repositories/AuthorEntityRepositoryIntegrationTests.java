package com.example.devtiro.repositories;

import com.example.devtiro.TestDataUtils;
import com.example.devtiro.domain.entities.AuthorEntity;
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
public class AuthorEntityRepositoryIntegrationTests {

    private final AuthorRepository underTest;

    @Autowired
    public AuthorEntityRepositoryIntegrationTests(AuthorRepository underTest) {
        this.underTest = underTest;
    }


    @Test
    public void testThatAuthorCanBeCreatedAndRecalled(){
        AuthorEntity authorEntity = TestDataUtils.createTestAuthorA();
        underTest.save(authorEntity);
        Optional<AuthorEntity> result = underTest.findById(authorEntity.getId());
        assertTrue(result.isPresent(), "The result should be present");
        assertEquals(authorEntity, result.get(), "The retrieved author should match the created author");
    }

  @Test
    public void testThatFindReturnsAllAuthors(){
        AuthorEntity authorEntityA = TestDataUtils.createTestAuthorA();
        AuthorEntity authorEntityB = TestDataUtils.createTestAuthorB();
        AuthorEntity authorEntityC = TestDataUtils.createTestAuthorC();

        underTest.save(authorEntityA);
        underTest.save(authorEntityB);
        underTest.save(authorEntityC);

        Iterable<AuthorEntity> result = underTest.findAll();
        assertThat(result).hasSize(3);
      assertThat(result).containsExactly(authorEntityA, authorEntityB, authorEntityC);
    }

    @Test
    public void testThatUpdateAuthorById(){
        AuthorEntity authorEntityA = TestDataUtils.createTestAuthorA();
        underTest.save(authorEntityA);
        authorEntityA.setName("UPDATED");
        underTest.save(authorEntityA);
        Optional<AuthorEntity> author = underTest.findById(authorEntityA.getId());
        assertTrue(author.isPresent());
        assertEquals(author.get(), authorEntityA);
    }

    @Test
    public void shouldDeleteAuthorById(){
        AuthorEntity authorEntity = TestDataUtils.createTestAuthorA();
        underTest.save(authorEntity);
        underTest.deleteById(authorEntity.getId());
        Optional<AuthorEntity> result = underTest.findById(authorEntity.getId());
        assertFalse(result.isPresent());
    }

    @Test
    public void testThatGetAuthorsWithAgeLessThan(){
        AuthorEntity testAuthorAEntity = TestDataUtils.createTestAuthorA();
        AuthorEntity testAuthorBEntity = TestDataUtils.createTestAuthorB();
        AuthorEntity testAuthorCEntity = TestDataUtils.createTestAuthorC();

        underTest.save(testAuthorAEntity);
        underTest.save(testAuthorBEntity);
        underTest.save(testAuthorCEntity);

        Iterable<AuthorEntity> result = underTest.ageLessThan(50);
        assertThat(result).containsExactly(testAuthorCEntity);
    }

    @Test
    public void testThatGetAuthorsWithAgeGreaterThan(){
        AuthorEntity testAuthorAEntity = TestDataUtils.createTestAuthorA();
        AuthorEntity testAuthorBEntity = TestDataUtils.createTestAuthorB();
        AuthorEntity testAuthorCEntity = TestDataUtils.createTestAuthorC();

        underTest.save(testAuthorAEntity);
        underTest.save(testAuthorBEntity);
        underTest.save(testAuthorCEntity);

        Iterable<AuthorEntity> result = underTest.findAuthorsWithAgeGreaterThan(50);
        assertThat(result).containsExactly(testAuthorAEntity, testAuthorBEntity);
    }

}
