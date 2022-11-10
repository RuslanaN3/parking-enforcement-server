package com.parking.pes.validator;

import com.parking.pes.dto.Location;
import com.parking.pes.repository.ParkingAreaRepository;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ParkingZoneValidator implements ConstraintValidator<ParkingZone, Location> {
    private ParkingAreaRepository parkingAreaRepository;

    public ParkingZoneValidator(ParkingAreaRepository parkingAreaRepository) {
        this.parkingAreaRepository = parkingAreaRepository;
    }

    @Override
    public void initialize(ParkingZone constraintAnnotation) {
    }

    @Override
    public boolean isValid(Location location, ConstraintValidatorContext context) {
        return location != null &&
            parkingAreaRepository.findParkingAreaByPoint(location.getLatitude(), location.getLongitude()) != null;
    }
}
