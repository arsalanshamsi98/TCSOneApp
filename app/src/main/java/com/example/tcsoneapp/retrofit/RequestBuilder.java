package com.example.tcsoneapp.retrofit;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by umair.irshad on 10/26/2018
 */

public class RequestBuilder {

    public static final String REQUEST_CLIENT_NAME = "TCS";
    public static final String REQUEST_CLIENT_CHANNEL = "MOB";
    public static final String REQUEST_REFERENCE_NUMBER = "";

    public static final String _REQUEST = "eAI_REQUEST";
    public static final String _HEADER = "eAI_HEADER";
    public static final String _MESSAGE = "eAI_MESSAGE";
    public static final String _BODY = "eAI_BODY";


    private static final String REQUEST_AUTH_USER = "";
    private static final String REQUEST_AUTH_PASSWORD = "";

    private JSONObject eAI_MESSAGE = new JSONObject();
    private JSONObject eAI_REQUEST = new JSONObject();

    private JSONObject jsonObject;

    public RequestBuilder() {

    }

    public RequestBuilder createJsonObject(String serviceName) {
//        JSONObject jsonObject = new JSONObject();
        try {

            JSONObject eAI_HEADER = new JSONObject();

            JSONObject securityInfo = new JSONObject();
            JSONObject authentication = new JSONObject();
            authentication.put("userId", REQUEST_AUTH_USER);
            authentication.put("password", REQUEST_AUTH_PASSWORD);

            eAI_HEADER.put("serviceName", serviceName);
            eAI_HEADER.put("client", REQUEST_CLIENT_NAME);
            eAI_HEADER.put("clientChannel", REQUEST_CLIENT_CHANNEL);
            eAI_HEADER.put("referenceNum", REQUEST_REFERENCE_NUMBER);
            eAI_HEADER.put("securityInfo", securityInfo);
            securityInfo.put("authentication", authentication);

            eAI_MESSAGE.put(_HEADER, eAI_HEADER);

//            create_eAI_MESSAGE();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }


    public RequestBuilder setJsonObjest(JSONObject submitLeadObject) {
        jsonObject = submitLeadObject;
        return this;
    }

    private void create_eAI_MESSAGE() {
        try {
            jsonObject.put(_MESSAGE, eAI_MESSAGE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JsonObject build() {
        create_eAI_MESSAGE();
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());
        return gsonObject;
    }

    public RequestBuilder addBody() {

        try {
            JSONObject eAI_BODY = new JSONObject();

            eAI_BODY.put(_REQUEST, eAI_REQUEST);
            eAI_MESSAGE.put(_BODY, eAI_BODY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }


    public void create_authenticateUserRequest(String userId ) {
        JSONObject authenticateUserRequest = new JSONObject();
        JSONObject jsonData = new JSONObject();

        try {
            jsonData.put("employeeNumber", userId);
            jsonData.put("applicationId", "1");
            authenticateUserRequest.put("jsonData", jsonData);

            eAI_REQUEST.put("tcsLoginRequest", authenticateUserRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}
