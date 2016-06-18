package app.thomasgalligani.myapplication;


import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.io.FileReader;
import java.io.BufferedReader;


public class MainActivity extends Activity {

    private String output;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resources = getResources();

        DownloadTask task = new DownloadTask();
        // task.execute("http://api.openweathermap.org/data/2.5/weather?q=London,uk");
        task.execute("http://api.openweathermap.org/data/2.5/weather?q={Boston}&APPID=adf401838d67c27778aeefe3f0b9f239");
    }

    public void click(View view)
    {
        JSONObject json;
        JSONArray QAs;
        JSONObject who;
        JSONArray answers;
        try
        {
            //Load the file from assets folder - don't forget to INCLUDE the extension
            output = LoadFile("databaseTest.json", false);
            //output to LogCat
            Log.i("test", output);
            try {
                json = new JSONObject(output);
                Log.i("try", json.toString());
                QAs = json.getJSONArray("question-answers");
                Log.i("try", QAs.toString());
                who = QAs.getJSONObject(0);
                Log.i("try", who.toString());
                answers = who.getJSONArray("answers");
                Log.i("try", answers.toString());

            }
            catch(JSONException e)
            {
                Toast toast = Toast.makeText(this, "JSON is mesed up!", Toast.LENGTH_LONG);
                toast.show();
            }

        }
        catch (IOException e)
        {
            //display an error toast message
            Toast toast = Toast.makeText(this, "File: not found!", Toast.LENGTH_LONG);
            toast.show();
        }



    }

    public String LoadFile(String fileName, boolean loadFromRawFolder) throws IOException
    {
        //Create a InputStream to read the file into
        InputStream iS;

        //get the file as a stream
        iS = resources.getAssets().open(fileName);


        //create a buffer that has the same size as the InputStream
        byte[] buffer = new byte[iS.available()];
        //read the text file as a stream, into the buffer
        iS.read(buffer);
        //create a output stream to write the buffer into
        ByteArrayOutputStream oS = new ByteArrayOutputStream();
        //write this buffer to the output stream
        oS.write(buffer);
        //Close the Input and Output streams
        oS.close();
        iS.close();

        //return the output stream as a String
        return oS.toString();
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                }

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                JSONObject jsonObject = new JSONObject(result);
                System.out.println("added1: " + jsonObject);
                String weatherInfo = jsonObject.getString("weather");

                System.out.println("added2: " + weatherInfo);
                Log.i("Weather content", weatherInfo);

                JSONArray arr = new JSONArray(weatherInfo);

                System.out.println("added3: " + arr);

                for (int i = 0; i < arr.length(); i++) {

                    JSONObject jsonPart = arr.getJSONObject(i);
                    System.out.println("added " + i + ": " + jsonPart);
                    Log.i("main", jsonPart.getString("main"));
                    Log.i("description", jsonPart.getString("description"));

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }




}
