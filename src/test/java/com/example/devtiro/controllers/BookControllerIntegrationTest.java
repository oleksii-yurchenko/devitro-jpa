package com.example.devtiro.controllers;

import com.example.devtiro.TestDataUtils;
import com.example.devtiro.domain.dto.AuthorDto;
import com.example.devtiro.domain.dto.BookDto;
import com.example.devtiro.domain.entities.AuthorEntity;
import com.example.devtiro.domain.entities.BookEntity;
import com.example.devtiro.mappers.Mapper;
import com.example.devtiro.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Mapper<AuthorEntity, AuthorDto> authorMapper;
    private BookService bookService;

    @Autowired
    public BookControllerIntegrationTest(MockMvc mockMvc, Mapper<AuthorEntity, AuthorDto> authorMapper, BookService bookService) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.authorMapper = authorMapper;
        this.bookService = bookService;
    }

    @Test
    public void createShouldReturnStatus201CreatedWhenBookDoesntExist() throws Exception {
        String isbn = "11111";
        AuthorEntity testAuthorA = TestDataUtils.createTestAuthorA();
        BookDto testBookDtoA = TestDataUtils.createTestBookDtoA(authorMapper.mapTo(testAuthorA));
        String testBookJson = objectMapper.writeValueAsString(testBookDtoA);

        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + isbn)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testBookJson))
                .andExpect(
                        MockMvcResultMatchers.status().isCreated()
                );
    }

    @Test
    public void updateShouldReturn200OkWhenBookExists() throws Exception {
        String isbn = "11111";
        BookEntity testBookA = TestDataUtils.createTestBookA(null);
        bookService.createUpdateBook(isbn, testBookA);

        BookDto testBookDtoA = TestDataUtils.createTestBookDtoA(null);
        String testBookJson = objectMapper.writeValueAsString(testBookDtoA);

        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + isbn)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testBookJson))
                .andExpect(
                        MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    public void createShouldReturnCreatedBook() throws Exception {
        String isbn = "11111";
        AuthorEntity testAuthorA = TestDataUtils.createTestAuthorA();
        BookDto testBookDtoA = TestDataUtils.createTestBookDtoA(authorMapper.mapTo(testAuthorA));
        String testBookJson = objectMapper.writeValueAsString(testBookDtoA);

        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + isbn)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testBookJson))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.isbn").value(isbn)
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.title").value(testBookDtoA.getTitle())
                );
    }

    @Test
    public void updateShouldUpdateExistedBook() throws Exception {
        String isbn = "11111";
        BookEntity testBookEntityA = TestDataUtils.createTestBookA(null);
        bookService.createUpdateBook(isbn, testBookEntityA);

        BookDto testBookDtoB = TestDataUtils.createTestBookDtoB(null);
        String testBookJson = objectMapper.writeValueAsString(testBookDtoB);

        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + isbn)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testBookJson));

        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].isbn").value(isbn))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(testBookDtoB.getTitle()));
    }

    @Test
    public void listBooksReturnsStatus200Ok() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(
                        MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    public void shouldListBooksReturnsBooks() throws Exception {
        BookEntity testBook = TestDataUtils.createTestBookA(null);
        bookService.createUpdateBook(testBook.getIsbn(), testBook);

        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].isbn").value(testBook.getIsbn())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].title").value(testBook.getTitle())
                );
    }

    @Test
    public void getBookReturnsStatus200OkWhenExist() throws Exception {
        BookEntity testBook = TestDataUtils.createTestBookA(null);
        bookService.createUpdateBook(testBook.getIsbn(), testBook);

        mockMvc.perform(MockMvcRequestBuilders.get("/books/" + testBook.getIsbn()))
                .andExpect(
                        MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    public void getBookReturnsStatus401NotFoundWhenDoesntExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/11111"))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound()
                );
    }

    @Test
    public void getBookShouldReturnsBookByIsbn() throws Exception {
        BookEntity testBook = TestDataUtils.createTestBookA(null);
        bookService.createUpdateBook(testBook.getIsbn(), testBook);

        mockMvc.perform(MockMvcRequestBuilders.get("/books/" + testBook.getIsbn()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.isbn").value(testBook.getIsbn())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.title").value(testBook.getTitle())
                );
    }

    @Test
    public void partialUpdateShouldReturn404NotFoundIfBookDoesntExist() throws Exception {
        BookDto bookDto = TestDataUtils.createTestBookDtoA(null);
        String jsonBookDto = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/books/" + bookDto.getIsbn())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBookDto))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void partialUpdateShouldReturn200OkIfBookExists() throws Exception {
        BookEntity book = TestDataUtils.createTestBookA(null);
        bookService.createUpdateBook(book.getIsbn(), book);
        String bookJson = "{\"title\": \"UPDATED\"}";

        mockMvc.perform(MockMvcRequestBuilders.patch("/books/" + book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void partialUpdateShouldReturnUpdatedBook() throws Exception {
        BookEntity book = TestDataUtils.createTestBookA(null);
        bookService.createUpdateBook(book.getIsbn(), book);

        String bookJson = "{\"title\": \"UPDATED\"}";

        mockMvc.perform(MockMvcRequestBuilders.patch("/books/" + book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.title").value("UPDATED")
                );
    }

    @Test
    public void deleteShouldReturn204NoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent()
                );
    }

    @Test
    public void deleteShouldDeleteBookIfExists() throws Exception {
        BookEntity book = TestDataUtils.createTestBookA(null);
        bookService.createUpdateBook(book.getIsbn(), book);

        mockMvc.perform(MockMvcRequestBuilders.delete("/books/" + book.getIsbn()));
        mockMvc.perform(MockMvcRequestBuilders.get("/books/" + book.getIsbn()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
