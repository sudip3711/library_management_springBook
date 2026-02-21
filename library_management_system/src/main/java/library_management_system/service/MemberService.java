package library_management_system.service;

import library_management_system.model.Member;
import library_management_system.repo.MemberRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepo memberRepo;

    public String addMember(Member member) {
        try {
            if (member.getName() == null || member.getName().isEmpty()) {
                return "Member name cannot be null or empty";
            }
            if (member.getEmail() == null || member.getEmail().isEmpty()) {
                return "Member email cannot be null or empty";
            }
            memberRepo.save(member);
            return "Member added successfully";
        }catch (Exception e) {
            return "Error adding member: " + e.getMessage();
        }
    }

    //updateMember
    public String updateMember(Long id, Member updatedMember) {
        try {
            Optional<Member> existingMember = memberRepo.findById(id);

            if (existingMember.isEmpty()) {
                return "Member not found with id " + id;
            }
            existingMember.get().setName(updatedMember.getName());
            existingMember.get().setEmail(updatedMember.getEmail());
            existingMember.get().setPhone(updatedMember.getPhone());
            memberRepo.save(existingMember.get());
            return "Member updated successfully";
        } catch (Exception e) {
           return "Error updating member: " + e.getMessage();
        }
    }

    //deleteMember
    public String deleteMember(Long id) {
        try {
            Optional<Member> member = memberRepo.findById(id);
            if (member.isEmpty()) {
                return "Member not found with id " + id;
            }
            memberRepo.delete(member.get());
            return "Member deleted successfully";
        } catch (Exception e) {
            return "Error deleting member: " + e.getMessage();
        }
    }

    //getAllMembers
    public List<Member> getAllMembers() {
        return memberRepo.findAll();
    }
}
