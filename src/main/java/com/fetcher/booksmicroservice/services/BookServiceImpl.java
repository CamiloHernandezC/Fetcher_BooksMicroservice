package com.fetcher.booksmicroservice.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.fetcher.booksmicroservice.DTO.BookDTO;
import com.fetcher.booksmicroservice.converters.BookConverter;
import com.fetcher.booksmicroservice.entities.Book;
import com.fetcher.booksmicroservice.repositories.BookRepository;
import com.fetcher.booksmicroservice.services.interfaces.BookServiceInterface;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookServiceInterface{

	private final BookRepository repository;
	private final BookConverter converter;
	
	@Override
	public List<BookDTO> findall(Specification<Book> bookSpec) {
		return converter.toDTO(repository.findAll(bookSpec));
	}

	@Override
	public void unpublishBook(Long id) {
		repository.deleteById(id);
	}

	@Override
	public BookDTO save(BookDTO newBook) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<BookDTO> findById(Long id) {
		Optional<Book> book = repository.findById(id);
		if(book.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(converter.toDTO(book.get()));
	}

	

}
