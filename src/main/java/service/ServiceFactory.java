package service;

import service.custom.impl.*;
import util.ServiceType;

public class ServiceFactory {
    private static ServiceFactory instance;

    private ServiceFactory(){}

    public static ServiceFactory getInstance(){
        return instance==null?instance=new ServiceFactory():instance;
    }

    public <T extends SuperService>T getServiceType(ServiceType serviceType){

        switch (serviceType){
            case ADMIN:return (T) new AdminServiceImpl();
            case MEMBER:return (T) new MemberServiceImpl();
            case BOOK:return (T) new BookServiceImpl();
            case CATEGORY:return (T) new CategoryServiceImpl();
            case BORROWING_TRANSACTION: return(T) new BorrowingTransactionServiceImpl();
            case FINE: return(T) new FineServiceImpl();
        }
        return null;
    }

}
