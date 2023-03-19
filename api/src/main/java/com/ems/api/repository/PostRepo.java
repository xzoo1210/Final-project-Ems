package com.ems.api.repository;


import com.ems.api.dto.EventPostExport;
import com.ems.api.entity.Post;
import com.ems.api.util.Constant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {
    @Query(value = "select p from Post p WHERE " +
            "p.postType=:postType AND " +
            "((upper(coalesce(p.subject,'')) like upper(concat('%', :keyword,'%'))) " +
            "OR (upper(coalesce(p.overviewDescription,'')) like upper(concat('%', :keyword,'%'))) " +
            "OR :keyword IS NULL) " +
            "AND (p.event.id = :eventId OR :eventId IS NULL)" +
            "AND (p.team.id = :teamId OR :teamId IS NULL)",
            countQuery = "select count(p) from Post p WHERE " +
                    "p.postType=:postType AND " +
                    "((upper(coalesce(p.subject,'')) like upper(concat('%', :keyword,'%'))) " +
                    "OR (upper(coalesce(p.overviewDescription,'')) like upper(concat('%', :keyword,'%'))) " +
                    "OR :keyword IS NULL) " +
                    "AND (p.event.id = :eventId OR :eventId IS NULL)" +
                    "AND (p.team.id = :teamId OR :teamId IS NULL)")
    Page<Post> search(@Param("keyword") String keyword,
                      @Param("eventId") Long eventId,
                      @Param("teamId") Long teamId,
                      @Param("postType") Constant.TypeConstant.PostType postType,
                      Pageable pageable);

    @Query(value = "select p from Post p WHERE " +
            "p.postType=:postType AND " +
            "p.status=:status AND " +
            "(p.eventId=:eventId OR :eventId is null) AND " +
            "((upper(coalesce(p.subject,'')) like upper(concat('%', :keyword,'%'))) " +
            "OR (upper(coalesce(p.overviewDescription,'')) like upper(concat('%', :keyword,'%'))) " +
            "OR :keyword IS NULL) ",
            countQuery = "select count(p) from Post p WHERE " +
                    "p.postType=:postType AND " +
                    "p.status=:status AND " +
                    "(p.eventId=:eventId OR :eventId is null) AND " +
                    "((upper(coalesce(p.subject,'')) like upper(concat('%', :keyword,'%'))) " +
                    "OR (upper(coalesce(p.overviewDescription,'')) like upper(concat('%', :keyword,'%'))) " +
                    "OR :keyword IS NULL) ")
    Page<Post> searchPublic(@Param("keyword") String keyword,
                            @Param("postType") Constant.TypeConstant.PostType postType,
                            @Param("status") Constant.TypeConstant.PostStatus status,
                            @Param("eventId") Long eventId,
                            Pageable pageable);

    Optional<Post> findByIdAndPostType(Long id, Constant.TypeConstant.PostType postType);
    List<Post> findByEventId(Long eventId);

    @Query(value = "select p.subject,   " +
            "c.first_name creatorName,   " +
            "p.create_date,   " +
            "CASE   " +
            "    WHEN p.status = 'ACTIVE' THEN 'Hoạt động'   " +
            "    WHEN p.status = 'NOT_ACTIVE' THEN 'Không hoạt động'   " +
            "END status,   " +
            "p.views   " +
            " from ems.post p   " +
            " join ems.contact c on p.creator_id=c.id   " +
            " where p.post_type='EVENT' and p.event_id=:eventId   " +
            " order by views desc",nativeQuery = true)
    List<EventPostExport> findAllByEventIdForExport(@Param("eventId")Long eventId);
}
