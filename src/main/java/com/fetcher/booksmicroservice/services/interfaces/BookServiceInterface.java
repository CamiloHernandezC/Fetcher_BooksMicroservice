package com.fetcher.booksmicroservice.services.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;

import com.fetcher.booksmicroservice.DTO.BookDTO;
import com.fetcher.booksmicroservice.entities.Book;

public interface BookServiceInterface {

	Optional<BookDTO> findById(Long id);
	
	List<BookDTO> findall(Specification<Book> bookSpec);

	void unpublishBook(Long id);

	BookDTO save(BookDTO newBook);

}
