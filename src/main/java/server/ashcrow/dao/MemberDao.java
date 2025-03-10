package server.ashcrow.dao;

import org.apache.ibatis.annotations.Mapper;

import server.ashcrow.dto.MemberDto;

@Mapper
public interface MemberDao {

    int signup(MemberDto memberDto);

    int findExistingMemberById(String memberId);

    MemberDto retrieveMemberById(String memberId);

    void updateLastAccessDateTime(MemberDto loginUser);

}
