package dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Fine {
    private String fineID;
    private String memberID;
    private String transactionID;
    private double fineAmount;
    private boolean paidStatus;
}
