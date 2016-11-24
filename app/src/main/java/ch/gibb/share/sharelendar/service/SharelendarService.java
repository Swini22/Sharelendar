package ch.gibb.share.sharelendar.service;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ch.gibb.share.sharelendar.model.Admin;
import ch.gibb.share.sharelendar.model.Event;
import ch.gibb.share.sharelendar.model.SchoolClass;

public class SharelendarService {

    public List<SchoolClass> getAllSchoolClasses() {
        HttpURLConnection urlConnection= null;
        List<SchoolClass> schoolClasses = new ArrayList<SchoolClass>();
        try {
            URL url = new URL("https://sharelender.born.ch/AllSchoolClasses");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // gets the server json data
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String next;
            while ((next = bufferedReader.readLine()) != null){
                JSONArray jsonArray = new JSONArray(next);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    SchoolClass schoolClass = fillSchoolClass(jsonObject);
                    schoolClasses.add(schoolClass);
                }
            }
        } catch (IOException |JSONException e) {
            e.printStackTrace();
        }
        return schoolClasses;
    }

    public boolean checkAdmin(Admin admin) {
        HttpURLConnection urlConnection= null;
        boolean result = false;
        JSONObject user = new JSONObject();

        try {
            URL url = new URL("https://sharelender.born.ch/CheckAdmin");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type","application/json");
            urlConnection.connect();

            user.put("username", admin.getUsername());
            user.put("password", admin.getPassword());

            // Send POST output.
            DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream ());
            out.writeBytes(user.toString());
            out.flush();
            out.close();

            int httpResult = urlConnection.getResponseCode();
            if(httpResult == HttpURLConnection.HTTP_OK){
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(),"utf-8"));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                result= Boolean.valueOf(response.toString());
                in.close();
            }
        } catch (IOException |JSONException e) {
            e.printStackTrace();
        }finally{
            if(urlConnection!=null)
                urlConnection.disconnect();
        }
        return result;
    }

    public List<Event> getAllEventsInSchoolClass(int id) {
        HttpURLConnection urlConnection= null;
        List<Event> events = new ArrayList<Event>();


        try {
            URL url = new URL("https://sharelender.born.ch/AllEventsInSchoolClass");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.connect();

            String payload = ""+id;
            // Send POST output.
            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream ());
            out.write(payload);
            out.flush();
            out.close();


            int httpResult = urlConnection.getResponseCode();
            if(httpResult == HttpURLConnection.HTTP_OK) {
                // gets the server json data
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String next;
                while ((next = bufferedReader.readLine()) != null) {
                    JSONArray jsonArray = new JSONArray(next);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        Event event = fillEvent(jsonObject);
                        events.add(event);
                    }
                }
            }
        } catch (IOException |JSONException |ParseException e) {
            e.printStackTrace();
        }
        return events;
    }

    private Event fillEvent(JSONObject jsonObject) throws JSONException, ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Event event = new Event();
        event.setId(jsonObject.getInt("id"));
        event.setInformation(jsonObject.getString("information"));
        event.setDate(simpleDateFormat.parse(jsonObject.getString("date")));
        JSONObject jsonObjectSchoolClass =  jsonObject.getJSONObject("schoolClass");
        SchoolClass schoolclass = fillSchoolClass(jsonObjectSchoolClass);
        event.setSchoolClass(schoolclass);
        return event;
    }

    @NonNull
    private SchoolClass fillSchoolClass(JSONObject jsonObjectSchoolClass) throws JSONException {
        SchoolClass schoolclass = new SchoolClass();
        schoolclass.setId(jsonObjectSchoolClass.getInt("id"));
        schoolclass.setName(jsonObjectSchoolClass.getString("name"));
        return schoolclass;
    }

    public Event createEvent(Event event) {
        HttpURLConnection urlConnection= null;
        JSONObject eventJSON = new JSONObject();
        JSONObject schoolClassJSON = new JSONObject();
        Event newEvent = null;

        try {
            URL url = new URL("https://sharelender.born.ch/CreateEvent");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("PUT");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.connect();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            eventJSON.put("date", simpleDateFormat.format(event.getDate()));
            eventJSON.put("information", event.getInformation());
            schoolClassJSON.put("id", event.getSchoolClass().getId());
            schoolClassJSON.put("name", event.getSchoolClass().getName());
            eventJSON.put("schoolClass", schoolClassJSON);


            // Send PUT output.
            DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream ());
            out.writeBytes(eventJSON.toString());
            out.flush();
            out.close();

            int httpResult = urlConnection.getResponseCode();
            if(httpResult == HttpURLConnection.HTTP_OK){
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(),"utf-8"));
                String next;
                while ((next = in.readLine()) != null){
                    JSONArray jsonArray = new JSONArray(next);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        newEvent = fillEvent(jsonObject);
                    }
                }
                in.close();
            }
        } catch (IOException |JSONException |ParseException e) {
            e.printStackTrace();
        }finally{
            if(urlConnection!=null)
                urlConnection.disconnect();
        }
        return newEvent;
    }

    public Event updateEvent(Event event) {
        HttpURLConnection urlConnection= null;
        JSONObject eventJSON = new JSONObject();
        JSONObject schoolClassJSON = new JSONObject();
        Event newEvent = null;

        try {
            URL url = new URL("https://sharelender.born.ch/UpdateEvent");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("PUT");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.connect();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            eventJSON.put("date", simpleDateFormat.format(event.getDate()));
            eventJSON.put("information", event.getInformation());
            eventJSON.put("id", event.getId());
            schoolClassJSON.put("id", event.getSchoolClass().getId());
            schoolClassJSON.put("name", event.getSchoolClass().getName());
            eventJSON.put("schoolClass", schoolClassJSON);


            // Send PUT output.
            DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream ());
            out.writeBytes(eventJSON.toString());
            out.flush();
            out.close();

            int httpResult = urlConnection.getResponseCode();
            if(httpResult == HttpURLConnection.HTTP_OK){
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(),"utf-8"));
                String next;
                while ((next = in.readLine()) != null){
                    JSONArray jsonArray = new JSONArray(next);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        newEvent = fillEvent(jsonObject);
                    }
                }
                in.close();
            }
        } catch (IOException |JSONException |ParseException e) {
            e.printStackTrace();
        }finally{
            if(urlConnection!=null)
                urlConnection.disconnect();
        }
        return newEvent;
    }

    public Event deleteEvent(Event event) {
        HttpURLConnection urlConnection= null;
        JSONObject eventJSON = new JSONObject();
        JSONObject schoolClassJSON = new JSONObject();
        Event newEvent = null;

        try {
            URL url = new URL("https://sharelender.born.ch/DeleteEvent");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("DELETE");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.connect();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            eventJSON.put("date", simpleDateFormat.format(event.getDate()));
            eventJSON.put("information", event.getInformation());
            eventJSON.put("id", event.getId());
            schoolClassJSON.put("id", event.getSchoolClass().getId());
            schoolClassJSON.put("name", event.getSchoolClass().getName());
            eventJSON.put("schoolClass", schoolClassJSON);


            // Send PUT output.
            DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream ());
            out.writeBytes(eventJSON.toString());
            out.flush();
            out.close();

            int httpResult = urlConnection.getResponseCode();
            if(httpResult == HttpURLConnection.HTTP_OK){
                newEvent = event;
            }
        } catch (IOException |JSONException e) {
            e.printStackTrace();
        }finally{
            if(urlConnection!=null)
                urlConnection.disconnect();
        }
        return newEvent;
    }


}
