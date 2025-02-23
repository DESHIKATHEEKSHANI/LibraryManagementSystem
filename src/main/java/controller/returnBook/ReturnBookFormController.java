package controller.returnBook;

import com.jfoenix.controls.JFXTextField;
import dto.Book;
import dto.BorrowingTransaction;
import dto.Fine;
import entity.BookEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import service.custom.BookService;
import service.custom.BorrowingTransactionService;
import service.custom.FineService;
import service.custom.impl.BookServiceImpl;
import service.custom.impl.BorrowingTransactionServiceImpl;
import service.custom.impl.FineServiceImpl;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReturnBookFormController {

    private final BorrowingTransactionService transactionService = new BorrowingTransactionServiceImpl();
    private final FineService fineService = new FineServiceImpl();

    @FXML
    private AnchorPane paneLoadContent;

    @FXML
    private Label lblBookID, lblBorrowDate, lblDueDate, lblFineAmount, lblFineID, lblIsOverdurornot, lblMemberID, lblOverDueDays, lblTransactionID;

    @FXML
    private JFXTextField txtBalance, txtYourPayment;

    @FXML
    private DatePicker txtReturnDate;

    @FXML
    private TextField txtTransactionID;

    @FXML
    private Button btnReturnBook;

    private double fineAmount = 0;
    private int overdueDays = 0;
    private String fineID = null;

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String transactionID = txtTransactionID.getText();
        BorrowingTransaction transaction = transactionService.getTransactionById(transactionID);

        if (transaction == null) {
            showAlert("Error", "Transaction not found!", Alert.AlertType.ERROR);
            return;
        }

        lblTransactionID.setText(transaction.getTransactionID());
        lblMemberID.setText(transaction.getMemberID());
        lblBookID.setText(transaction.getBookID());
        lblBorrowDate.setText(transaction.getBorrowDate().toString());
        lblDueDate.setText(transaction.getDueDate().toString());

        if ("Returned".equals(transaction.getStatus())) {
            showAlert("Error", "This book has already been returned!", Alert.AlertType.ERROR);
            clearFields();
            btnReturnBook.setDisable(true);
            return;
        }

        txtReturnDate.setValue(LocalDate.now());

        calculateOverdue(transaction.getDueDate());
    }


    private void calculateOverdue(LocalDate dueDate) {
        LocalDate today = LocalDate.now();
        overdueDays = (int) ChronoUnit.DAYS.between(dueDate, today);

        if (overdueDays > 0) {
            fineAmount = overdueDays * 30;
            lblIsOverdurornot.setText("Overdue");
            lblOverDueDays.setText(overdueDays + " days");
            lblFineAmount.setText("Rs. " + fineAmount);
            fineID = generateFineID();
            lblFineID.setText(fineID);

            showAlert("Warning", "Book is overdue! A fine of Rs. " + fineAmount + " has been applied.", Alert.AlertType.WARNING);
            btnReturnBook.setDisable(true);
        } else {
            lblIsOverdurornot.setText("Not Overdue");
            lblOverDueDays.setText("0");
            lblFineAmount.setText("Rs. 0");
            fineAmount = 0;
            fineID = null;
            btnReturnBook.setDisable(false);
        }
    }

    private String generateFineID() {
        return fineService.generateNewFineId();
    }

    @FXML
    void btnPaymentDoneOnAction(ActionEvent event) {
        double payment = Double.parseDouble(txtYourPayment.getText());
        double balance = fineAmount - payment;

        if (balance > 0) {
            showAlert("Warning", "Payment incomplete! Remaining balance: Rs. " + balance, Alert.AlertType.WARNING);
            txtBalance.setText("Rs. " + balance);
            btnReturnBook.setDisable(true);
        } else {
            txtBalance.setText("Rs. 0");
            showAlert("Success", "Payment completed! You can now return the book.", Alert.AlertType.INFORMATION);
            btnReturnBook.setDisable(false);
            updateFineRecord();
        }
    }


    private void updateFineRecord() {
        if (fineID != null) {
            Fine fine = new Fine(fineID, lblMemberID.getText(), lblTransactionID.getText(), fineAmount, true);
            fineService.recordFine(fine);
        }
    }

    @FXML
    void btnReturnBookOnAction(ActionEvent event) {
        BorrowingTransaction transaction = transactionService.getTransactionById(lblTransactionID.getText());

        if (transaction == null) {
            showAlert("Error", "Transaction not found!", Alert.AlertType.ERROR);
            return;
        }

        transaction.setReturnDate(LocalDate.now());
        transaction.setStatus("Returned");

        boolean success = transactionService.updateTransaction(transaction);

        if (success) {
            updateBookAvailability(transaction.getBookID());

            showAlert("Success", "Book returned successfully!", Alert.AlertType.INFORMATION);
            clearFields();
        } else {
            showAlert("Error", "Failed to return book!", Alert.AlertType.ERROR);
        }
    }

    private void updateBookAvailability(String bookID) {
        BookService bookService = new BookServiceImpl();
        Book book = bookService.getBookById(bookID);

        if (book != null) {
            book.setAvailabilityStatus("Available");
            bookService.updateBook(book);
        } else {
            showAlert("Error", "Book not found in the system!", Alert.AlertType.ERROR);
        }
    }


    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        txtTransactionID.clear();
        lblTransactionID.setText("");
        lblMemberID.setText("");
        lblBookID.setText("");
        lblBorrowDate.setText("");
        lblDueDate.setText("");
        lblFineID.setText("");
        lblFineAmount.setText("");
        lblIsOverdurornot.setText("");
        lblOverDueDays.setText("");
        txtReturnDate.setValue(null);
        txtYourPayment.clear();
        txtBalance.clear();
        btnReturnBook.setDisable(true);
    }

    @FXML
    void btnTrackOverDueBooksOnAction(ActionEvent event) throws IOException {
        loadContent("/view/BorrowingHistoryForm.fxml");
    }

    @FXML
    void initialize() {
        txtYourPayment.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                double payment = Double.parseDouble(newValue);
                double balance = fineAmount - payment;
                txtBalance.setText("Rs. " + balance);
                btnReturnBook.setDisable(balance > 0);
            } catch (NumberFormatException e) {
                txtBalance.setText("Rs. 0");
            }
        });
    }

    private void loadContent(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            paneLoadContent.getChildren().clear();
            paneLoadContent.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
