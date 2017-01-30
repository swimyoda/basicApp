package app.thomasgalligani.myapplication;


import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.net.*;


public class MainActivity extends Activity {

    private String outputFile;
    private Resources resources;
    private TextView textView;
    private EditText usernameBox;
    private EditText passwordBox;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button speakButton;
    private Button enterButton;
    private Button loginButton;
    private ArrayList<String> words = new ArrayList<String>();
    private ArrayList<String> questionWords = new ArrayList<String>();
    private ArrayList<String> whatWords = new ArrayList<String>();
    private String speachText;
    private String username;
    private String password;
    private URL site;
    private FakeServer server;
    TextToSpeech voice;
    String b1 = "";
    String b2 = "";
    String b3 = "";
    String b4 = "";
    String[] currentQuestionWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resources = getResources();
        try {
            site = new URL("https//:www.google.com");
        }
        catch(MalformedURLException e)
        {
            Toast.makeText(getApplicationContext(), "Could not connect to the server", Toast.LENGTH_LONG).show();
        }

        server = new FakeServer();//fake server

        voice =new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    voice.setLanguage(Locale.US);
                }
            }
        });
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        MyCurrentLocationListener locationListener = new MyCurrentLocationListener();
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        catch (SecurityException f) {
            Toast.makeText(getApplicationContext(), "Please enable location services for this app in your settings",
                    Toast.LENGTH_LONG).show();
        }
        try{


            sendSMS("+19788867847", "");
        }
        catch(Exception f) {
            Log.i("Error", "This is what is wrong");
        }

        textView = (TextView)(findViewById(R.id.textView));
        button1 = (Button)(findViewById(R.id.Button1));
        button2 = (Button)(findViewById(R.id.Button2));
        button3 = (Button)(findViewById(R.id.Button3));
        button4 = (Button)(findViewById(R.id.Button4));
        speakButton = (Button)(findViewById(R.id.talk));
        enterButton = (Button)(findViewById(R.id.enter));
        loginButton = (Button)(findViewById(R.id.loginButton));
        passwordBox = (EditText)(findViewById(R.id.password));
        usernameBox = (EditText)(findViewById(R.id.username));


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
        questionWords.add("whom");
        questionWords.add("which");
        questionWords.add("wherefore");
        questionWords.add("will");
        questionWords.add("do");
        questionWords.add("shall");
        questionWords.add("should");
        questionWords.add("could");


    }
    public void sendSMS(String phoneNo, String msg)
    {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void connect()throws IOException
    {
        URLConnection connect = site.openConnection();
        connect.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

    }

    public void login(View view) {
        String name = "";
        do{
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(passwordBox.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            loginButton.setVisibility(View.INVISIBLE);
            username = usernameBox.getText().toString();
            usernameBox.setText("");
            usernameBox.setVisibility(View.INVISIBLE);
            password = passwordBox.getText().toString();
            passwordBox.setText("");
            passwordBox.setVisibility(View.INVISIBLE);


            name = server.check(username, password);
            if (!name.equals("")) {
                button1.setVisibility(View.VISIBLE);
                button2.setVisibility(View.VISIBLE);
                button3.setVisibility(View.VISIBLE);
                button4.setVisibility(View.VISIBLE);
                speakButton.setVisibility(View.VISIBLE);
                textView.setText("Welcome to PEC, " + name + ", click the SPEAK button to start");
                voice.speak("Welcome to PEC, " + name + ", click the SPEAK button to start", TextToSpeech.QUEUE_FLUSH, null);
            }
        }while(name.equals(""));
    }


    public void answer1(View view){
        Toast toast;
        ((Button)(findViewById(R.id.Button1))).setBackgroundColor(0xA11D79D5);
        ((Button)(findViewById(R.id.Button2))).setBackgroundColor(0x00000000);
        ((Button)(findViewById(R.id.Button3))).setBackgroundColor(0x00000000);
        ((Button)(findViewById(R.id.Button4))).setBackgroundColor(0x00000000);
        try {
            voice.speak(((Button)(findViewById(R.id.Button1))).getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
        }
        catch(Exception e) {
            toast = Toast.makeText(this, "", Toast.LENGTH_LONG);
        }
    }

    public void answer2(View view){
        Toast toast;
        ((Button)(findViewById(R.id.Button2))).setBackgroundColor(0xA11D79D5);
        ((Button)(findViewById(R.id.Button1))).setBackgroundColor(0x00000000);
        ((Button)(findViewById(R.id.Button3))).setBackgroundColor(0x00000000);
        ((Button)(findViewById(R.id.Button4))).setBackgroundColor(0x00000000);
        try {
            voice.speak(((Button)(findViewById(R.id.Button2))).getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
        }
        catch(Exception e) {
            toast = Toast.makeText(this, "", Toast.LENGTH_LONG);
        }
    }
    public void answer3(View view){
        Toast toast;
        ((Button)(findViewById(R.id.Button3))).setBackgroundColor(0xA11D79D5);
        ((Button)(findViewById(R.id.Button1))).setBackgroundColor(0x00000000);
        ((Button)(findViewById(R.id.Button2))).setBackgroundColor(0x00000000);
        ((Button)(findViewById(R.id.Button4))).setBackgroundColor(0x00000000);
        try {
            voice.speak(((Button)(findViewById(R.id.Button3))).getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
        }
        catch(Exception e) {
            toast = Toast.makeText(this, "", Toast.LENGTH_LONG);
        }
    }
    public void answer4(View view){
        Toast toast;
        ((Button)(findViewById(R.id.Button4))).setBackgroundColor(0xA11D79D5);
        ((Button)(findViewById(R.id.Button1))).setBackgroundColor(0x00000000);
        ((Button)(findViewById(R.id.Button2))).setBackgroundColor(0x00000000);
        ((Button)(findViewById(R.id.Button3))).setBackgroundColor(0x00000000);
        String buttonText = ((Button)(findViewById(R.id.Button4))).getText().toString();
        String addWhat = buttonText.substring(buttonText.indexOf("ADD")+4);
        if(buttonText.contains("ADD"))
        {
        }
        else
        {
            try {
                voice.speak(buttonText, TextToSpeech.QUEUE_FLUSH, null);
            }
            catch(Exception e) {
                toast = Toast.makeText(this, "", Toast.LENGTH_LONG);
            }
        }

    }
    public void ask(View view){
        Intent intent = new Intent();
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification noti = new Notification.Builder(this)
        .setTicker("Help")
        .setContentTitle("I've been abducted")
        .setContentText("Yo")
        .setSmallIcon(R.mipmap.download)
        .setContentIntent(pIntent).getNotification();
        ((Button)(findViewById(R.id.Button1))).setBackgroundColor(0x00000000);
        ((Button)(findViewById(R.id.Button2))).setBackgroundColor(0x00000000);
        ((Button)(findViewById(R.id.Button3))).setBackgroundColor(0x00000000);
        ((Button)(findViewById(R.id.Button4))).setBackgroundColor(0x00000000);
        promptSpeechInput();
    }

    public void click() {
        try {
            String input;
            input = speachText;
            textView.setText(speachText);
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
                        case "whom":
                            answers = QAs.getJSONObject(0).getJSONArray("answers");
                            break;
                        case "what":
                        case "which":
                            JSONArray categories = QAs.getJSONObject(1).getJSONArray("answers");
                            if(keywords[1]!="")
                                answers = categories.getJSONArray(Integer.parseInt(keywords[1].substring(keywords[1].length() - 1)));

                            //toast = Toast.makeText(this, answers.toString(), Toast.LENGTH_LONG);
                            //toast.show();
                            break;
                        case "how":
                            answers = QAs.getJSONObject(2).getJSONArray("answers");
                            break;
                        case "why":
                        case "wherefore":
                            answers = QAs.getJSONObject(3).getJSONArray("answers");
                            break;
                        case "where":
                            answers = QAs.getJSONObject(4).getJSONArray("answers");
                            break;
                        case "when":
                            answers = QAs.getJSONObject(5).getJSONArray("answers");
                            break;
                        case "will":
                        case "do":
                        case "shall":
                        case "should":
                        case "could":
                            String[] a = {"Yes", "No", "Maybe", "I don't know"};
                            answers = new JSONArray(a);
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
            textView.setText("Try again, I couldn't hear a question");
            voice.speak("Try again, I couldn't hear a question", TextToSpeech.QUEUE_FLUSH, null);
            Button button1 = (Button) findViewById(R.id.Button1);
            button1.setText("");
            Button button2 = (Button) findViewById(R.id.Button2);
            button2.setText("");
            Button button3 = (Button) findViewById(R.id.Button3);
            button3.setText("");
            Button button4 = (Button) findViewById(R.id.Button4);
            button4.setText("");
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
        output[0] = "";
        Log.i("question", "1");
        switch (question) {
            case "what":
            case "which":
                output[0] = question;
                Log.i("question", "2");
                output[1] = findKeyword(wordified, whatWords);
                if(output[1]==null)
                {
                    output[1] = "";
                }
                Log.i("question", "3");
                break;
            case "who":
            case "whom":
            case "how":
            case "where":
            case "when":
            case "why":
            case "wherefore":
            case "will":
            case "do":
            case "shall":
            case "should":
            case "could":
                output[0] = question;
                output[1] = null;
                break;
        }
        currentQuestionWord = output;
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
            if (sentence.charAt(i) == '?' || sentence.charAt(i) == ' ' || sentence.charAt(i) == '\'') {
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

    public void addAnswer(View view) {
        int index;
        View view2 = this.getCurrentFocus();
        if (view2 != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        String answer = textView.getText().toString();
        index = outputFile.indexOf("question-word\": \"" + currentQuestionWord[0]);
        if(currentQuestionWord[1]!=null)
        {
            index = outputFile.indexOf(", \"" + currentQuestionWord[1] + "\"]");
        }
        else
        {
            String after = outputFile.substring(index);
            index = after.indexOf("]");
            String before = outputFile.substring(0, index);
             after = outputFile.substring(index);
            outputFile = "";
        }

        enterButton.setVisibility(View.GONE);
        speakButton.setVisibility(View.VISIBLE);
    }

    public void deleteText(View view)
    {
        textView.setText("");
    }

}
 class MyCurrentLocationListener implements android.location.LocationListener {
    @Override
    public  void onLocationChanged(Location location) {
        location.getLatitude();
        location.getLongitude();

        String myLocation = "Latitude = " + location.getLatitude() + " Longitude = " + location.getLongitude();

        //I make a log to see the results
        Log.e("MY CURRENT LOCATION", myLocation);
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+19788867847", null, myLocation, null, null);

        } catch (Exception ex) {
            Log.e("no", "sorry");
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}
 class FakeServer
 {
     private String[] passwords;
     private String[] usernames;
     private String[] names;

     public FakeServer()
     {
         passwords = new String[3];
         usernames = new String[3];
         names = new String[3];
         for(int i=0; i<3; i++)
         {
             passwords[i] = "password";
             usernames[i] = "name";
             names[i] = "Joe" + i;
         }
     }

     public String check(String usrnm, String pswrd)
     {
         int i = 0;
         for (String username:usernames)
         {
             for(String password:passwords)
             {
                 if(username.equals("usrnm")&&password.equals(pswrd))
                     return names[i];
             }
             i++;
         }
        return "";
     }
 }
