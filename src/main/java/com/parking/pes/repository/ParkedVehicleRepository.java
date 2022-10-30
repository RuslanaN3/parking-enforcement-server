package com.parking.pes.repository;

import com.parking.pes.model.ParkedVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkedVehicleRepository extends JpaRepository<ParkedVehicle, Integer> {

    ParkedVehicle findByLicensePlate(String licensePlate);

}
