package com.nashtech.rookies.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.rookies.model.Book;
import com.nashtech.rookies.payload.response.BookDto;
import com.nashtech.rookies.service.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(
		origins = "http://localhost:3000",
		methods = {RequestMethod.GET, RequestMethod.POST}, exposedHeaders = {"Custom-Header"}, 
		maxAge = 30)
public class ApiController {
	
	private final BookService bookService;
	
	public ApiController(BookService bookService) {
		super();
		this.bookService = bookService;
	}

	@GetMapping("/books")
	@CrossOrigin(
			origins = "http://localhost:3000",
			methods = {RequestMethod.GET, RequestMethod.POST}, exposedHeaders = {"Custom-Header"}, 
			maxAge = 30)
    public ResponseEntity<List<BookDto>> getAllBook() {
        return ResponseEntity.ok(this.bookService.getAllBook().stream().map(e -> new BookDto(e.getId(), e.getName(), e.getAuthor().getId())).collect(Collectors.toList()));
    }
	
	@GetMapping("/book/{id}")
	@Operation(summary = "Get a book by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Khong co quyen",
            content = @Content)})
    public ResponseEntity<BookDto> getBookbyId(@PathVariable int id) {
		Optional<Book> book = this.bookService.getBookById(id);
		if(book.isPresent()) {
			Book b1 = book.get();
			return ResponseEntity.ok(new BookDto(b1.getId(), b1.getName(), b1.getAuthor().getId()));
		}else {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
        
    }
	//comment

}