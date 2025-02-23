package dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private String bookID;
    private String isbn;
    private String title;
    private String author;
    private String category;
    private String availabilityStatus;
}
