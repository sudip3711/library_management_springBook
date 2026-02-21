package library_management_system.service;

import jakarta.transaction.Transactional;
import library_management_system.model.Book;
import library_management_system.model.IssueRecord;
import library_management_system.model.Member;
import library_management_system.repo.BookRepo;
import library_management_system.repo.IssueRepo;
import library_management_system.repo.MemberRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class IssueService {

    @Autowired
    private IssueRepo issueRepo;
    @Autowired
    private BookRepo bookRepo;
    @Autowired
    private MemberRepo memberRepo;



    public String issueBook(IssueRecord req) {

        //Validate Member
        if (req.getMember() == null || req.getMember().getId() == 0) {
            return "Member details are missing";
        }

        Member member = memberRepo.findById(req.getMember().getId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        //calculate fine before issuing new book
        //Check Fine
        double pendingFine = calculateFineUsingMId(member.getId());

        double fineToShow = Math.max(pendingFine, member.getFineAmount());


        if ( pendingFine > 0 || member.getFineAmount() > 0) {
            return "Pending fine ₹" + fineToShow + ". Clear it first.";
        }

        //Validate Book List
        if (req.getBook() == null || req.getBook().isEmpty()) {
            return "No books selected";
        }

        if (req.getBook().size() > 5) {
            return "Cannot issue more than 5 books";
        }

        List<Book> issuedBooks = new ArrayList<>();

        // Validate Each Book
        for (Book b : req.getBook()) {

            if (b.getIsbn() == null) {
              return "Book ISBN is missing";
            }

            Book book = bookRepo.findByIsbn(b.getIsbn());

            if (book == null) {
                return "Book not found: " + b.getIsbn();
            }

            if (!book.getAvailable()) {
               return "Book already issued: " + b.getIsbn();
            }

            // Mark book unavailable
            book.setAvailable(false);

            // Set owning side relationship
            book.setIssueRecords(req);

            issuedBooks.add(book);
        }

        //Set Issue Details
        req.setMember(member);
        req.setIssueDate(LocalDate.now());
        req.setReturnDate(LocalDate.now().plusDays(10));
        req.setBook(issuedBooks);

        //Save IssueRecord
        issueRepo.save(req);

        return "Books issued successfully";
    }


    @Transactional
    public String returnBook(String isbn) {

        // Find book
        Book book = bookRepo.findByIsbn(isbn);

        if (book == null) {
            return "Book not found with ISBN: " + isbn;
        }

        if (book.getAvailable()) {
            return "Book already returned";
        }

        IssueRecord issueRecord = book.getIssueRecords();
        if (issueRecord == null) {
            return "No issue record found for this book";
        }

        double fine = calculateFineUsingIsbn(isbn);

        // Mark book as available
        book.setAvailable(true);

        // Remove book from issueRecord book list
        issueRecord.getBook().remove(book);

        // Break relationship from book side
        book.setIssueRecords(null);

        bookRepo.save(book);

        //If no books left → delete issueRecord
        if (issueRecord.getBook().isEmpty()) {
            issueRepo.delete(issueRecord);
        } else {
            issueRepo.save(issueRecord);
        }

        return "Book returned successfully. Fine ₹: " + fine;
    }


    @Transactional
    public Double calculateFineUsingIsbn(String isbn) {

        // Find book by ISBN
        Book book = bookRepo.findByIsbn(isbn);

        if (book == null) {
            throw new RuntimeException("Book not found with ISBN: " + isbn);
        }

        // Get issue record
        IssueRecord issueRecord = book.getIssueRecords();

        if (issueRecord == null) {
            throw new RuntimeException("No issue record found for this book.");
        }

        Member member = issueRecord.getMember();

        LocalDate returnDate = issueRecord.getReturnDate();
        LocalDate currentDate = LocalDate.now();

        // Check if late
        if (currentDate.isAfter(returnDate)) {

            long daysLate = java.time.temporal.ChronoUnit
                    .DAYS.between(returnDate, currentDate);

            double fine = daysLate * 10;   // ₹10 per day

            member.setFineAmount(fine);

            memberRepo.save(member);


            return fine;
        } else {
            return 0.0;
        }
    }


    public double calculateFineUsingMId(Long memberId) {

        Member member = memberRepo.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        List<IssueRecord> issueRecords = member.getIssueRecords();

        if (issueRecords == null || issueRecords.isEmpty()) {
            return 0;
        }

        double totalFine = 0;
        LocalDate currentDate = LocalDate.now();

        for (IssueRecord issue : issueRecords) {
            LocalDate returnDate = issue.getReturnDate();
            if (returnDate != null && currentDate.isAfter(returnDate)) {
                long daysLate = java.time.temporal.ChronoUnit.DAYS.between(returnDate, currentDate);
                totalFine += daysLate * 10; // ₹10 per late day
            }
        }

        // No need to save to database
        return totalFine;
    }

    @Transactional
    public String payFine(Long memberId, double amount) {

        if (memberId == null || memberId <= 0) {
            return "Invalid Member ID";
        }

        if (amount <= 0) {
            return "Amount must be greater than 0";
        }

        Member member = memberRepo.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        double fineAmount = member.getFineAmount();

        if (fineAmount <= 0) {
            return "No pending fine for this member.";
        }

        if (amount > fineAmount) {
            return "Amount exceeds pending fine. Pending fine: ₹" + fineAmount;
        }

        double remainingFine = fineAmount - amount;

        // Store remaining fine in database (optional)
        member.setFineAmount(remainingFine);
        memberRepo.save(member);

        if (remainingFine == 0) {
            return "Fine fully paid. No pending dues.";
        } else {
            return "Partial payment done. Remaining fine: ₹" + remainingFine;
        }
    }
}
