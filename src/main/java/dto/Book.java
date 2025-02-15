package dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private String bookID;           // Unique identifier for the book (bookID)
    private String isbn;             // ISBN of the book
    private String title;            // Title of the book
    private String author;           // Author of the book
    private String category;         // Category of the book (could be a foreign key)
    private String availabilityStatus; // Availability status (Available/Borrowed)
}
