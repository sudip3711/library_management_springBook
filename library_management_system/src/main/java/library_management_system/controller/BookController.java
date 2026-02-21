package library_management_system.controller;

import library_management_system.model.Book;
import library_management_system.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;


    @PostMapping("/add")
    public ResponseEntity<String> addBook(@RequestBody Book book) {
        String savedBook = bookService.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    //updateBook
    @PutMapping("/update/{isbn}")
    public ResponseEntity<String> updateBook(@RequestBody Book book, @PathVariable String isbn) {
        String updatedBook = bookService.updateBook(isbn, book);
        return ResponseEntity.status(HttpStatus.OK).body(updatedBook);
    }

    //deleteBook
    @DeleteMapping("/delete/{isbn}")
    public ResponseEntity<String> deleteBook(@PathVariable String isbn) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.deleteBook(isbn));
    }

    //getAllBooks
    @GetMapping("/all")
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }
        //getAllIssuedBooks
    @GetMapping("/issued")
    public ResponseEntity<List<Book>> getAllIssuedBooks() {
        return ResponseEntity.ok(bookService.getAllIssuedBooks());
    }
}
