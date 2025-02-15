package service.custom;

import dto.Member;
import service.SuperService;

import java.util.List;

public interface MemberService extends SuperService {
    boolean addMember(Member member);
    boolean updateMember(Member member);
    boolean deleteMember(String memberId);
    Member searchMember(String memberId);
    List<Member> getAllMembers();
    String generateNewMemberId();
}
