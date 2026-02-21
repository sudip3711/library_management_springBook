package library_management_system.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Data
@NoArgsConstructor
@ToString(exclude = "issueRecord")
public class Book {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String title;
    private String author;
    @Column(unique = true)
    private String isbn;
    private Boolean available;

    @ManyToOne
    @JoinColumn
    private IssueRecord issueRecords;
}
