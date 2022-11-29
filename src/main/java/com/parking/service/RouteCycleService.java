package com.parking.service;


import com.parking.model.RouteCycle;

public interface RouteCycleService {
    RouteCycle createCycleIfNotPresent(Integer cycleNumber);

    RouteCycle findByCycleNumber(Integer cycleNumber);
}
