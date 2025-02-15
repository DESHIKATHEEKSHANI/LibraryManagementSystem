package repository.custom;

import entity.MemberEntity;
import repository.CrudDao;

import java.util.List;

public interface MemberDao extends CrudDao<MemberEntity> {
    String getLastMemberId();

}
