package com.rd.t1d.service;

import com.rd.t1d.dto.CityDetails;
import com.wirefreethought.geodb.client.GeoDbApi;
import com.wirefreethought.geodb.client.model.*;
import com.wirefreethought.geodb.client.net.GeoDbApiClient;
import com.wirefreethought.geodb.client.request.FindPlacesRequest;
import com.wirefreethought.geodb.client.request.PlaceRequestType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CommonUtilityService {

    public List<CityDetails> getCityDetails(String cityPrefix){
        GeoDbApiClient geoDbApiClient = new GeoDbApiClient(GeoDbInstanceType.FREE);
        GeoDbApi geoDbApi = new GeoDbApi(geoDbApiClient);

        PopulatedPlacesResponse response = geoDbApi.findPlaces(
                FindPlacesRequest.builder()
                        .namePrefix(cityPrefix)
                        .limit(10)
                        .types(Collections.singleton(PlaceRequestType.CITY)).build()
        );

        if(null == response.getData() || response.getData().size() == 0){
            return new ArrayList<CityDetails>();
        }

        List<CityDetails> cityDetailsList = new ArrayList<>(response.getData().size());
        for(PopulatedPlaceSummary placeSummary : response.getData()){
            CityDetails cityDetails = new CityDetails(placeSummary.getName(), placeSummary.getRegion(), placeSummary.getCountry());
            cityDetailsList.add(cityDetails);
        }
        return cityDetailsList;
    }
}
