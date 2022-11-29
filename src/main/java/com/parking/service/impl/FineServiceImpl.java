package com.parking.service.impl;

import com.parking.dto.CreateFineRequest;
import com.parking.model.ParkedVehicle;
import com.parking.service.FineService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FineServiceImpl implements FineService {
    private static final String API_KEY = "X-API-KEY";

    private RestTemplate restTemplate;
    private String fineServiceUrl;
    private String fineServiceApiKey;

    public FineServiceImpl(RestTemplate restTemplate, @Value("${fine.service.url}") String fineServiceUrl,
                           @Value("${fine.service.apiKey}") String fineServiceApiKey) {
        this.restTemplate = restTemplate;
        this.fineServiceUrl = fineServiceUrl;
        this.fineServiceApiKey = fineServiceApiKey;
    }

    @Override
    public void reportViolation(ParkedVehicle parkedVehicle) {
       CreateFineRequest createFineRequest = new CreateFineRequest();
       createFineRequest.setLicensePlate(parkedVehicle.getLicensePlate());

       performCreateFineRequest(createFineRequest);
    }

    private void performCreateFineRequest(CreateFineRequest createFineRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(API_KEY, fineServiceApiKey);

        HttpEntity<CreateFineRequest> request = new HttpEntity<>(createFineRequest, headers);
        //restTemplate.exchange(fineGenerationServiceUrl, HttpMethod.POST, request, Map.class).getBody();
    }
}
