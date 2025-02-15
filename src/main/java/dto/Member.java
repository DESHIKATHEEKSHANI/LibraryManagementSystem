package dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    private String memberID;
    private String name;
    private String contactInfo;
    private LocalDate membershipDate;
    private String status;
}
