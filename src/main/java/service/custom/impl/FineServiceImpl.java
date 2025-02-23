package service.custom.impl;

import dto.Fine;
import entity.FineEntity;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.FineDao;
import service.custom.FineService;
import util.DaoType;

import java.util.Optional;

public class FineServiceImpl implements FineService {

    private final FineDao fineDao = DaoFactory.getInstance().getDaoType(DaoType.FINE);
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public String generateNewFineId() {
        String lastFineId = fineDao.getLastFineId();

        if (lastFineId == null || !lastFineId.matches("^FP\\d{3}$")) {
            return "FP001";
        }

        try {
            int num = Integer.parseInt(lastFineId.substring(2)) + 1;

            return String.format("FP%03d", num);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Failed to parse last fine ID", e);
        }
    }


    @Override
    public void recordFine(Fine fine) {
        FineEntity fineEntity = modelMapper.map(fine, FineEntity.class);
        fineDao.save(fineEntity);
    }

    @Override
    public Optional<Fine> getFineByTransactionID(String transactionID) {
        Optional<FineEntity> fineEntityOptional = fineDao.findByTransactionID(transactionID);
        return fineEntityOptional.map(fineEntity -> modelMapper.map(fineEntity, Fine.class));
    }
}
