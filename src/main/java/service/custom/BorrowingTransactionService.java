package service.custom;

import dto.BorrowingTransaction;
import service.SuperService;

import java.util.List;
import java.util.Map;

public interface BorrowingTransactionService extends SuperService {
    boolean issueBook(BorrowingTransaction transaction);
    boolean addTransaction(BorrowingTransaction transaction);
    String generateNewTransactionId();
    List<BorrowingTransaction> getAllTransactions();
    boolean updateTransaction(BorrowingTransaction transaction);
    boolean deleteTransaction(String transactionId);
    int getBorrowedBooksCountByMember(String memberId);

    BorrowingTransaction getTransactionById(String transactionID);

    int getCountBorrowedBooks();

    Map<String, Integer> getBorrowingStatistics();
}
