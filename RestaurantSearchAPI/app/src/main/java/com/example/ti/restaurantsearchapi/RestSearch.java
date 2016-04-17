package com.example.ti.restaurantsearchapi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TI on 2016/04/16.
 * JSON解析
 */
public class RestSearch {
    public final int total_hit_count;
    public final List<Rest> restList = new ArrayList<Rest>();

    public RestSearch(JSONObject jsonObject) throws JSONException {

        total_hit_count = jsonObject.getInt("total_hit_count");

        JSONArray restArray = jsonObject.getJSONArray("rest");

        for (int i=0; i<restArray.length(); i++) {
            JSONObject restJson = restArray.getJSONObject(i);
            Rest rest = new Rest(restJson);
            restList.add(rest);
        }
    }

    public static class Rest {
        public final String name;
        public final Access access;
        public final Image_url image_url;
        public final String address;
        public final String tel;
        public final String opentime;
        public final String url;

        public Rest(JSONObject jsonObject) throws JSONException {
            name = jsonObject.getString("name");
            access = new Access(jsonObject.getJSONObject("access"));
            image_url = new Image_url(jsonObject.getJSONObject("image_url"));
            address = jsonObject.getString("address");
            tel = jsonObject.getString("tel");
            opentime = jsonObject.getString("opentime");
            url = jsonObject.getString("url");
        }
    }

    public static class Access {
        public final String line;
        public final String station;
        public final String station_exit;
        public final String walk;
        public final String note;

        public Access(JSONObject jsonObject) throws  JSONException {
            if (!jsonObject.isNull("line")) {
                line = jsonObject.getString("line");
            } else {
                line = null;
            }
            if (!jsonObject.isNull("station")) {
                station = jsonObject.getString("station");
            } else {
                station = null;
            }
            if (!jsonObject.isNull("station_exit")) {
                station_exit = jsonObject.getString("station_exit");
            } else {
                station_exit = null;
            }
            if (!jsonObject.isNull("walk")) {
                walk = jsonObject.getString("walk");
            } else {
                walk = null;
            }
            if (!jsonObject.isNull("note")) {
                note = jsonObject.getString("note");
            } else {
                note =null;
            }
        }
    }

    public static class Image_url {
        public final String shop_image1;
        public final String shop_image2;
        public final String qrcode;

        public Image_url(JSONObject jsonObject) throws JSONException {
            if (!jsonObject.isNull("shop_image1")) {
                shop_image1 = jsonObject.getString("shop_image1");
            } else {
                shop_image1 = null;
            }
            if (!jsonObject.isNull("shop_image2")) {
                shop_image2 = jsonObject.getString("shop_image2");
            } else {
                shop_image2 = null;
            }
            if (!jsonObject.isNull("qrcode")) {
                qrcode = jsonObject.getString("qrcode");
            } else {
                qrcode = null;
            }

        }
    }
}
