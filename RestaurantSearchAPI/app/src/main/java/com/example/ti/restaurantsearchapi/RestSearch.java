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
            if (!jsonObject.getString("address").equals("{}")) {
                address = jsonObject.getString("address");
            } else {
                address = "";
            }
            if (!jsonObject.getString("tel").equals("{}")) {
                tel = jsonObject.getString("tel");
            } else {
                tel = "";
            }
            if (!jsonObject.getString("opentime").equals("{}")) {
                opentime = jsonObject.getString("opentime");
            } else {
                opentime = "";
            }
            if (!jsonObject.getString("url").equals("{}")) {
                url = jsonObject.getString("url");
            } else {
                url = "";
            }
        }
    }

    public static class Access {
        public final String line;
        public final String station;
        public final String station_exit;
        public final String walk;
        public final String note;

        public Access(JSONObject jsonObject) throws  JSONException {
            if (!jsonObject.getString("line").equals("{}")) {
                line = jsonObject.getString("line");
            } else {
                line = "";
            }
            if (!jsonObject.getString("station").equals("{}")) {
                station = jsonObject.getString("station");
            } else {
                station = "";
            }
            if (!jsonObject.getString("station_exit").equals("{}")) {
                station_exit = jsonObject.getString("station_exit");
            } else {
                station_exit = "";
            }
            if (!jsonObject.getString("walk").equals("{}")) {
                walk = jsonObject.getString("walk");
            } else {
                walk = "";
            }
            if (!jsonObject.getString("note").equals("{}")) {
                note = jsonObject.getString("note");
            } else {
                note = "";
            }
        }
    }

    public static class Image_url {
        public final String shop_image1;
        public final String shop_image2;
        public final String qrcode;

        public Image_url(JSONObject jsonObject) throws JSONException {
            if (!jsonObject.getString("shop_image1").equals("{}")) {
                shop_image1 = jsonObject.getString("shop_image1");
            } else {
                shop_image1 = null;
            }
            if (!jsonObject.getString("shop_image2").equals("{}")) {
                shop_image2 = jsonObject.getString("shop_image2");
            } else {
                shop_image2 = null;
            }
            if (!jsonObject.getString("qrcode").equals("{}")) {
                qrcode = jsonObject.getString("qrcode");
            } else {
                qrcode = null;
            }

        }
    }
}
