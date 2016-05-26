package com.malin.volley.utils;

import org.json.JSONObject;

import java.util.Map;

public class MapToJsonString {
    private MapToJsonString() {
    }

    public static String mapToJSONObject(Map<String, String> map) {
        String json = null;
        try {
            json = new JSONObject(map).toString();
        } catch (Exception e) {
            e.printStackTrace();
            json = new JSONObject().toString();
        }
        return json;
    }
}
