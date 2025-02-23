package repository.custom;

import entity.FineEntity;
import repository.CrudDao;

import java.util.Optional;

public interface FineDao extends CrudDao<FineEntity> {
    String getLastFineId();

    Optional<FineEntity> findByTransactionID(String transactionID);
}
