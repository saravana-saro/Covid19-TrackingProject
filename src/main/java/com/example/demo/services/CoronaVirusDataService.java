package com.example.demo.services;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.model.LocationStates;

import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronaVirusDataService {

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";

    private List<LocationStates> allStates = new ArrayList<LocationStates>();

    public List<LocationStates> getAllStates() {
        return allStates;
    }
    

    public void setAllstates(List<LocationStates> allstates){
        this.allStates = allstates;
    }

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        List<LocationStates> newStates = new ArrayList<LocationStates>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        
        for (CSVRecord record : records) {
        	
            LocationStates losta = new LocationStates();
            losta.setState(record.get("Province/State"));
            losta.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int prevCases = Integer.parseInt(record.get(record.size() - 2));
            losta.setLatestTotalCases(latestCases);
            losta.setDiffFromPrevay(latestCases - prevCases);
            System.out.println(losta);
            newStates.add(losta);
        }
        
        this.allStates = newStates;
    }

}


