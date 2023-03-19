package com.ems.api.repository;

import com.ems.api.entity.Contact;
import com.ems.api.util.Constant;
import com.ems.api.util.Constant.TypeConstant.ContactType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepo extends JpaRepository<Contact, Long> {

    @Query(value = "select distinct c  from UserContact uc join uc.otherContact c WHERE " +
            " uc.userId = :userContactId " +
            "AND (" +
            "(upper(coalesce(c.email,'')) like upper(concat('%', :keyword,'%')))" +
            " OR (upper(coalesce(c.phone,'')) like upper(concat('%', :keyword,'%')))" +
            " OR (upper(coalesce(c.mobile,'')) like upper(concat('%', :keyword,'%')))" +
            " OR (upper(coalesce(c.firstName,'')) like upper(concat('%', :keyword,'%')))" +
            " OR :keyword IS NULL" +
            ") " +
            " AND" +
            "(" +
            " (c.id in (select c1.id from Contact c1 join c1.joinedTeams tc  WHERE tc.teamId = :teamId) or :teamId is null) " +
            " AND (c.id in (select c1.id from Contact c1 join c1.joinedEvents ec where ec.eventId = :eventId) or :eventId is null) " +
            ")" +
            "AND c.emailAccount IS NOT NULL",
            countQuery = "select count(distinct c) from UserContact uc join uc.otherContact c WHERE " +
                    " uc.userId = :userContactId " +
                    "AND (" +
                    "(upper(coalesce(c.email,'')) like upper(concat('%', :keyword,'%')))" +
                    " OR (upper(coalesce(c.phone,'')) like upper(concat('%', :keyword,'%')))" +
                    " OR (upper(coalesce(c.mobile,'')) like upper(concat('%', :keyword,'%')))" +
                    " OR (upper(coalesce(c.firstName,'')) like upper(concat('%', :keyword,'%')))" +
                    " OR :keyword IS NULL" +
                    ") " +
                    " AND" +
                    "(" +
                    " (c.id in (select c1.id from Contact c1 join c1.joinedTeams tc  WHERE tc.teamId = :teamId) or :teamId is null) " +
                    " AND (c.id in (select c1.id from Contact c1 join c1.joinedEvents ec where ec.eventId = :eventId) or :eventId is null) " +
                    ")" +
                    "AND c.emailAccount IS NOT NULL")
    Page<Contact> searchAdded(@Param("keyword") String keyword,
                              @Param("teamId") Long teamId,
                              @Param("eventId") Long eventId,
                              @Param("userContactId") Long userContactId,
                              Pageable pageable);

    @Query(value = "select c from Contact c " +
            "where c.id<>:userContactId " +
            "AND (" +
            "(upper(coalesce(c.email,'')) like upper(concat('%', :keyword,'%')))" +
            " OR (upper(coalesce(c.phone,'')) like upper(concat('%', :keyword,'%')))" +
            " OR (upper(coalesce(c.mobile,'')) like upper(concat('%', :keyword,'%')))" +
            " OR (upper(coalesce(c.firstName,'')) like upper(concat('%', :keyword,'%')))" +
            " OR :keyword IS NULL" +
            ") " +
            "AND c.emailAccount IS NOT NULL " +
            "AND c.id not in(select uc.otherContactId from UserContact uc where uc.userId=:userContactId)",
            countQuery = "select count(c) from Contact c " +
                    "where c.id<>:userContactId " +
                    "AND (" +
                    "(upper(coalesce(c.email,'')) like upper(concat('%', :keyword,'%')))" +
                    " OR (upper(coalesce(c.phone,'')) like upper(concat('%', :keyword,'%')))" +
                    " OR (upper(coalesce(c.mobile,'')) like upper(concat('%', :keyword,'%')))" +
                    " OR (upper(coalesce(c.firstName,'')) like upper(concat('%', :keyword,'%')))" +
                    " OR :keyword IS NULL" +
                    ") " +
                    "AND c.emailAccount IS NOT NULL " +
                    "AND c.id not in(select uc.otherContactId from UserContact uc where uc.userId=:userContactId)")
    Page<Contact> searchNotAdded(@Param("keyword") String keyword,
                                 @Param("userContactId") Long userContactId,
                                 Pageable pageable);

    @Query(value = "select c from Contact c where " +
            "((upper(coalesce(c.email,'')) like upper(concat('%', :keyword,'%')))" +
            " OR (upper(coalesce(c.phone,'')) like upper(concat('%', :keyword,'%')))" +
            " OR (upper(coalesce(c.mobile,'')) like upper(concat('%', :keyword,'%')))" +
            " OR (upper(coalesce(c.firstName,'')) like upper(concat('%', :keyword,'%')))" +
            " OR :keyword IS NULL) " +
            "AND c.account IS NOT NULL", countQuery = "select c from Contact c where " +
            "((upper(coalesce(c.email,'')) like upper(concat('%', :keyword,'%')))" +
            " OR (upper(coalesce(c.phone,'')) like upper(concat('%', :keyword,'%')))" +
            " OR (upper(coalesce(c.mobile,'')) like upper(concat('%', :keyword,'%')))" +
            " OR (upper(coalesce(c.firstName,'')) like upper(concat('%', :keyword,'%')))" +
            " OR :keyword IS NULL) " +
            "AND c.account IS NOT NULL")
    Page<Contact> searchContactIsUser(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "select c from Contact c where " +
            "c.account.email = :email")
    Contact findByAccountEmail(@Param("email") String email);

    List<Contact> findAllByEventId(Long eventId);

    @Query(value = "select c from Contact c where c.eventId=:eventId " +
            "and 1 in (select 1 from c.teamMembers tm where tm.member.emailAccount=:email)")
    List<Contact> findAllByEventIdAndContainUser(@Param("eventId") Long eventId,
                                                 @Param("email") String userEmail);

    Contact findByIdAndContactType(Long id, ContactType contactType);


    @Query(value = "select c from Contact c where " +
            " c.id <> :userContactId " +
            "AND c.contactType = :contactType " +
            "AND c.emailAccount IS not null " +
            "AND (" +
            "(upper(coalesce(c.email,'')) like upper(concat('%', :keyword,'%')))" +
            " OR (upper(coalesce(c.phone,'')) like upper(concat('%', :keyword,'%')))" +
            " OR (upper(coalesce(c.mobile,'')) like upper(concat('%', :keyword,'%')))" +
            " OR (upper(coalesce(c.firstName,'')) like upper(concat('%', :keyword,'%')))" +
            " OR :keyword IS NULL" +
            ") ")
    List<Contact> searchUserToAddToEvent(@Param("keyword") String keyword,
                                         @Param("userContactId") Long userContactId,
                                         @Param("contactType") ContactType contactType

    );

    @Query(value = "select c from EventContact ec join ec.member c " +
            " where c.emailAccount<>:userEmail " +
            "AND ec.eventId=:eventId " +
            "AND c.contactType=:contactType " +
            "AND ec.status=:status " +
            "AND ec.memberId not in (select tc.memberId from TeamContact tc where tc.teamId=:teamId) " +
            "AND (" +
            "(upper(coalesce(c.email,'')) like upper(concat('%', :keyword,'%')))" +
            " OR (upper(coalesce(c.phone,'')) like upper(concat('%', :keyword,'%')))" +
            " OR (upper(coalesce(c.mobile,'')) like upper(concat('%', :keyword,'%')))" +
            " OR (upper(coalesce(c.firstName,'')) like upper(concat('%', :keyword,'%')))" +
            " OR :keyword IS NULL" +
            ") ",
            countQuery = "select count(c) from EventContact ec join ec.member c " +
                    " where c.emailAccount<>:userEmail " +
                    "AND ec.eventId=:eventId " +
                    "AND c.contactType=:contactType " +
                    "AND ec.status=:status " +
                    "AND ec.memberId not in (select tc.memberId from TeamContact tc where tc.teamId=:teamId) " +
                    "AND (" +
                    "(upper(coalesce(c.email,'')) like upper(concat('%', :keyword,'%')))" +
                    " OR (upper(coalesce(c.phone,'')) like upper(concat('%', :keyword,'%')))" +
                    " OR (upper(coalesce(c.mobile,'')) like upper(concat('%', :keyword,'%')))" +
                    " OR (upper(coalesce(c.firstName,'')) like upper(concat('%', :keyword,'%')))" +
                    " OR :keyword IS NULL" +
                    ") ")
    List<Contact> searchUserToAddToTeam(@Param("keyword") String keyword,
                                        @Param("userEmail") String userEmail,
                                        @Param("teamId") Long teamId,
                                        @Param("eventId") Long eventId,
                                        @Param("contactType") ContactType contactType,
                                        @Param("status") Constant.TypeConstant.EventMemberStatus status
    );
}
