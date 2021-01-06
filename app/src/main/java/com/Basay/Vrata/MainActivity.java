package com.Basay.Vrata;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


interface StukOtvet {  // интерфейс для передачи ответа от потока запроса обратно в Активити
    void KtoTam(String OtvetArduino);
}

class Stuk extends AsyncTask<String,Void,String>{
    public StukOtvet delegate = null;
    @SuppressLint("StaticFieldLeak")
    private String myURL;
    Stuk(String url_){
        myURL=url_;
    }
    public static String doGet(String url)
            throws Exception {

        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0" );
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        connection.setRequestProperty("Content-Type", "application/json");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = bufferedReader.readLine()) != null) {
            response.append(inputLine);
        }
        bufferedReader.close();

//      print result
        Log.i(    "try_send_http","Response string: " + response.toString());
        return response.toString();
    }
    @Override
    protected String doInBackground(String... strings) {
       // String s = Con.getString(R.string.LocalURL);
        //s=s;
        try {
            //private static AsyncTask Potok;
            //String myURL = ;
            strings[0] = doGet(myURL +strings[0] );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strings[0];
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        delegate.KtoTam(s);
        //TextView hw= findViewById(R.id.twHW);
        //HW.setText(s);

    }
}


//===========================================================================================         ACTIVITY =============================================
public class MainActivity extends AppCompatActivity implements StukOtvet{
    Stuk Zapros;//=new Stuk();
    String TAG ="поток";
    private TextView HW;
    String On,Off;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HW= findViewById(R.id.twHW);
        On=getString(R.string.LightON);
        Off=getString(R.string.LightOFF);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void  Posyl (String Command){
        //String s=;
        if (Command.equals(On) || Command.equals(Off) ){
            Zapros=new Stuk(getString(R.string.LocalURL));
            Zapros.delegate=this;
            Zapros.execute(Command);
        }


    }

    public void onClick_btnON(View view) {
        Posyl(On);
    }

    public void onClick_btnOFF(View view) {
        Posyl(Off);
    }

    public void onClick_twHW(View view) {
    }

    @Override
    public void KtoTam(String OtvetArduino) {
        HW.setText(OtvetArduino);
        Zapros.cancel(true);
    }
}