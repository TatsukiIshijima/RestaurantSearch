package com.example.ti.restaurantsearchapi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by TI on 2016/04/16.
 */
public class GurunabiApi {

    private static final String API_ENDPOINT = "http://api.gnavi.co.jp/RestSearchAPI/20150630/";

    public static RestSearch getRestaurant(String lat, String lon, int range, String freeword,
                                           String lunch, String bottom, String buffet, String parking, String no_smoking) throws IOException ,JSONException {

        String accekey = "*********************";
        String format = "json";
        int hit_per_page = 10;
        int offset_page = 1;

        String prmKeyid = "?keyid=" + accekey;
        String prmFormat = "&format=" + format;
        String prmLat = "&latitude=" + lat;
        String prmLon = "&longitude=" + lon;
        String prmHit_per_page = "&hit_per_page=" + hit_per_page;
        String prmoffset_page = "&offset_page=" + offset_page;
        String prmrange = "&range=" + range;
        String prmfreeword = "&freeword=" + freeword;
        String prmlunch = "&lunch=" + lunch;
        String prmbottle = "&bottomless_cup=" + bottom;
        String prmbuffet = "&buffet=" + buffet;
        String prmparking = "&parking=" + parking;
        String prmsmoking = "&no_smoking==" + no_smoking;

        URL url = new URL(API_ENDPOINT + prmKeyid + prmFormat + prmLat + prmLon + prmHit_per_page + prmoffset_page + prmrange + prmfreeword + prmlunch + prmbottle + prmbuffet + prmparking + prmsmoking);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            connection.disconnect();
        }
        return new RestSearch(new JSONObject(sb.toString()));
    }
}
