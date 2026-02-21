package library_management_system.controller;


import library_management_system.model.Book;
import library_management_system.model.Member;
import library_management_system.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    //addMember
    @PostMapping("/add")
    public ResponseEntity<String> addMember(@RequestBody Member member) {
         memberService.addMember(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.addMember(member));
    }

    //updateMember
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateMember(@PathVariable Long id, @RequestBody Member updatedMember) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.updateMember(id, updatedMember));
    }

    //deleteMember
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMember(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.deleteMember(id));
    }
    //getAllMembers
    @GetMapping("/all")
    public ResponseEntity<List<Member>> getAllMembers() {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getAllMembers());
    }
}
