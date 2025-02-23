package controller.transaction;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import dto.BorrowingTransaction;
import dto.Book;
import dto.Member;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import service.custom.BorrowingTransactionService;
import service.custom.BookService;
import service.custom.MemberService;
import service.custom.impl.BorrowingTransactionServiceImpl;
import service.custom.impl.BookServiceImpl;
import service.custom.impl.MemberServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionFormController {

    private final BorrowingTransactionService transactionService = new BorrowingTransactionServiceImpl();
    private final BookService bookService = new BookServiceImpl();
    private final MemberService memberService = new MemberServiceImpl();

    @FXML
    private JFXComboBox<String> comboBoxBook;

    @FXML
    private JFXComboBox<String> comboBoxMember;

    @FXML
    private DatePicker dateDue;

    @FXML
    private DatePicker dateIssue;

    @FXML
    private Label lblBookID;

    @FXML
    private Label lblDueDate;

    @FXML
    private Label lblIssueDate;

    @FXML
    private Label lblMemberID;

    @FXML
    private Label lblTransactionId;

    @FXML
    private JFXTextField txtTransactionID;

    @FXML
    public void initialize() {
        loadBookNames();
        loadMemberNames();
        generateTransactionID();
        setDefaultDates();
    }

    private void loadBookNames() {
        List<Book> books = bookService.getAllBooks();
        ObservableList<String> bookNames = FXCollections.observableArrayList(
                books.stream().map(Book::getTitle).collect(Collectors.toList())
        );
        comboBoxBook.setItems(bookNames);
    }

    private void loadMemberNames() {
        List<Member> members = memberService.getAllMembers();
        ObservableList<String> memberNames = FXCollections.observableArrayList(
                members.stream().map(Member::getName).collect(Collectors.toList())
        );
        comboBoxMember.setItems(memberNames);
    }

    private void generateTransactionID() {
        txtTransactionID.setText(transactionService.generateNewTransactionId());
    }
    private void setDefaultDates() {
        LocalDate today = LocalDate.now();
        dateIssue.setValue(today);
        dateDue.setValue(today.plusDays(7));
    }

    @FXML
    void btnIssueBookOnAction(ActionEvent event) {
        String transactionID = txtTransactionID.getText();
        String bookName = comboBoxBook.getValue();
        String memberName = comboBoxMember.getValue();

        if (bookName == null || memberName == null) {
            showAlert("Error", "Please select a book and a member!", Alert.AlertType.WARNING);
            return;
        }

        Book selectedBook = bookService.getBookByTitle(bookName);
        Member selectedMember = memberService.getMemberByName(memberName);

        if (selectedBook == null || selectedMember == null) {
            showAlert("Error", "Invalid book or member selection!", Alert.AlertType.ERROR);
            return;
        }

        int borrowedBooksCount = transactionService.getBorrowedBooksCountByMember(selectedMember.getMemberID());

        if (borrowedBooksCount >= 3) {
            showAlert("Error", "Member cannot borrow more than 3 books at a time!", Alert.AlertType.WARNING);
            clearTextFields();
            return;
        }

        LocalDate issueDate = dateIssue.getValue();
        LocalDate dueDate = dateDue.getValue();

        BorrowingTransaction transaction = new BorrowingTransaction(
                transactionID,
                selectedMember.getMemberID(),
                selectedBook.getBookID(),
                issueDate,
                null,
                dueDate,
                "Borrowed"
        );

        boolean result = transactionService.issueBook(transaction);
        if (result) {
            lblTransactionId.setText(transactionID);
            lblBookID.setText(selectedBook.getBookID());
            lblMemberID.setText(selectedMember.getMemberID());
            lblIssueDate.setText(issueDate.toString());
            lblDueDate.setText(dueDate.toString());

            showAlert("Success", "Book Issued Successfully!", Alert.AlertType.INFORMATION);
            generateTransactionID();
            clearTextFields();
        } else {
            showAlert("Error", "Failed to issue book!", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void clearTextFields() {
        comboBoxBook.getSelectionModel().clearSelection();
        comboBoxMember.getSelectionModel().clearSelection();

        dateIssue.setValue(LocalDate.now());
        dateDue.setValue(LocalDate.now().plusDays(7));

        generateTransactionID();
    }

    public void btnDoneOnAction(ActionEvent actionEvent) {
        clearLabelFields();
    }

    private void clearLabelFields() {
        lblBookID.setText("");
        lblMemberID.setText("");
        lblTransactionId.setText("");
        lblIssueDate.setText("");
        lblDueDate.setText("");
    }

}
