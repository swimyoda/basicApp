package app.thomasgalligani.myapplication;


import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import java.util.ArrayList;


public class MainActivity extends Activity {

    private String outputFile;
    private Resources resources;
    private EditText editText;
    private ArrayList<String> questions = new ArrayList<String>();
    private ArrayList<String> answers = new ArrayList<String>();
    private ArrayList<String> words = new ArrayList<String>();
    private ArrayList<String> questionWords = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resources = getResources();
        editText = (EditText)findViewById(R.id.editText);
        //DownloadTask task = new DownloadTask();
        // task.execute("http://api.openweathermap.org/data/2.5/weather?q=London,uk");
        //task.execute("http://api.openweathermap.org/data/2.5/weather?q={Boston}&APPID=adf401838d67c27778aeefe3f0b9f239");
    }

    public void click(View view)
    {
        String input = getString(R.id.editText);
        String[] keywords = processText(input);
        JSONObject fullFile;
        JSONArray QAs;
        JSONArray answers;

        try {
            outputFile = LoadFile("databaseTest.json", false);
            try {
                fullFile = new JSONObject(outputFile);
                QAs = fullFile.getJSONArray("question-answers");
            } catch (JSONException e) {
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
        try {
            switch (keywords[0]) {
                case "who":
                    answers = QAs.getJSONObject(0).getJSONArray("answers");
                    break;
                case "what":
                    JSONObject categories = QAs.getJSONObject(1).getJSONObject("answers");
                    answers = categories.getJSONObject(keywords[1]).getJSONArray();
                case "how":
                    answers = QAs.getJSONObject(2).getJSONArray("answers");
                    break;
                case "why":
                    answers = QAs.getJSONObject(3).getJSONArray("answers");
                    break;
                case "where":
                    answers = QAs.getJSONObject(4).getJSONArray("answers");
                    break;
                case "when":
                    answers = QAs.getJSONObject(5).getJSONArray("answers");
                    break;

            }
        }
        catch(JSONException e)
        {
            Toast toast = Toast.makeText(this, "JSON is mesed up!", Toast.LENGTH_LONG);
            toast.show();
        }

        JSONObject who;
        JSONArray answersWho;

        JSONObject what;
        JSONArray categoriesWhat;
        ArrayList<ArrayList<JSONArray>> answersWhat = new ArrayList<ArrayList<JSONArray>>();

        JSONObject how;
        JSONArray answersHow;

        JSONObject why;
        JSONArray answersWhy;

        JSONObject where;
        JSONArray answersWhere;

        JSONObject when;
        JSONArray answersWhen;
        try
        {
            //Load the file from assets folder - don't forget to INCLUDE the extension
            output = LoadFile("databaseTest.json", false);
            //output to LogCat
            Log.i("test", output);
            try {
                fullFile = new JSONObject(output);
                Log.i("try", fullFile.toString());
                QAs = fullFile.getJSONArray("question-answers");





                Log.i("try", QAs.toString());
                who = QAs.getJSONObject(0);
                what = QAs.getJSONObject(1);
                categoriesWhat = what.getJSONArray("answers");
                for(JSONArray a: categoriesWhat)
                {
                    for(JSONArray b: a)
                    {
                        answersWhat<JSONArray>.add(b);
                    }
                }/*
                how = QAs.getJSONObject(2);
                why = QAs.getJSONObject(3);
                where = QAs.getJSONObject(4);
                when = QAs.getJSONObject(5);
                Log.i("try", who.toString());
                answers = who.getJSONArray("answers");*/
                for(ArrayList<JSONArray> q: answersWhat)
                {
                    for(JSONArray j: q)
                        Log.i("try", q.toString());
                }

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


    //done--------------
    private String[] processText(String input)
    {
        ArrayList<String> wordified = wordify(input);
        String question = findKeyword(questions, wordified);
        String[] output = new String[2];
        switch(question)
        {
            case "what":
                output[0] = question;
                output[1] = findKeyword(wordified, whatWords);
                break;
            case default:
                output[0] = question;
                output[1] = null;
                break;
            return output;
        }
    }

    private ArrayList<String> wordify(String sentence)
    {
        ArrayList<String> words = new ArrayList<String>();
        int start = 0;
        for(int i = 0; i < sentence.length(); i++)
        {
            if(sentence.charAt(i) == " " || i == sentence.length() - 1)
            {
                words.add(sentence.substring(start, i).toLowerCase());
                i++;
                start = i;
            }
        }
        return words;
    }

    public String findKeyword(ArrayList<String> words, ArrayList<String> arr)
    {
        for(String word: arr)
        {
            for(String w: words)
            {
                if(word.equals(w))
                {
                    return word;
                }
            }
        }
        return null;
    }

    public ArrayList<String> howQuestion()
    {
        ArrayList<String> answers = new ArrayList<String>();
        answers.add("Good!");
        answers.add("Okay");
        answers.add("Bad!");
        return answers;
    }
    public ArrayList<String> doQuestion()
    {
        ArrayList<String> answers = new ArrayList<String>();
        answers.add("Yes");
        answers.add("Maybe");
        answers.add("No");
        return answers;
    }
    public ArrayList<String> whenQuestion()
    {
        ArrayList<String> answers = new ArrayList<String>();
        answers.add("Yesterday");
        for(int i = 1; i <= 12; i++){
            answers.add(i + " o'clock");
        }
        answers.add("Tomorrow");
        return answers;
    }
    public ArrayList<String> whatQuestion(ArrayList<String> s)
    {
        String keyword = findKeyword(s, words);
        int index;
        for(int i = 0; i < questions.size(); i++){
            if (questions.get(i).contains(keyword) == true){
                index = i;
                break;
            }
        }
        return answers.get(index);
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
/*
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
*/



}
