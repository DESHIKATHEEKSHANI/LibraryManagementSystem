package repository.custom;

import entity.AdminEntity;
import repository.CrudDao;

public interface AdminDao extends CrudDao<AdminEntity> {
    AdminEntity findByEmail(String email);
}
