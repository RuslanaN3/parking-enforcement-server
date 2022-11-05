package com.parking.pes.service.impl;

import com.parking.pes.dto.CreateFineRequest;
import com.parking.pes.model.ParkedVehicle;
import com.parking.pes.service.FineService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FineServiceImpl implements FineService {

    private RestTemplate restTemplate;
    private String fineGenerationServiceUrl;

    public FineServiceImpl(RestTemplate restTemplate, @Value("${fine.service.url}") String fineGenerationServiceUrl) {
        this.restTemplate = restTemplate;
        this.fineGenerationServiceUrl = fineGenerationServiceUrl;
    }

    @Override
    public void createFine(ParkedVehicle parkedVehicle) {
       CreateFineRequest createFineRequest = new CreateFineRequest();
       createFineRequest.setLicensePlate(parkedVehicle.getLicensePlate());

       performCreateFineRequest(createFineRequest);
    }

    private void performCreateFineRequest(CreateFineRequest createFineRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("apiKey", "token");

        HttpEntity<CreateFineRequest> request = new HttpEntity<>(createFineRequest, headers);
        restTemplate.exchange(fineGenerationServiceUrl, HttpMethod.POST, request, Map.class).getBody();
    }
}
