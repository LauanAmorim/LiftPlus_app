package com.example.liftplus;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NetworkUtils
{
    private static final String API_URL = "https://openlibrary.org/search.json";
    private static final String QUERY_PARAM = "q";

    private static URL buildUrl(String exercicioSearchQuery){
        Uri buildUri = Uri.parse(API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, exercicioSearchQuery)
                .build();

        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String getExerciseFromHttpUrl (URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            } else {
                return null;
            }
        }
        finally {
            urlConnection.disconnect();
        }
    }

    private static List<exercicioRepository> jsonFormatter(String jsonResponse){
        List<exercicioRepository> exercicioRepositoryList = new ArrayList<>();
        try{
            JSONObject json = new JSONObject(jsonResponse);

            int dataLength = 50;
            if(json.length() < dataLength) {
                dataLength = json.length();
            }

            for(int i = 0; i<dataLength; i++) {
                JSONObject currentExercicio = json.getJSONObject(String.valueOf(i));
                String id = currentExercicio.getString("id");
                String nome = currentExercicio.getString("nome");
                String grupoMuscular = currentExercicio.getString("musculo");

                Log.v("Data", "Number" + 1);

                exercicioRepository exercicio = new exercicioRepository(id, nome, grupoMuscular);

                exercicioRepositoryList.add(exercicio);
            }
        }catch (JSONException ex) {
            Log.v("Network", "can't read json");
        }
        return exercicioRepositoryList;
    }

    public static List<exercicioRepository> getDataFromApi (String query) throws IOException{
        URL apiURl = buildUrl(query);
        String jsonResponse = getExerciseFromHttpUrl(apiURl);
        List<exercicioRepository> exercicioRepositoryList = jsonFormatter(jsonResponse);
        return  exercicioRepositoryList;


    }
}
