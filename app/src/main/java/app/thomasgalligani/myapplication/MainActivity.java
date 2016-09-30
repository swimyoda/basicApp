package app.thomasgalligani.myapplication;


import android.app.Activity;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.content.ActivityNotFoundException;
import android.support.v7.app.AppCompatActivity;


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
import java.util.List;
import android.widget.RemoteViews;



public class MainActivity extends Activity {

    private String outputFile;
    private Resources resources;
    private EditText editText;
    private ArrayList<String> words = new ArrayList<String>();
    private ArrayList<String> questionWords = new ArrayList<String>();
    private ArrayList<String> whatWords = new ArrayList<String>();
    private String speachText;
    TextToSpeech voice;
    String b1 = "";
    String b2 = "";
    String b3 = "";
    String b4 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resources = getResources();
        voice =new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    voice.setLanguage(Locale.US);
                }
            }
        });
        editText = (EditText) findViewById(R.id.editText);

        try {
            outputFile = LoadFile("databaseTest.json", false);
            try {
                JSONObject fullFile = new JSONObject(outputFile);
                JSONArray QAs = fullFile.getJSONArray("question-answers");
                JSONArray whatWordsJ = QAs.getJSONObject(1).getJSONArray("answers");
                for (int i = 0; i < whatWordsJ.length(); i++) {
                    whatWords.add(whatWordsJ.getJSONArray(i).getString(4));
                    Log.i("something",whatWordsJ.getJSONArray(i).getString(4));
                }

            } catch (JSONException e) {
                Log.i("something else", "couldn't read");
            }
        } catch (IOException e) {
        }


        questionWords.add("who");
        questionWords.add("what");
        questionWords.add("where");
        questionWords.add("when");
        questionWords.add("why");
        questionWords.add("how");


    }
    public void answer1(View view){
        Toast toast;
        try {
            voice.speak(((Button)(findViewById(R.id.Button1))).getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
        }
        catch(Exception e) {
            toast = Toast.makeText(this, "you are a dumbass", Toast.LENGTH_LONG);
        }
        }

    public void answer2(View view){
        Toast toast;
        try {
            voice.speak(((Button)(findViewById(R.id.Button2))).getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
        }
        catch(Exception e) {
            toast = Toast.makeText(this, "you are a dumbass", Toast.LENGTH_LONG);
        }
    }
    public void answer3(View view){
        Toast toast;
        try {
            voice.speak(((Button)(findViewById(R.id.Button3))).getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
        }
        catch(Exception e) {
            toast = Toast.makeText(this, "you are a dumbass", Toast.LENGTH_LONG);
        }
    }
    public void answer4(View view){
        Toast toast;
        try {
            voice.speak(((Button)(findViewById(R.id.Button4))).getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
        }
        catch(Exception e) {
            toast = Toast.makeText(this, "you are a dumbass", Toast.LENGTH_LONG);
        }
    }
    public void ask(View view){
        promptSpeechInput();
    }

    public void click() {
        try {
            String input;
            input = editText.getText().toString();
            input = speachText;
            editText.setText(speachText);
            Toast toast;
            String[] keywords = processText(input);
            JSONObject fullFile;
            JSONArray QAs;
            JSONArray answers;


            try {
                outputFile = LoadFile("databaseTest.json", false);
                try {
                    fullFile = new JSONObject(outputFile);
                    QAs = fullFile.getJSONArray("question-answers");
                    answers = QAs.getJSONObject(0).getJSONArray("answers");
                    switch (keywords[0]) {
                        case "who":
                            answers = QAs.getJSONObject(0).getJSONArray("answers");
                            break;
                        case "what":
                            JSONArray categories = QAs.getJSONObject(1).getJSONArray("answers");
                            answers = categories.getJSONArray(Integer.parseInt(keywords[1].substring(keywords[1].length() - 1)));
                            Log.i("Answers", answers.toString());
                            break;
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


                    GridLayout grid = (GridLayout) findViewById(R.id.gridLayout);

                    Button button1 = (Button) findViewById(R.id.Button1);
                    button1.setText(answers.get(0).toString());
                    Button button2 = (Button) findViewById(R.id.Button2);
                    button2.setText(answers.get(1).toString());
                    Button button3 = (Button) findViewById(R.id.Button3);
                    button3.setText(answers.get(2).toString());
                    Button button4 = (Button) findViewById(R.id.Button4);
                    button4.setText(answers.get(3).toString());




                } catch (JSONException e) {
                    toast = Toast.makeText(this, "JSON is mesed up!", Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (IOException e) {
                //display an error toast message
                toast = Toast.makeText(this, "File: not found!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
        catch(Exception e) {
            Toast.makeText(MainActivity.this, "Please ask a question first", Toast.LENGTH_LONG).show();
        }


        JSONObject who;
        JSONArray answersWho;


    }


    //done--------------
    private String[] processText(String input) {
        ArrayList<String> wordified = wordify(input);
        String question = findKeyword(questionWords, wordified);
        question = question.substring(0,question.length() - 1);
        Log.i("question", question);
        String[] output = new String[2];
        switch (question) {
            case "what":
                output[0] = question;
                output[1] = findKeyword(wordified, whatWords);
                Log.i("key", output[1]);
                break;
            case "who":
            case "how":
            case "where":
            case "when":
            case "why":
                output[0] = question;
                output[1] = null;
                break;
        }
        Log.i("ouput", output[0] + output[1]);
        return output;
    }

    private ArrayList<String> wordify(String sentence) {
        ArrayList<String> words = new ArrayList<String>();
        int start = 0;
        for (int i = 0; i < sentence.length(); i++) {
            if (sentence.charAt(i) == ' ' || i == sentence.length() - 1) {
                words.add(sentence.substring(start, i).toLowerCase());
                i++;
                start = i;
            }
        }
        return words;
    }

    public String findKeyword(ArrayList<String> words, ArrayList<String> arr) {
        int arrInd = -1;
        for (String word : arr) {
            arrInd++;
            for (String w : words) {
                if (word.equals(w)) {
                    return word+arrInd;
                }
            }
        }
        return null;
    }

   /* public ArrayList<String> whatQuestion(ArrayList<String> s)
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
    }*/

    public String LoadFile(String fileName, boolean loadFromRawFolder) throws IOException {
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


    public void promptSpeechInput()
    {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something");

        try
        {
            startActivityForResult(i, 100);
        }
        catch (ActivityNotFoundException e)
        {
            Toast.makeText(MainActivity.this, "Sorry, your device doesn't support speech-language", Toast.LENGTH_LONG).show();
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent i)
    {
        super.onActivityResult(requestCode, resultCode, i);
        switch(requestCode)
        {
            case 100:
                if (resultCode == RESULT_OK && i != null)
                {
                    ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    speachText = result.get(0);
                    click();
                }
                break;
        }
    }

}
