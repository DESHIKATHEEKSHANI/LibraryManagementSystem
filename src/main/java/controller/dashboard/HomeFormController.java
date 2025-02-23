package controller.dashboard;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import service.custom.impl.BookServiceImpl;
import service.custom.impl.BorrowingTransactionServiceImpl;
import service.custom.impl.MemberServiceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HomeFormController {

    @FXML
    private Label txtAdminName;

    @FXML
    private Label txtCountBooks;

    @FXML
    private Label txtCountBorrowedBooks;

    @FXML
    private Label txtCountMembers;

    @FXML
    private TextField txtSearchBook;

    @FXML
    public void initialize() {
        updateCounts();
    }

    private void updateCounts() {
        BookServiceImpl bookService = new BookServiceImpl();
        MemberServiceImpl memberService = new MemberServiceImpl();
        BorrowingTransactionServiceImpl transactionService = new BorrowingTransactionServiceImpl();

        int countBooks = bookService.getCountBooks();
        int countBorrowedBooks = transactionService.getCountBorrowedBooks();
        int countMembers = memberService.getCountMembers();

        txtCountBooks.setText(String.valueOf(countBooks));
        txtCountBorrowedBooks.setText(String.valueOf(countBorrowedBooks));
        txtCountMembers.setText(String.valueOf(countMembers));
    }

    public void setAdminName(String adminName) {
        txtAdminName.setText("Welcome, " + adminName);
    }

}
