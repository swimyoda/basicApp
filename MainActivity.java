
//this is the one that attempts to make and destroy buttons


package com.example.nick.basicapp;

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


import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Resources resources;
    private String output;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resources = getResources();

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

    public void processText(View view)
    {
        EditText text = (EditText)findViewById(R.id.query);

    }

    /*
     * changes the input from sentance form into an arraylist of indevidual words
     */
    private ArrayList<String> wordify(String sentence)
    {
        ArrayList<String> words1 = new ArrayList<String>();
        int start = 0;
        for(int i = 0; i < sentence.length(); i++)
        {
            if((sentence.charAt(i) == ' ' || i == sentence.length()-1) || sentence.charAt(i) == '?')
            {
                words1.add(sentence.substring(start,i).toLowerCase());
                i++;
                start = i;
            }
        }
        return words1;
    }

    /*
    *   Comparing individual words with inputted array of words to check (returns word if match, returns null if no match)
    */
    public String findKeyword(ArrayList<String> words2, ArrayList<String> arr) {
        ArrayList<String> w2;
        for(String word: arr) {
            for(String w: words2) {
                w2 = wordify(w);
                for(String w3: w2)
                {
                    if(word.compareToIgnoreCase(w3) == 0) {
                        return word;
                    }

                }
            }
        }
        return null;
    }

    public String findKeyword2(ArrayList<String> words2, ArrayList<String> arr) {
        for(String word: arr) {
            for(String w: words2) {
                if(word.compareToIgnoreCase(w) == 0) {
                    return word;
                }

            }
        }
        return null;
    }


    public ArrayList<String> howQuestion(ArrayList<String> s){
        int index =0;
        String keyword = findKeyword2(s, words);
        for(int i = 0; i < questions.size(); i++){
            if(questions.get(i).contains("How")){
                if (questions.get(i).contains(keyword)){
                    index = i;
                    break;
                }
            }
        }
        return answers.get(index);
    }
    public ArrayList<String> doQuestion(ArrayList<String> s){
        int index =0;
        String keyword = findKeyword2(s, words);
        for(int i = 0; i < questions.size(); i++){
            if(questions.get(i).contains("Do")){
                if (questions.get(i).contains(keyword)){
                    index = i;
                    break;
                }
            }
        }
        return answers.get(index);
    }
    public ArrayList<String> whenQuestion(ArrayList<String> s){
        int index =0;
        String keyword = findKeyword2(s, words);
        for(int i = 0; i < questions.size(); i++){
            if(questions.get(i).contains("When")){
                if (questions.get(i).contains(keyword)){
                    index = i;
                    break;
                }
            }
        }
        return answers.get(index);
    }
    public ArrayList<String> whatQuestion(ArrayList<String> s){
        int index =0;
        String keyword = findKeyword2(s, words);
        for(int i = 0; i < questions.size(); i++){
            if(questions.get(i).contains("What")){
                if (questions.get(i).contains(keyword)){
                    index = i;
                    break;
                }
            }
        }
        return answers.get(index);
    }
private void buttonSet(ArrayList<String> answers) {
    ArrayList<Button> buttons = new ArrayList<>();
    GridLayout grid = (GridLayout) findViewById(R.id.grids);
    GridLayout.Spec row = GridLayout.spec(0);
    GridLayout.Spec colspan = GridLayout.spec(0);
    GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, colspan);
    try {
        grid.removeAllViews();

    }
    catch(Exception e) {

    }
    for(int i = 0; i < answers.size(); i++) {
        buttons.set(i, new Button(this));
        buttons.get(i).setText(answers.get(i));
        grid.addView(buttons.get(i),gridLayoutParam);

    }
}
}
