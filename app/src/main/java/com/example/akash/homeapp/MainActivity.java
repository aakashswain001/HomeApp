package com.example.akash.homeapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.akash.homeapp.R.id.parent;

/**
 * Created by root on 24/3/17.
 */

public class MainActivity extends Activity {
    private EditText time;
    private TextView tv;
    private Button on,off;
    private int temp1=5,temp2=5,temp3=5,temp4=0;
    private ImageButton sp;
    private int FLAG = 1;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        time = (EditText)findViewById(R.id.ed);
        time.setText("0");
        tv = (TextView)findViewById(R.id.tv);
        on = (Button)findViewById(R.id.on);
        off = (Button)findViewById(R.id.off);
        sp = (ImageButton)findViewById(R.id.ac);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.device, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FLAG = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              promptSpeechInput();
            }
        });
        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertToDatabase(time.getText().toString(),"1",Integer.toString(FLAG));
            }
        });
        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertToDatabase(time.getText().toString(),"0",Integer.toString(FLAG));
            }
        });
    }


    private void insertToDatabase(final String time, final String status,final String device){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramTime = params[0];
                String paramStatus = params[1];
                String paramDevice = params[2];
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("time",time));
                nameValuePairs.add(new BasicNameValuePair("status", status));
                nameValuePairs.add(new BasicNameValuePair("device",device));
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://studentsforhire.in/test1_inp");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "success";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(time,status,device);
    }
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    tv.setText(result.get(0));
                    String[] splited = result.get(0).split("\\s+");
                    TextView ttt = (TextView)findViewById(R.id.tttt);
                    for (String str:splited
                         ) {
                        ttt.setText(str);
                       if(str.equals("on")){temp1=1;}else if (str.equals("off")||str.equals("of")){temp1=0;}else if (str.equals("one")||str.equals("1")){
                            temp2 = 1;
                        }else if (str.equals("two")||str.equals("2")){
                            temp2 = 2;
                        }else if (str.equals("after")){
                           temp3=1;
                       }else if (temp3==1){
                           try {
                               temp4 = Integer.parseInt(str);
                               temp3=5;
                           }catch (Exception e){}
                       }
                    }
             //       TextView ttt = (TextView)findViewById(R.id.tttt);
                    if((temp1!=5)&&(temp2!=5)){insertToDatabase(Integer.toString(temp4),Integer.toString(temp1),Integer.toString(temp2));
                    ttt.setText("Successfully received voice input");
                        temp1=5;
                        temp2=5;
                        temp3=5;
                        temp4=0;
                    }else {
                    ttt.setText("Command not recognized .Try Again...");
               }
                }
                break;
            }

        }
    }


}
