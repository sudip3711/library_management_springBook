package library_management_system.controller;

import library_management_system.model.IssueRecord;
import library_management_system.repo.IssueRepo;
import library_management_system.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/issue")
public class IssueController {

    @Autowired
    private IssueService issueService;

    //issue book
    @PostMapping("/issuebook")
    public ResponseEntity<String>issueBook(@RequestBody IssueRecord issueRecord){

        return ResponseEntity.status(HttpStatus.CREATED).body(issueService.issueBook(issueRecord));
    }

    //return book
    @PostMapping("/returnbook")
    public ResponseEntity<String> returnBook(@RequestBody Map<String, String> request) {

        String isbn = request.get("isbn");
        return ResponseEntity.ok(issueService.returnBook(isbn));
    }

    //calculate fine
    @GetMapping("/fine/{isbn}")
    public ResponseEntity<String>calculateFineIsbn(@PathVariable String isbn){
        return ResponseEntity.status(HttpStatus.OK).body("Fine amount: ₹ "+issueService.calculateFineUsingIsbn(isbn));
    }
    @GetMapping("/finemi/{memberId}")
    public ResponseEntity<String>calculateFineMId(@PathVariable Long memberId){
        return ResponseEntity.status(HttpStatus.OK).body("Fine amount: ₹ "+issueService.calculateFineUsingMId(memberId));
    }

    @PutMapping("/payfine/{memberId}")
    public ResponseEntity<String>payFine(@PathVariable Long memberId, @RequestParam double amount){
        return ResponseEntity.status(HttpStatus.OK).body(issueService.payFine(memberId, amount));
    }


}
