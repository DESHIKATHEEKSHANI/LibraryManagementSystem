package repository.custom;

import entity.BookEntity;
import entity.BorrowingTransactionEntity;
import repository.CrudDao;

import java.util.Map;

public interface BorrowingTransactionDao extends CrudDao<BorrowingTransactionEntity> {
    String getLastTransactionId();
    int getCountByMemberAndReturnDateNull(String memberId);

    int getCountBorrowedBooks();

    Map<String, Integer> getBorrowingStatistics();
}
