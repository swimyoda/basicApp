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
    private Button button1;
    private String b1 = "";
    private Button button2;
    private String b2 = "";
    private Button button3;
    private String b3 = "";
    private Button button4;
    private String b4 = "";
    private Button speakButton;
    private Toast toast;
    private ArrayList<String> words = new ArrayList<String>();
    private ArrayList<String> questionWords = new ArrayList<String>();
    private ArrayList<String> whatWords = new ArrayList<String>();
    private String speachText;
    private TextToSpeech voice;
    private String[] currentQuestionWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resources = getResources();

        voice = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    voice.setLanguage(Locale.US);
                }
            }
        });

        editText = (EditText) findViewById(R.id.editText);//Text field on top of display
        button1 = (Button) (findViewById(R.id.Button1));
        button2 = (Button) (findViewById(R.id.Button2));
        button3 = (Button) (findViewById(R.id.Button2));
        button4 = (Button) (findViewById(R.id.Button2));//answer choice buttons
        speakButton = (Button) (findViewById(R.id.talk));//button to activate voice recognition

        try {
            outputFile = LoadFile("databaseTest.json", false);

            //breaks down full JSON file to find the subcategories of what currently stored in the database
            JSONObject fullFile = new JSONObject(outputFile);
            JSONArray QAs = fullFile.getJSONArray("question-answers");
            JSONArray whatWordsJ = QAs.getJSONObject(1).getJSONArray("answers");
            for (int i = 0; i < whatWordsJ.length(); i++) {//finds the subcategory which is stored as the last element of the the possible answers array
                whatWords.add(whatWordsJ.getJSONArray(i).getString(4));
                Log.i("What words", whatWordsJ.getJSONArray(i).getString(4));
            }
         } catch (JSONException e) {
            //Log.i("something else", "couldn't read");
        }
        catch(IOException e)
        {
            //Log.i("something else", "no file");
        }
        questionWords.add("who");
        questionWords.add("what");
        questionWords.add("where");
        questionWords.add("when");
        questionWords.add("why");
        questionWords.add("how");
    }

    //onCLick for the first button
    public void answer1(View view){
        //highlights button 1
        ((Button)(findViewById(R.id.Button1))).setBackgroundColor(0xA11D79D5);
        ((Button)(findViewById(R.id.Button2))).setBackgroundColor(0x00000000);
        ((Button)(findViewById(R.id.Button3))).setBackgroundColor(0x00000000);
        ((Button)(findViewById(R.id.Button4))).setBackgroundColor(0x00000000);

        //reads the selected answer aloud
        try {
            voice.speak(((Button)(findViewById(R.id.Button1))).getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
        }
        catch(Exception e) {
            toast = Toast.makeText(this, "Error: 1", Toast.LENGTH_LONG);
        }
    }

    //onCLick method for button 2
    public void answer2(View view){
        //highlights button 2
        ((Button)(findViewById(R.id.Button2))).setBackgroundColor(0xA11D79D5);
        ((Button)(findViewById(R.id.Button1))).setBackgroundColor(0x00000000);
        ((Button)(findViewById(R.id.Button3))).setBackgroundColor(0x00000000);
        ((Button)(findViewById(R.id.Button4))).setBackgroundColor(0x00000000);

        //reads the selected answer aloud
        try {
            voice.speak(((Button)(findViewById(R.id.Button2))).getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
        }
        catch(Exception e) {
            toast = Toast.makeText(this, "Error: 1", Toast.LENGTH_LONG);
        }
    }

    //onClick method for button 3
    public void answer3(View view){
        //highlights button 3
        ((Button)(findViewById(R.id.Button3))).setBackgroundColor(0xA11D79D5);
        ((Button)(findViewById(R.id.Button1))).setBackgroundColor(0x00000000);
        ((Button)(findViewById(R.id.Button2))).setBackgroundColor(0x00000000);
        ((Button)(findViewById(R.id.Button4))).setBackgroundColor(0x00000000);

        //reads the selected answer aloud
        try {
            voice.speak(((Button)(findViewById(R.id.Button3))).getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
        }
        catch(Exception e) {
            toast = Toast.makeText(this, "Error: 1", Toast.LENGTH_LONG);
        }
    }

    //onClick method for button 4
    public void answer4(View view){
        //highlights button 4
        ((Button)(findViewById(R.id.Button4))).setBackgroundColor(0xA11D79D5);
        ((Button)(findViewById(R.id.Button1))).setBackgroundColor(0x00000000);
        ((Button)(findViewById(R.id.Button2))).setBackgroundColor(0x00000000);
        ((Button)(findViewById(R.id.Button3))).setBackgroundColor(0x00000000);

        //reads teh selected answer aloud
        try {
            voice.speak(((Button)(findViewById(R.id.Button4))).getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
        }
        catch(Exception e) {
            toast = Toast.makeText(this, "Error: 1", Toast.LENGTH_LONG);
        }
    }

    //onCLick method for speak button
    public void speak(View view){
        ((Button)(findViewById(R.id.Button1))).setBackgroundColor(0x00000000);
        ((Button)(findViewById(R.id.Button2))).setBackgroundColor(0x00000000);
        ((Button)(findViewById(R.id.Button3))).setBackgroundColor(0x00000000);
        ((Button)(findViewById(R.id.Button4))).setBackgroundColor(0x00000000);
        promptSpeechInput();
    }

    //called once a question has been heard by the voice recognition
    private void interperateQuestion() {

        try {//catches a null pointer exception
            String input= speachText;
            //displays the question in the text field
            editText.setText(speachText);

            String[] keywords = processText(input);//finds 2 keywords, the first being the question word, the second being the subcategory (if applicable)

            JSONObject fullFile;
            JSONArray QAs;
            JSONArray answers;
            try {//catches a problem reading the file
                outputFile = LoadFile("databaseTest.json", false);
                try {//catches a JSON formating problem in the database

                    //breaks down the database into the array of possible question types
                    fullFile = new JSONObject(outputFile);
                    QAs = fullFile.getJSONArray("question-answers");

                    answers = new JSONArray();
                    //breaks finds the answers based on the kind of question
                    switch (keywords[0]) {
                        case "who":
                            answers = QAs.getJSONObject(0).getJSONArray("answers");
                            break;
                        case "what"://a special case, since there are subcategories within this question type
                            JSONArray categories = QAs.getJSONObject(1).getJSONArray("answers");
                            answers = categories.getJSONArray(Integer.parseInt(keywords[1].substring(keywords[1].length() - 1)));
                            //Toast.makeText(this, answers.toString(), Toast.LENGTH_LONG)show();
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

                    //sets each button to a possible answers
                    Button button1 = (Button) findViewById(R.id.Button1);
                    button1.setText(answers.get(0).toString());
                    Button button2 = (Button) findViewById(R.id.Button2);
                    button2.setText(answers.get(1).toString());
                    Button button3 = (Button) findViewById(R.id.Button3);
                    button3.setText(answers.get(2).toString());
                    Button button4 = (Button) findViewById(R.id.Button4);
                    button4.setText(answers.get(3).toString());

                } catch (JSONException e) {
                    Toast.makeText(this, "Error:2", Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                Toast.makeText(this, "Error: 3", Toast.LENGTH_LONG).show();
            }
        }
        catch(NullPointerException e) {
            Toast.makeText(MainActivity.this, "Error: 4", Toast.LENGTH_LONG).show();
        }
    }


    //Finds the one or two keywords in the parameter and returns them in an array
    //The second element will be returned as NULL if the question type does not necessitate a subcategory
    private String[] processText(String input) {

        ArrayList<String> wordified = wordify(input);//changes the question from a multi-word string to an array of on-word strings
        String question = findKeyword(questionWords, wordified);//finds the question type based on the question word found in the wordified array
        question = question.substring(0,question.length() - 1);//removes the index from the end of the question word that was added as part of the findKeyword method
        String[] output = new String[2];
        switch (question) {
            case "what":
                output[0] = question;
                output[1] = findKeyword(wordified, whatWords);
                break;
            case "who":
            case "how":
            case "where":
            case "when":
            case "why":
                output[0] = question;
                output[1] = null;//who, how, where, when, and why all have no subcategories, so the second keyword slot it left null
                break;
        }
        currentQuestionWord = output;
        //Log.i("output1", output[1]);
        //Log.i("ouput", output[0] + output[1]);
        return output;
    }

    //a method to convert a question from a single string into an arraylist of strings, one for each word
    private ArrayList<String> wordify(String sentence)
    {
        ArrayList<String> words = new ArrayList<String>();
        int start = 0;
        for (int i = 0; i <= sentence.length(); i++) {
            if (sentence.charAt(i) == '?' || sentence.charAt(i) == ' ') //ends the next word when it finds a space or a question mark
            {
                words.add(sentence.substring(start, i).toLowerCase());
                i++;
                start = i;
            }
        }
        return words;
    }

    //method to find a keyword from the "arr" parameter inside the "words" parameter
    public String findKeyword(ArrayList<String> words, ArrayList<String> arr)
    {
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

    //method to load the database text file
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
    //method to initiate the speach recognition software
    public void promptSpeechInput()
    {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something"); //prompts the user to ask their question

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
                    interperateQuestion();
                }
                break;
        }
    }
/*
    public void addAnswer(View view) {
        int index;
        Log.i("write", "1");
        View view2 = this.getCurrentFocus();
        Log.i("write", "2");
        if (view2 != null) {
            Log.i("write", "3");
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        String answer = editText.getText().toString();
        Log.i("write", "4");
        index = outputFile.indexOf("question-word\": \"" + currentQuestionWord[0]);
        Log.i("write", "5");
        if(currentQuestionWord[1]!=null)
        {
            index = outputFile.indexOf(", \"" + currentQuestionWord[1] + "\"]");
        }
        else
        {
            Log.i("write", "6");
            String after = outputFile.substring(index);
            index = after.indexOf("[");
            Log.i("write", "7");
            String before = outputFile.substring(0, index);
            Log.i("write", "8");
            after = outputFile.substring(index);
            Log.i("write", "9");
            outputFile = before+"\""+answer+"\","+after;
        }
        try
        {
            PrintWriter writer = new PrintWriter("basicApp-master/app/src/main/res/databaseTest.json");
            Log.i("write", "10");
            writer.print(outputFile);
        }
        catch(java.io.FileNotFoundException e)
        {
            toast = Toast.makeText(this, "Error: 5", Toast.LENGTH_LONG);
        }

        speakButton.setVisibility(View.VISIBLE);
    }*/

    //method to empty the text field
    public void deleteText(View view)
    {
        editText.setText("");
    }

}
