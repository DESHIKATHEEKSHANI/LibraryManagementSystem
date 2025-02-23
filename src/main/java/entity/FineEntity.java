package entity;

import jakarta.persistence.*;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Fines")
public class FineEntity {

    @Id
    @Column(unique = true, nullable = false, length = 10)
    private String fineID;

    @ManyToOne
    @JoinColumn(name = "memberID", referencedColumnName = "memberID", nullable = false)
    private MemberEntity member;

    @ManyToOne
    @JoinColumn(name = "transactionID", referencedColumnName = "transactionID", nullable = true)
    private BorrowingTransactionEntity transaction;

    @Column(nullable = false)
    private double fineAmount;

    @Column(nullable = false)
    private boolean paidStatus = false;
}
