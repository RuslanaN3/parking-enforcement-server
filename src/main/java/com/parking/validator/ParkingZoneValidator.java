package com.parking.validator;

import com.parking.dto.Location;
import com.parking.repository.ParkingAreaRepository;
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
