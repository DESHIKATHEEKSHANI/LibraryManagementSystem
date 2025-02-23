package dto;

import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BorrowingTransaction {
    private String transactionID;
    private String memberID;
    private String bookID;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private LocalDate dueDate;
    private String status;

}
