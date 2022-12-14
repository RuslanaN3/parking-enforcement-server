package com.parking.repository;

import com.parking.dto.Status;
import com.parking.model.ParkedVehicle;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkedVehicleRepository extends JpaRepository<ParkedVehicle, Long> {

    ParkedVehicle findByLicensePlate(String licensePlate);

    ParkedVehicle findByLicensePlateAndStatus(String licensePlate, Status status);

    boolean existsParkedVehicleByLicensePlate(String licensePlate);

    List<ParkedVehicle> findAllByStatus(Status status);
}
