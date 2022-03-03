package com.fetcher.booksmicroservice.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.fetcher.booksmicroservice.DTO.BookDTO;
import com.fetcher.booksmicroservice.entities.Book;
import com.fetcher.booksmicroservice.exceptions.BookNotFoundException;
import com.fetcher.booksmicroservice.exceptions.UnauthorizedException;
import com.fetcher.booksmicroservice.services.interfaces.BookServiceInterface;

import lombok.RequiredArgsConstructor;
import net.kaczmarzyk.spring.data.jpa.domain.Between;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;



@RestController
@RequiredArgsConstructor
public class BookController {
	
	private final BookServiceInterface service;

	@GetMapping("/books")
	List<BookDTO> findall(
			@And({
			@Spec(path = "description",	params = "description",	spec = Like.class),
			@Spec(path = "title",	params = "title",	spec = Like.class),
			@Spec(path = "author.username",	params = "author",	spec = Like.class),
			@Spec(path = "price", params={"priceGreaterThan","priceLessThan"}, spec=Between.class)
			})
			Specification<Book> bookSpec){
		
		return (service.findall(bookSpec));
	}
	
	
	@DeleteMapping("/books/{id}")  
	void deleteBook(@PathVariable("id")final Long id, @RequestHeader("username") final String username){
		Optional<BookDTO> optional = service.findById(id);
		if(optional.isEmpty()) {
			throw new BookNotFoundException();
		}
		if(!optional.get().getAuthor().getUsername().equals(username)) {
			throw new UnauthorizedException("because you are trying to unpublish a book that don't belongs to you");
		}
		service.unpublishBook(id);
	}
	
	@PostMapping("/books")
	BookDTO newBook(@RequestBody BookDTO newBook) {
		return service.save(newBook);
	}
	
}
