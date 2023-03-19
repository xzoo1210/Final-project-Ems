package com.ems.api.repository;

import com.ems.api.entity.UserContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserContactRepo extends JpaRepository<UserContact, Long> {
    boolean existsByUserIdAndOtherContactId(Long userId, Long otherContactId);

    UserContact findByUserIdAndOtherContactId(Long userId, Long otherContactId);
}
