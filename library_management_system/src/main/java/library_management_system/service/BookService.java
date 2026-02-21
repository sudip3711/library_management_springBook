package library_management_system.service;

import library_management_system.model.Book;
import library_management_system.repo.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepo bookRepo;

    //addBook
    public String addBook(Book book) {
        try {
            bookRepo.save(book);
            return "Book added successfully";
        } catch (Exception e) {
           return "Error adding book: " + e.getMessage();
        }
    }

    //updateBook
    public String updateBook(String isbn, Book updatedBook) {

        try {
            Book existingBook = bookRepo.findByIsbn(isbn);
            if (existingBook != null || existingBook.getIssueRecords()==null ) {
                existingBook.setTitle(updatedBook.getTitle());
                existingBook.setAuthor(updatedBook.getAuthor());
                bookRepo.save(existingBook);
                return "Book updated successfully";
            }
            else
               return "Book not found with isbn " + isbn;

        } catch (Exception e) {
           return "Error updating book: " + e.getMessage();
        }
    }

    //deleteBook
    public String deleteBook(String isbn) {
        try {
            Book book = bookRepo.findByIsbn(isbn);
            if (book != null && book.getIssueRecords()==null && book.getAvailable()) {
                bookRepo.delete(book);
                return "Book with ISBN " + isbn + " deleted successfully";
            } else {
                return "Book not found with isbn " + isbn;
            }
        } catch (Exception e) {
           return "Error deleting book: " + e.getMessage();
        }
    }

    //getAllBooks
    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    //getAllIssuedBooks
    public List<Book> getAllIssuedBooks() {
        Optional<List<Book>> issuedBooks = bookRepo.AllIssueBook();
        if (issuedBooks.isPresent()) {
            return issuedBooks.get();
        } else {
            throw new RuntimeException("No issued books found");
        }
    }
}
