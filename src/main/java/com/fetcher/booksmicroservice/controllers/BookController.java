package com.fetcher.booksmicroservice.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.fetcher.booksmicroservice.DTO.BookDTO;
import com.fetcher.booksmicroservice.entities.Book;
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
	private List<BookDTO> findall(
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
	private void deleteBook(@PathVariable("id")final Long id, @RequestHeader("username") final String username){
		Optional<BookDTO> optional = service.findById(id);
		if(optional.isPresent() && optional.get().getAuthor().getUsername().equals(username)) {
			service.unpublishBook(id);
			return;
		}
		throw new UnauthorizedException("because you are trying to update a book that don't belongs to you");
	}
	
	@PutMapping("/books")
	private BookDTO newBook(@RequestBody BookDTO newBook) {
//		if(service.findBookByBookname(newBook.getBookname()).isPresent()) {
//			throw new BooknameTakenException(newBook.getBookname());
//		}
//		return service.save(newBook);
		return null;
	}
	
	private BookDTO updateBook(@RequestBody BookDTO newBook, @RequestHeader("username") String authenticatedBook) {
//		if(authenticatedBook.equals(newBook.getBookname())) {
//			return service.update(newBook);
//		}
//		throw new UnauthorizedException("because you are trying to update a book that don't belongs to you");
		return null;
		
	}
	
}
