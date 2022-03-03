package com.fetcher.booksmicroservice.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fetcher.booksmicroservice.DTO.BookDTO;
import com.fetcher.booksmicroservice.DTO.UserDTO;
import com.fetcher.booksmicroservice.entities.Book;
import com.fetcher.booksmicroservice.entities.User;
import com.fetcher.booksmicroservice.exceptions.BookNotFoundException;
import com.fetcher.booksmicroservice.exceptions.UnauthorizedException;
import com.fetcher.booksmicroservice.repositories.BookRepository;

@SpringBootTest
class BookControllerTests {
	
	protected MockMvc mockMvc;

	@MockBean
	private BookRepository repository;

	@Autowired
	WebApplicationContext wac;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	private static final String TEST_BOOK_TITLE = "Book Title";
	private static final String TEST_BOOK_DESCRIPTION = "Book Description";
	private static final String TEST_IMAGE_PATH = "assets/books.png";
	private static final double TEST_PRICE = 5000;
	
	private static final String WRONG_TEST_USER_NAME = "wrong user name";
	private static final String TEST_USER_NAME = "test user name";
	private final User userMock = new User(888L, "testUser", "testPseudonim", TEST_USER_NAME, "testPassword", null);
	private final UserDTO userDTOMock = new UserDTO(888L, "testUser", "testPseudonim", TEST_USER_NAME, "testPassword", null);
	private final Book bookMock = new Book(999L, TEST_BOOK_TITLE, TEST_BOOK_DESCRIPTION, userMock, TEST_IMAGE_PATH, TEST_PRICE);
	private final BookDTO bookDTOMock = new BookDTO(999L, TEST_BOOK_TITLE, TEST_BOOK_DESCRIPTION, userDTOMock, TEST_IMAGE_PATH, TEST_PRICE);

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	@Test
	void findAll() throws Exception {
		when(repository.findAll(Mockito.any(Specification.class))).thenReturn(Collections.singletonList(bookMock));
		mockMvc.perform(get("/books?title=title"))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$").isArray())
		.andExpect(jsonPath("$[0].title").value(bookMock.getTitle()))
		.andExpect(jsonPath("$[1]").doesNotExist());
	}
	
	@Test
	void unpublishBook() throws Exception {
		when(repository.findById(bookMock.getId())).thenReturn(Optional.of(bookMock));
		mockMvc.perform(MockMvcRequestBuilders.delete("/books/999")
                .header("username", TEST_USER_NAME))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isOk());
	}
	
	@Test
	void unpublishBookBookNotFoundError() throws Exception {
		when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		mockMvc.perform(MockMvcRequestBuilders.delete("/books/999")
                .header("username", TEST_USER_NAME))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
	}
	
	@Test
	void unpublishBookUnauthorizedException() throws Exception {
		when(repository.findById(bookMock.getId())).thenReturn(Optional.of(bookMock));
		mockMvc.perform(MockMvcRequestBuilders.delete("/books/999")
                .header("username", WRONG_TEST_USER_NAME))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
	}
	
	@Test
	void publishBook() throws Exception {
		when(repository.save(bookMock)).thenReturn(bookMock);
		ObjectMapper mapper = new ObjectMapper();
	    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
	    String requestJson=ow.writeValueAsString(bookDTOMock);
		mockMvc.perform(post("/books").contentType(APPLICATION_JSON_UTF8)
				.content(requestJson))
				.andExpect(status().isOk());
	}
	
	
	
}
