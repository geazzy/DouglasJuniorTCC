/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.util;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderAddressComponent;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zanoni
 */
public class GeoCodeUtil {

    private static final Geocoder geocoder = new Geocoder();
    private static final Map<String, String> mapUserLocation = new HashMap<>();
    //private static final Geocoder geocoder;

    public static String getCountry(String adress) {
        // Geocoder geocoder = new Geocoder("196213830538-6b4vlkvko1rrho0tjprg4s92hc8vgtu2.apps.googleusercontent.com",
        //        "AIzaSyDIcqvpT5tPqCYfa_MhUW_NBFLn9dcxw7Y");
        String country = "";

        if ("".equals(adress) || adress == null) {
            return "";
        }

        Map<String, Integer> countryMap = new HashMap<>();

        GeocoderRequest geocoderRequest = new GeocoderRequestBuilder()
                .setAddress(adress).setLanguage("en").getGeocoderRequest();
        GeocodeResponse geocoderResponse;
        try {
            geocoderResponse = geocoder.geocode(geocoderRequest);

            for (GeocoderResult georesult : geocoderResponse.getResults()) {

                for (GeocoderAddressComponent addressComp : georesult.getAddressComponents()) {

                    //em caso do resultado retornar mais de um país, pega o país que mais ocorreu
                    if (addressComp.getTypes().contains("country")) {
                        country = addressComp.getLongName();
                        if (countryMap.containsKey(country)) {
                            countryMap.put(country, countryMap.get(country) + 1);
                        } else {
                            countryMap.put(country, 1);
                        }

                    }
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(GeoCodeUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        Integer maior = 0;
        String countryResult = "";
        for (Map.Entry<String, Integer> countryTemp : countryMap.entrySet()) {
            String countryKey = countryTemp.getKey();
            Integer intValue = countryTemp.getValue();

            if (intValue > maior) {
                countryResult = countryKey;
                maior = intValue;
            }

        }
        //System.out.println("country map size: "+countryMap.size());
        // System.out.println("Maior: "+ maior);
        return countryResult;
    }

    public static String getCountry(String login, String adress) {

        String country = "";

        if ("".equals(adress) || adress == null) {
            return "";
        }
        
        if (mapUserLocation.containsKey(login)){
            return mapUserLocation.get(login);
        }
        
        

        Map<String, Integer> countryMap = new HashMap<>();

        GeocoderRequest geocoderRequest = new GeocoderRequestBuilder()
                .setAddress(adress).setLanguage("en").getGeocoderRequest();
        GeocodeResponse geocoderResponse;
        try {
            geocoderResponse = geocoder.geocode(geocoderRequest);

            for (GeocoderResult georesult : geocoderResponse.getResults()) {

                for (GeocoderAddressComponent addressComp : georesult.getAddressComponents()) {

                    //em caso do resultado retornar mais de um país, pega o país que mais ocorreu
                    if (addressComp.getTypes().contains("country")) {
                        country = addressComp.getLongName();
                        if (countryMap.containsKey(country)) {
                            countryMap.put(country, countryMap.get(country) + 1);
                        } else {
                            countryMap.put(country, 1);
                        }

                    }
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(GeoCodeUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        Integer maior = 0;
        String countryResult = "";
        for (Map.Entry<String, Integer> countryTemp : countryMap.entrySet()) {
            String countryKey = countryTemp.getKey();
            Integer intValue = countryTemp.getValue();

            if (intValue > maior) {
                countryResult = countryKey;
                maior = intValue;
            }

        }
        //System.out.println("country map size: "+countryMap.size());
        // System.out.println("Maior: "+ maior);
        mapUserLocation.put(login, countryResult);
        return countryResult;
    }

}
