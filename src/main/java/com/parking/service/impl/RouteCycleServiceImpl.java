package com.parking.service.impl;

import com.parking.model.ParkedVehicle;
import com.parking.model.RouteCycle;
import com.parking.repository.RouteCycleRepository;
import com.parking.service.RouteCycleService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RouteCycleServiceImpl implements RouteCycleService {

    private RouteCycleRepository routeCycleRepository;

    @Override
    public void createCycleIfNotPresent(Integer cycleNumber) {
        RouteCycle cycle = routeCycleRepository.findByCycleNumber(cycleNumber);
        if (cycle == null) {
            //clearLeftVehicles();
            RouteCycle routeCycle = new RouteCycle();
            routeCycle.setCycleNumber(cycleNumber);
            routeCycle.setStartedAt("time");
            routeCycleRepository.save(routeCycle);
        }
    }

    public RouteCycle findByCycleNumber(Integer cycleNumber) {
        return routeCycleRepository.findByCycleNumber(cycleNumber);
    }

    //private void clearLeftVehicles() {
    //    Integer currentCycleNumber = routeCycleRepository.findCurrentCycle();
    //    if (currentCycleNumber != null) {
    //        List<ParkedVehicle> parkedVehicles = parkedVehicleRepository.findAll();
    //        parkedVehicles.stream()
    //            .filter(parkedVehicle -> parkedVehicle.getRouteCycle().getCycleNumber() < currentCycleNumber)
    //            .forEach(parkedVehicle -> parkedVehicle.setResolved(true));
    //        parkedVehicleRepository.saveAll(parkedVehicles);
    //    }
    //}
}
