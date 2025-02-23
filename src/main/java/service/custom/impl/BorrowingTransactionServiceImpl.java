package service.custom.impl;
import dto.BorrowingTransaction;
import entity.BookEntity;
import entity.BorrowingTransactionEntity;
import entity.MemberEntity;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.BookDao;
import repository.custom.BorrowingTransactionDao;
import repository.custom.MemberDao;
import service.custom.BorrowingTransactionService;
import util.DaoType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BorrowingTransactionServiceImpl implements BorrowingTransactionService {

    private final BorrowingTransactionDao dao = DaoFactory.getInstance().getDaoType(DaoType.BORROWING_TRANSACTION);
    private final ModelMapper modelMapper = new ModelMapper();
    private final BookDao bookDao = DaoFactory.getInstance().getDaoType(DaoType.BOOK);
    private final MemberDao memberDao = DaoFactory.getInstance().getDaoType(DaoType.MEMBER);

    @Override
    public boolean issueBook(BorrowingTransaction transaction) {
        try {
            BookEntity selectedBook = bookDao.search(transaction.getBookID());
            MemberEntity selectedMember = memberDao.search(transaction.getMemberID());

            if (selectedBook != null && "Available".equals(selectedBook.getAvailabilityStatus()) &&
                    selectedMember != null) {

                selectedBook.setAvailabilityStatus("Issued");
                bookDao.update(selectedBook);

                BorrowingTransactionEntity transactionEntity = mapToEntity(transaction);

                dao.save(transactionEntity);

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addTransaction(BorrowingTransaction transaction) {
        try {
            BorrowingTransactionEntity transactionEntity = mapToEntity(transaction);
            return dao.save(transactionEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String generateNewTransactionId() {
        if (dao == null) {
            throw new IllegalStateException("DAO is not initialized. Please check DaoFactory setup.");
        }

        String lastId = dao.getLastTransactionId();

        if (lastId == null || !lastId.matches("^BT\\d{3}$")) {
            return "BT001";
        }

        try {
            int num = Integer.parseInt(lastId.substring(2)) + 1;
            return String.format("BT%03d", num);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Failed to parse last transaction ID", e);
        }
    }

    @Override
    public int getBorrowedBooksCountByMember(String memberId) {
        try {
            return dao.getCountByMemberAndReturnDateNull(memberId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<BorrowingTransaction> getAllTransactions() {
        return dao.getAll().stream()
                .map(entity -> modelMapper.map(entity, BorrowingTransaction.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateTransaction(BorrowingTransaction transaction) {
        try {
            BorrowingTransactionEntity transactionEntity = mapToEntity(transaction);
            return dao.update(transactionEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteTransaction(String transactionId) {
        return dao.delete(transactionId);
    }

    private BorrowingTransactionEntity mapToEntity(BorrowingTransaction dto) {
        BorrowingTransactionEntity entity = modelMapper.map(dto, BorrowingTransactionEntity.class);
        if (entity.getStatus() == null) {
            entity.setStatus("Borrowed");
        }
        if (entity.getReturnDate() == null) {
            entity.setReturnDate(null);
        }
        return entity;
    }
    @Override
    public BorrowingTransaction getTransactionById(String transactionID) {
        BorrowingTransactionEntity transactionEntity = dao.search(transactionID);
        if (transactionEntity != null) {
            return modelMapper.map(transactionEntity, BorrowingTransaction.class);
        }
        return null;
    }

    @Override
    public int getCountBorrowedBooks() {
        return dao.getCountBorrowedBooks();
    }

    @Override
    public Map<String, Integer> getBorrowingStatistics() {
        return dao.getBorrowingStatistics();
    }


}
