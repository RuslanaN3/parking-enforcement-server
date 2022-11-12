package com.parking.model;

import java.util.List;
import javax.persistence.*;

@Entity
public class RouteCycle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer cycleNumber;

    private String startedAt;

    @OneToMany(mappedBy = "routeCycle", cascade = CascadeType.ALL)
    private List<ParkedVehicle> parkedVehicles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCycleNumber() {
        return cycleNumber;
    }

    public void setCycleNumber(Integer cycleNumber) {
        this.cycleNumber = cycleNumber;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public List<ParkedVehicle> getParkedVehicles() {
        return parkedVehicles;
    }

    public void setParkedVehicles(List<ParkedVehicle> parkedVehicles) {
        this.parkedVehicles = parkedVehicles;
    }
}
