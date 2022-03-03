package com.fetcher.booksmicroservice.converters;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fetcher.booksmicroservice.DTO.BookDTO;
import com.fetcher.booksmicroservice.entities.Book;

@Service
public class BookConverter {

	public BookDTO toDTO(final Book Book) {
		final ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(Book, BookDTO.class);
	}
	
	public List<BookDTO> toDTO(final List<Book> Books) {
		List<BookDTO> resultDTO = new ArrayList<>();
		final ModelMapper modelMapper = new ModelMapper();
		for (Book Book : Books) {
			resultDTO.add(modelMapper.map(Book, BookDTO.class));
		}
		return resultDTO;
	}
	
	public Book toEntity(final BookDTO dto) {
		final ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(dto, Book.class);
	}
}
