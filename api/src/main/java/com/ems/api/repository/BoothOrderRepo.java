package com.ems.api.repository;

import com.ems.api.entity.BoothOrder;
import com.ems.api.util.Constant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoothOrderRepo extends JpaRepository<BoothOrder, Long> {
    @Query(value = "select bo from BoothOrder bo " +
            "WHERE bo.booth.eventId=:eventId " +
            "AND (bo.boothId=:boothId or :boothId is null)",
            countQuery = "select count(bo) from BoothOrder bo " +
                    "WHERE bo.booth.eventId=:eventId " +
                    "AND (bo.boothId=:boothId or :boothId is null)")
    Page<BoothOrder> searchBoothOrder(@Param("eventId") Long eventId,
                                      @Param("boothId") Long boothId,
                                      Pageable pageable);

    boolean existsByBoothIdAndEmailAndStatus(Long boothId, String email, Constant.TypeConstant.BoothOrderStatus status);

    boolean existsByBoothId(Long boothId);

    @Query(value = "select bo from BoothOrder bo " +
            "WHERE bo.booth.eventId=:eventId and bo.status=:status")
    List<BoothOrder> findAllByEventIdAndStatus(@Param("eventId") Long eventId,
                                               @Param("status") Constant.TypeConstant.BoothOrderStatus status);

    boolean existsByBoothIdAndStatus(Long boothId,Constant.TypeConstant.BoothOrderStatus status);
}
