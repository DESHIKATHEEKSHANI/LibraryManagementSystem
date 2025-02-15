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
@Table(name = "member")
public class MemberEntity {

    @Id
    @Column(unique = true, nullable = false)
    private String memberID; // Custom memberID (e.g., 'LM001', 'LM002', etc.)

    @Column(nullable = false)
    private String name; // Member's name

    @Column(nullable = false)
    private String contactInfo; // Contact information for the member

    @Column(nullable = false)
    private LocalDate membershipDate; // Date when the membership started

    @Column(nullable = false)
    private String status; // Member's status (e.g., active, inactive, etc.)
}
