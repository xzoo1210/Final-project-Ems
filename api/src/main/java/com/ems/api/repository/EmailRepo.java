package com.ems.api.repository;

import com.ems.api.entity.Email;
import com.ems.api.util.Constant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepo extends JpaRepository<Email, Long> {
    @Query(value = "select e from Email e where " +
            "e.status in (:statuses) " +
            "and e.emailType = :emailType",
            countQuery = "select count(e) from Email e where " +
                    "e.status in (:status) " +
                    "and e.emailType = :emailType")
    Page<Email> findAllByStatusAndEmailTypeOrderByIdDesc(@Param("statuses") List<Constant.TypeConstant.EmailStatus> statuses,
                                                         @Param("emailType") Constant.EmailType emailType,
                                                         Pageable pageable);
}
