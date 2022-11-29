package com.parking.dto;

public class VerificationResult {
    private Status status;

    public VerificationResult(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
