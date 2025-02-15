package entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookEntity {

    @Id
    @Column(unique = true, nullable = false)
    private String bookID;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = true)
    private String category;

    @Column(nullable = false)
    private String availabilityStatus = "Available";


}

