package repository;

import repository.custom.impl.*;
import service.custom.impl.BorrowingTransactionServiceImpl;
import util.DaoType;

public class DaoFactory {
    private static DaoFactory instance;
    private DaoFactory(){}
    public static DaoFactory getInstance() {
        return instance==null?instance=new DaoFactory():instance;
    }

    public <T extends SuperDao> T getDaoType(DaoType type){
        switch (type){
            case ADMIN: return (T) new AdminDaoImpl();
            case MEMBER:return (T) new MemberDaoImpl();
            case BOOK:return (T) new BookDaoImpl();
            case CATEGORY:return (T) new CategoryDaoImpl();
            case BORROWING_TRANSACTION:return (T) new BorrowingTransactionDaoImpl();
            case FINE: return(T) new FineDaoImpl();
        }
        return null;
    }
}
