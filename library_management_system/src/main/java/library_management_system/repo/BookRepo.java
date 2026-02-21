package library_management_system.repo;

import library_management_system.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {
    //findByIsbn
    Book findByIsbn(String isbn);

    //findAllIssuedBooks
    @Query("SELECT b FROM Book b WHERE b.issueRecords IS NOT NULL")
    Optional<List<Book>>AllIssueBook();

}
