package com.ems.api.repository;

import com.ems.api.entity.TeamContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamContactRepo extends JpaRepository<TeamContact, Long> {
    TeamContact findByMemberIdAndTeamId(Long memberId,Long teamId);
    List<TeamContact> findAllByTeamId(Long teamId);
    List<TeamContact> findAllByMemberId(Long memberId);
}
