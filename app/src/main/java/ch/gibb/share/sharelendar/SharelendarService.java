package ch.gibb.share.sharelendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
                    SchoolClass schoolClass = new SchoolClass();
                    schoolClass.setId(jsonObject.getInt("id"));
                    schoolClass.setName(jsonObject.getString("name"));
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
        try {
            URL url = new URL("https://sharelender.born.ch/CheckAdmin");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            String urlParameters = "username="+admin.getUsername()+"&password="+admin.getPassword();


            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();;

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            if(response.equals("true")){
                result= true;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
