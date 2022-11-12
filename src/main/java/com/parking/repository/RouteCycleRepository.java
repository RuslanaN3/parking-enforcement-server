package com.parking.repository;

import com.parking.model.RouteCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteCycleRepository extends JpaRepository<RouteCycle, Integer> {
    RouteCycle findByCycleNumber(Integer cycleNumber);

    @Query(value = "SELECT MAX(cycle_number) FROM route_cycle", nativeQuery = true)
    Integer findCurrentCycle();
}
