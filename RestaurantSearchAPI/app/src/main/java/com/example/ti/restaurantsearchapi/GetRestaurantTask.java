package com.example.ti.restaurantsearchapi;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by TI on 2016/04/16.
 */
public class GetRestaurantTask extends AsyncTask<String, Void, RestSearch> {
    Exception exception;

    @Override
    protected RestSearch doInBackground(String... params) {
        try {
            return GurunabiApi.getRestaurant(params[0], params[1], Integer.parseInt(params[2]), params[3],
                    params[4], params[5], params[6], params[7], params[8]);
        } catch (IOException | JSONException e) {
            exception = e;
        }
        return null;
    }
}
