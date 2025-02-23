package controller.transaction;

import dto.BorrowingTransaction;
import dto.Fine;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableRow;
import javafx.beans.property.SimpleStringProperty;
import service.custom.BorrowingTransactionService;
import service.custom.FineService;
import service.custom.impl.BorrowingTransactionServiceImpl;
import service.custom.impl.FineServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class BorrowingHistoryFormController {

    @FXML
    private TableView<BorrowingTransaction> tblBorrowingHistory;

    @FXML
    private TableColumn<BorrowingTransaction, String> colTransactionID;

    @FXML
    private TableColumn<BorrowingTransaction, String> colMemberID;

    @FXML
    private TableColumn<BorrowingTransaction, String> colBookID;

    @FXML
    private TableColumn<BorrowingTransaction, LocalDate> colBorrowDate;

    @FXML
    private TableColumn<BorrowingTransaction, LocalDate> colDueDate;

    @FXML
    private TableColumn<BorrowingTransaction, LocalDate> colReturnDate;

    @FXML
    private TableColumn<BorrowingTransaction, String> colFines;

    private final BorrowingTransactionService transactionService = new BorrowingTransactionServiceImpl();
    private final FineService fineService = new FineServiceImpl();

    @FXML
    public void initialize() {
        setupColumns();
        loadBorrowingHistory();
        applyRowColors();
    }

    private void setupColumns() {
        colTransactionID.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
        colMemberID.setCellValueFactory(new PropertyValueFactory<>("memberID"));
        colBookID.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        colBorrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        colDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        colFines.setCellValueFactory(cellData -> {
            BorrowingTransaction transaction = cellData.getValue();
            return new SimpleStringProperty(determineFineStatus(transaction));
        });
    }

    private void loadBorrowingHistory() {
        List<BorrowingTransaction> transactions = transactionService.getAllTransactions();
        tblBorrowingHistory.getItems().setAll(transactions);
    }

    private String determineFineStatus(BorrowingTransaction transaction) {
        LocalDate today = LocalDate.now();
        Optional<Fine> fine = fineService.getFineByTransactionID(transaction.getTransactionID());

        if (transaction.getReturnDate() != null) {
            if (fine.isPresent()) {
                return fine.get().isPaidStatus() ? "Returned - Fine Paid" : "Returned - Fine Unpaid";
            }
            return "Returned - No Fine";
        } else if (transaction.getDueDate().isBefore(today)) {
            return fine.isPresent() ? "Not Returned - Fine Due" : "Not Returned - Overdue";
        } else {
            return "Not Due Yet";
        }
    }

    private void applyRowColors() {
        tblBorrowingHistory.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(BorrowingTransaction transaction, boolean empty) {
                super.updateItem(transaction, empty);
                if (transaction == null || empty) {
                    setStyle("");
                } else {
                    LocalDate today = LocalDate.now();
                    Optional<Fine> fine = fineService.getFineByTransactionID(transaction.getTransactionID());

                    if (transaction.getReturnDate() != null) {
                        if (fine.isPresent() && !fine.get().isPaidStatus()) {
                            setStyle("-fx-background-color: #FFA500;");
                        } else {
                            setStyle("-fx-background-color: #bae5bb;");
                        }
                    } else if (transaction.getDueDate().isBefore(today)) {
                        setStyle("-fx-background-color: #de4741;");
                    } else {
                        setStyle("-fx-background-color: #f1a69a;");
                    }
                }
            }
        });
    }
}
