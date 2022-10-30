package com.parking.pes.repository;

import com.parking.pes.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    Event findByLicensePlate(String licensePlate);

}
