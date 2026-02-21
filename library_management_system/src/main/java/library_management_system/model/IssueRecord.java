package library_management_system.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude = "book")
public class IssueRecord {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private LocalDate issueDate;
    private LocalDate returnDate;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(cascade = {CascadeType.PERSIST,
            CascadeType.MERGE},mappedBy = "issueRecords")
    private List<Book> book;
}
