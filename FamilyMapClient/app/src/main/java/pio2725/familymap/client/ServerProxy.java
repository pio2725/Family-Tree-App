package pio2725.familymap.client;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import Reqeust.LoginRequest;
import Reqeust.RegisterRequest;
import Result.EventResult;
import Result.LoginResult;
import Result.PersonResult;
import Result.PersonResultSingle;
import Result.RegisterResult;

public class ServerProxy {

    private Gson gson;
    private static final String TAG = "HttpClient";
    private boolean isSuccess;

    public ServerProxy() {
        gson = new Gson();
        isSuccess = false;
    }

    public LoginResult getLoginResult(URL url, LoginRequest loginRequest) {

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.addRequestProperty("Accept", "application/json");
            connection.connect();

            String jsonString = gson.toJson(loginRequest);

            OutputStream requestBody = connection.getOutputStream();
            writeString(jsonString, requestBody);
            requestBody.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Reader reader = new InputStreamReader(connection.getInputStream());
                LoginResult loginResult = gson.fromJson(reader, LoginResult.class);
                connection.getInputStream().close();
                setSuccess(true);
                return loginResult;
            }
            else {
                LoginResult loginResult = new LoginResult();
                loginResult.setMessage(connection.getResponseMessage());
                return loginResult;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Sign in proxy");
        }
        return null;
    }

    public RegisterResult getRegisterResult(URL url, RegisterRequest registerRequest) {

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            connection.connect();

            String jsonString = gson.toJson(registerRequest);

            OutputStream requestBody = connection.getOutputStream();
            writeString(jsonString, requestBody);
            requestBody.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Reader reader = new InputStreamReader(connection.getInputStream());
                RegisterResult registerResult = gson.fromJson(reader, RegisterResult.class);
                connection.getInputStream().close();
                return registerResult;
            }
            else {
                RegisterResult registerResult = new RegisterResult();
                registerResult.setMessage(connection.getResponseMessage());
                return registerResult;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Register in proxy");
        }
        return null;
    }

    public PersonResult getAllPeopleResult(URL url, String authToken) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.addRequestProperty("Authorization", authToken);
            connection.addRequestProperty("Accept", "application/json");
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Reader reader = new InputStreamReader(connection.getInputStream());
                PersonResult personResult = gson.fromJson(reader, PersonResult.class);
                connection.getInputStream().close();
                return personResult;
            }
            else {
                PersonResult personResult = new PersonResult();
                personResult.setMessage("error");
                return personResult;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Getting all people in proxy");
        }
        return null;
    }

    public PersonResultSingle getPersonResult(URL url, String authToken) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.addRequestProperty("Authorization", authToken);
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Reader reader = new InputStreamReader(connection.getInputStream());
                PersonResultSingle personResultSingle = gson.fromJson(reader, PersonResultSingle.class);
                connection.getInputStream().close();
                return personResultSingle;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            PersonResultSingle personResultSingle = new PersonResultSingle();
            personResultSingle.setMessage("Getting a person in proxy");
            return personResultSingle;
        }
        return null;
    }

    public EventResult getAllEventResult(URL url, String authToken) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.addRequestProperty("Authorization", authToken);
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Reader reader = new InputStreamReader(connection.getInputStream());
                EventResult eventResult = gson.fromJson(reader, EventResult.class);
                return eventResult;
            }
            else {
                EventResult eventResult = new EventResult();
                eventResult.setMessage(connection.getResponseMessage());
                return eventResult;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Getting all events in proxy");
        }
        return null;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
