package app.thomasgalligani.myapplication;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;



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
        SmsManager smsManager = SmsManager.getDefault();


        try{

            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                    + "+19789181911")));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + "+19789181911"));
            intent.putExtra("sms_body", "This works dipshit");
            startActivity(intent);

        }
        catch(Exception f) {
            Log.i("Error", "This is what is wrong");
        }

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(location);

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };
        Toast toast;
        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
        catch(SecurityException s) {
            toast = Toast.makeText(this, "Location services are currently offline", Toast.LENGTH_LONG);
        }
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
                            //toast = Toast.makeText(this, answers.toString(), Toast.LENGTH_LONG);
                            //toast.show();
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
        catch(NullPointerException e) {
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
        Log.i("question", "1");
        switch (question) {
            case "what":
                output[0] = question;
                Log.i("question", "2");
                output[1] = findKeyword(wordified, whatWords);
                Log.i("question", "3");
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
        Log.i("question", "4");
        //Log.i("output1", output[1]);
        //Log.i("ouput", output[0] + output[1]);
        Log.i("question", "5");
        return output;
    }

    private ArrayList<String> wordify(String sentence) {
        ArrayList<String> words = new ArrayList<String>();
        int start = 0;
        for (int i = 0; i <= sentence.length(); i++) {
            if (sentence.charAt(i) == '?' || sentence.charAt(i) == ' ') {
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
                Log.i("matches",word + " " + w);
                if (word.equals(w)) {
                    return word+arrInd;
                }
            }
        }
        return null;
    }

  

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
                    speachText = result.get(0).substring(0, 1).toUpperCase() + result.get(0).substring(1)+"?";
                    click();
                }
                break;
        }
    }
    public void makeUseOfNewLocation(Location l) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                + "+19789181911")));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + "+19789181911"));
        intent.putExtra("sms_body", l.toString());
        startActivity(intent);
    }

}
