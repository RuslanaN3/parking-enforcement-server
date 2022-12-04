package com.parking.repository;

import com.parking.model.Event;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByLicensePlate(String licencePlate);

    List<Event> findAllByTimestampBetween(Instant from, Instant to);

}



