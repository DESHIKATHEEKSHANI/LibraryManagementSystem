package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "BorrowingTransactions")
public class BorrowingTransactionEntity {

    @Id
    @Column(unique = true, nullable = false, length = 10)
    private String transactionID;

    @ManyToOne
    @JoinColumn(name = "memberID", referencedColumnName = "memberID", nullable = false)
    private MemberEntity member;

    @ManyToOne
    @JoinColumn(name = "bookID", referencedColumnName = "bookID", nullable = false)
    private BookEntity book;

    @Column(nullable = false)
    private LocalDate borrowDate = LocalDate.now();

    @Column
    private LocalDate returnDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false, length = 20)
    private String status = "Borrowed";
}
