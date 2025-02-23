package service.custom;

import dto.Fine;
import service.SuperService;

import java.util.Optional;

public interface FineService extends SuperService {
    String generateNewFineId();

    void recordFine(Fine fine);

    Optional<Fine> getFineByTransactionID(String transactionID);
}
