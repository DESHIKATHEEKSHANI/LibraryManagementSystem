package service.custom.impl;

import dto.Member;
import entity.MemberEntity;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.MemberDao;
import service.custom.MemberService;
import util.DaoType;

import java.util.List;
import java.util.stream.Collectors;

public class MemberServiceImpl implements MemberService {

    private final MemberDao dao = DaoFactory.getInstance().getDaoType(DaoType.MEMBER);
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public boolean addMember(Member member) {
        try {
            MemberEntity memberEntity = mapToEntity(member);
            return dao.save(memberEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String generateNewMemberId() {
        String lastId = dao.getLastMemberId();

        if (lastId == null || !lastId.matches("LM\\d{3}")) {
            return "LM001"; // Default ID
        }

        try {
            int num = Integer.parseInt(lastId.substring(2)) + 1;
            return String.format("LM%03d", num);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Failed to parse last member ID", e);
        }
    }

    @Override
    public boolean updateMember(Member member) {
        try {
            MemberEntity memberEntity = mapToEntity(member);
            return dao.update(memberEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteMember(String memberId) {
        return dao.delete(memberId);
    }

    @Override
    public Member searchMember(String memberId) {
        MemberEntity entity = dao.search(memberId);
        return entity != null ? modelMapper.map(entity, Member.class) : null;
    }

    @Override
    public List<Member> getAllMembers() {
        return dao.getAll().stream()
                .map(entity -> modelMapper.map(entity, Member.class))
                .collect(Collectors.toList());
    }

    // Helper method to map from Member DTO to MemberEntity
    private MemberEntity mapToEntity(Member member) {
        return modelMapper.map(member, MemberEntity.class);
    }
}
