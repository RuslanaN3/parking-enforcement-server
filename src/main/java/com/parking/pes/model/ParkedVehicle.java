package com.parking.pes.model;

import java.util.Date;
import javax.persistence.*;

@Entity
public class ParkedVehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String licensePlate;

    private Date firstTimeSpotted;

    private Date lastTimeSpotted;

    @Enumerated(EnumType.STRING)
    private Status status = Status.STARTED;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Date getFirstTimeSpotted() {
        return firstTimeSpotted;
    }

    public void setFirstTimeSpotted(Date firstTimeSpotted) {
        this.firstTimeSpotted = firstTimeSpotted;
    }

    public Date getLastTimeSpotted() {
        return lastTimeSpotted;
    }

    public void setLastTimeSpotted(Date lastTimeSpotted) {
        this.lastTimeSpotted = lastTimeSpotted;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
