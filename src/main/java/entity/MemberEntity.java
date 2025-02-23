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
    private String memberID;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String contactInfo;

    @Column(nullable = false)
    private LocalDate membershipDate;

    @Column(nullable = false)
    private String status;
}
