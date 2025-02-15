package controller.book;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import dto.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import service.custom.BookService;
import service.custom.CategorySevice;
import service.custom.impl.BookServiceImpl;
import service.custom.impl.CategoryServiceImpl;

import java.time.LocalDate;
import java.util.List;

public class BookFormController {

    private final BookService bookService = new BookServiceImpl();
    private final CategorySevice categoryService = new CategoryServiceImpl();

    @FXML
    private TableColumn colAuthor;
    @FXML
    private TableColumn colAvailability;
    @FXML
    private TableColumn colGenre;
    @FXML
    private TableColumn colID;
    @FXML
    private TableColumn colIsbn;
    @FXML
    private TableColumn colTitle;

    @FXML
    private JFXComboBox<String> comBoxGenre;
    @FXML
    private TableView tblBooks;

    @FXML
    private JFXTextField txtAuthor;
    @FXML
    private JFXTextField txtBookID;
    @FXML
    private JFXTextField txtIsbn;
    @FXML
    private JFXTextField txtTitle;

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (validateInput()) {
            String bookID = generateNewBookId();
            txtBookID.setText(bookID);

            Book book = new Book(
                    bookID,                // Book ID
                    txtIsbn.getText(),     // ISBN
                    txtTitle.getText(),    // Title
                    txtAuthor.getText(),   // Author
                    comBoxGenre.getValue(),// Category (from combobox)
                    "Available"            // Availability status (default to Available)
            );


            boolean result = bookService.addBook(book);
            if (result) {
                showAlert("Success", "Book Added Successfully!", Alert.AlertType.INFORMATION);
                loadTable();
                clearFields();
            } else {
                showAlert("Error", "Failed to add book", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        boolean result = bookService.deleteBook(txtBookID.getText());
        if (result) {
            showAlert("Success", "Book Deleted Successfully!", Alert.AlertType.INFORMATION);
            loadTable();
            clearFields();
        } else {
            showAlert("Error", "Failed to delete book", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        Book book = bookService.searchBook(txtBookID.getText());
        if (book != null) {
            setTextToValues(book);
        } else {
            showAlert("Error", "Book not found", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (validateInput()) {
            Book book = new Book(
                    txtBookID.getText(),
                    txtIsbn.getText(),
                    txtTitle.getText(),
                    txtAuthor.getText(),
                    comBoxGenre.getValue(),
                    "Available"// Default to Available
            );

            boolean result = bookService.updateBook(book);
            if (result) {
                showAlert("Success", "Book Updated Successfully!", Alert.AlertType.INFORMATION);
                loadTable();
                clearFields();
            } else {
                showAlert("Error", "Failed to update book", Alert.AlertType.ERROR);
            }
        }
    }

    private void loadTable() {
        List<Book> books = bookService.getAllBooks();

        if (books != null && !books.isEmpty()) {
            ObservableList<Book> observableBooks = FXCollections.observableList(books);
            tblBooks.setItems(observableBooks);
        } else {
            tblBooks.getItems().clear();
        }
    }

    private void setTextToValues(Book book) {
        txtBookID.setText(book.getBookID());
        txtIsbn.setText(book.getIsbn());
        txtTitle.setText(book.getTitle());
        txtAuthor.setText(book.getAuthor());
        comBoxGenre.setValue(book.getCategory());
    }

    private void clearFields() {
        txtBookID.setText(generateNewBookId());
        txtIsbn.clear();
        txtTitle.clear();
        txtAuthor.clear();
        comBoxGenre.setValue(null);
    }

    private String generateNewBookId() {
        return bookService.generateNewBookId();
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean validateInput() {
        if (txtTitle.getText().trim().isEmpty() || txtAuthor.getText().trim().isEmpty() || comBoxGenre.getValue() == null) {
            showAlert("Validation Error", "Please fill in all fields!", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    @FXML
    public void initialize() {
        // Define columns without generics
        colID.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colAvailability.setCellValueFactory(new PropertyValueFactory<>("availabilityStatus"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("category"));

        loadTable();

        tblBooks.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                setTextToValues((Book) newValue);
            }
        });

        // Setup for the book form
        txtBookID.setText(generateNewBookId());
        List<String> categories = categoryService.getAllCategories();
        comBoxGenre.setItems(FXCollections.observableArrayList(categories));
    }
}
