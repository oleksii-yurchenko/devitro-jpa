package com.example.devtiro.controllers;

import com.example.devtiro.TestDataUtils;
import com.example.devtiro.domain.dto.AuthorDto;
import com.example.devtiro.domain.entities.AuthorEntity;
import com.example.devtiro.services.AuthorService;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class AuthorControllerIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthorService authorService;

    @Autowired
    public AuthorControllerIntegrationTest(MockMvc mockMvc, AuthorService authorService) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.authorService = authorService;
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsHttp201Created() throws Exception {
        AuthorDto testAuthorDtoA = TestDataUtils.createTestAuthorDtoA();
        testAuthorDtoA.setId(null);
        String authorJson = objectMapper.writeValueAsString(testAuthorDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsSavedAuthor() throws Exception {
        AuthorDto testAuthorDtoA = TestDataUtils.createTestAuthorDtoA();
        testAuthorDtoA.setId(null);
        String authorJson = objectMapper.writeValueAsString(testAuthorDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testAuthorDtoA.getName())
        )
        .andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(testAuthorDtoA.getAge())
        );
    }

    @Test
    public void testThatListAuthorsReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListAuthorsReturnsListOfAuthors() throws Exception {
        AuthorEntity testAuthorA = TestDataUtils.createTestAuthorA();
        authorService.save(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.length()").value(1)
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].name").value(testAuthorA.getName())
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].age").value(testAuthorA.getAge())
                );
    }

    @Test
    public void testThatGetAuthorsReturns200OkIfExist() throws Exception {
        AuthorEntity testAuthorA = TestDataUtils.createTestAuthorA();
        authorService.save(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/" + testAuthorA.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetAuthorsReturns401IfDoesntExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/1")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetAuthorReturnsAuthor() throws Exception {
        AuthorEntity testAuthorA = TestDataUtils.createTestAuthorA();
        authorService.save(testAuthorA);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/authors/" + testAuthorA.getId())
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.id").isNumber()
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.name").value(testAuthorA.getName())
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.age").value(testAuthorA.getAge())
                );
    }

    @Test
    public void fullUpdateAuthorReturns404WhenAuthorDoesntExists() throws Exception {
        AuthorDto testAuthorDto = TestDataUtils.createTestAuthorDtoA();
        String authorJson = objectMapper.writeValueAsString(testAuthorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void fullUpdateAuthorReturnsNewAuthor() throws Exception {
        AuthorEntity testAuthorA = TestDataUtils.createTestAuthorA();
        testAuthorA.setId(null);
        AuthorEntity savedAuthor = authorService.save(testAuthorA);

        AuthorDto testAuthorDtoB = TestDataUtils.createTestAuthorDtoB();
        String authorJson = objectMapper.writeValueAsString(testAuthorDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(savedAuthor.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(testAuthorDtoB.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(testAuthorDtoB.getAge()));
    }

    @Test
    public void patchAuthorReturns404WhenAuthorDoesntExists() throws Exception {
        AuthorDto testAuthorDto = TestDataUtils.createTestAuthorDtoA();
        String authorJson = objectMapper.writeValueAsString(testAuthorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void patchAuthorReturns200OkWhenAuthorExist() throws Exception {
        AuthorEntity authorEntity = TestDataUtils.createTestAuthorA();
        AuthorEntity authorEntitySaved = authorService.save(authorEntity);

        AuthorDto authorDto = TestDataUtils.createTestAuthorDtoA();
        authorDto.setId(authorEntitySaved.getId());
        String authorJson = objectMapper.writeValueAsString(authorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + authorEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void patchAuthorReturnsPatchedAuthor() throws Exception {
        AuthorEntity authorEntity = TestDataUtils.createTestAuthorA();
        authorService.save(authorEntity);
        String authorJson = "{\"name\": \"UPDATED\"}";

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + authorEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("UPDATED"));
    }

    @Test
    public void deleteShouldReturn204NoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/authors/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void deleteShouldDeleteAuthorIfExists() throws Exception {
        AuthorEntity author = TestDataUtils.createTestAuthorA();
        authorService.save(author);

        mockMvc.perform(MockMvcRequestBuilders.delete("/authors/" + author.getId()));
        mockMvc.perform(MockMvcRequestBuilders.get("/authors/" + author.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
