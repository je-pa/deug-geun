package com.zb.deuggeun.reserve.repository;

import com.zb.deuggeun.common.repository.CustomJpaRepository;
import com.zb.deuggeun.reserve.entity.ReservationStatistics;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationStatisticsRepository extends
    CustomJpaRepository<ReservationStatistics, Long> {

}
