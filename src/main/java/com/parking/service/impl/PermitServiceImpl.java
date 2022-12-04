package com.parking.service.impl;

import com.parking.dto.PermitRequest;
import com.parking.dto.PermitResponse;
import com.parking.model.Address;
import com.parking.service.PermitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PermitServiceImpl implements PermitService {
    private static final String API_KEY = "X-API-KEY";

    private RestTemplate restTemplate;
    private String permitServiceUrl;
    private String permitServiceApiKey;

    public PermitServiceImpl(RestTemplate restTemplate,
                             @Value("${permit.service.url}") String permitServiceUrl,
                             @Value("${permit.service.apiKey}") String permitServiceApiKey) {
        this.restTemplate = restTemplate;
        this.permitServiceUrl = permitServiceUrl;
        this.permitServiceApiKey = permitServiceApiKey;
    }

    @Override
    public PermitResponse checkPermit(String licensePlate, Address parkingAreaAddress) {
        PermitRequest permitRequest = createRequest(licensePlate, parkingAreaAddress);
        HttpHeaders headers = new HttpHeaders();
        headers.set(API_KEY, permitServiceApiKey);
        HttpEntity<PermitRequest> request = new HttpEntity<>(permitRequest, headers);
        PermitResponse permitResponse = restTemplate.exchange(permitServiceUrl, HttpMethod.POST, request, PermitResponse.class).getBody();
        return permitResponse;
    }

    private PermitRequest createRequest(String licensePlate, Address parkingAreaAddress) {
        PermitRequest permitRequest = new PermitRequest();
        permitRequest.setLicensePlate(licensePlate);
        permitRequest.setParkingAreaAddress(parkingAreaAddress);
        return permitRequest;
    }
}



