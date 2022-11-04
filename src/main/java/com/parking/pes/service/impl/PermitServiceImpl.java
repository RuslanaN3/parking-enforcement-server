package com.parking.pes.service.impl;

import com.parking.pes.dto.Location;
import com.parking.pes.dto.PermitRequest;
import com.parking.pes.dto.PermitResponse;
import com.parking.pes.service.PermitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PermitServiceImpl implements PermitService {

    private RestTemplate restTemplate;
    private String permitServiceUrl;

    public PermitServiceImpl(RestTemplate restTemplate, @Value("${permit.service.url}") String permitServiceUrl) {
        this.restTemplate = restTemplate;
        this.permitServiceUrl = permitServiceUrl;
    }

    @Override
    public PermitResponse checkPermit(String licensePlate, Location location) {

        // some logic
        HttpHeaders headers = new HttpHeaders();
        headers.set("apiKey", "token");

        PermitRequest permitRequest = new PermitRequest();
        permitRequest.setLicensePlate(licensePlate);
        permitRequest.setLocation(location);
        HttpEntity<PermitRequest> request = new HttpEntity<>(permitRequest, headers);

        PermitResponse permitResponse = restTemplate.exchange(permitServiceUrl, HttpMethod.POST, request, PermitResponse.class).getBody();
        //permitResponse.setHasPermit(false);
        //PermitResponse permitResponse = new PermitResponse();
        //permitResponse.setHasPermit(false);
        return permitResponse;
    }
}
