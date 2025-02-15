package repository;

import repository.custom.impl.AdminDaoImpl;
import repository.custom.impl.BookDaoImpl;
import repository.custom.impl.CategoryDaoImpl;
import repository.custom.impl.MemberDaoImpl;
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
        }
        return null;
    }
}
