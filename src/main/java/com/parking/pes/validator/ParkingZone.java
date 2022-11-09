package com.parking.pes.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ParkingZoneValidator.class)
public @interface ParkingZone {
    String message() default "{Isn't parking zone}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
