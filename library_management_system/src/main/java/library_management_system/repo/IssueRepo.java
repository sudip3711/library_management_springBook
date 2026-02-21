package library_management_system.repo;

import library_management_system.model.IssueRecord;
import library_management_system.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepo extends JpaRepository<IssueRecord, Long> {
}
