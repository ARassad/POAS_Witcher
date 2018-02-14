package ServerExchange.ServerRequests;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Dryush on 13.02.2018.
 */

abstract class ServerRequest {

    private String serverAdress;
    private String API = "api?method=";
    public ServerRequest(String serverAdress)
    {
        this.serverAdress = serverAdress;
    }



    protected <T> void  doRequest(String method, String paramsNames[], String paramsValue[], T answer) throws IOException {

        String request = serverAdress + API + method ;
        int count = paramsNames.length;
        for ( int i = 0; i < count; i++){
            request += '&' + paramsNames[i] + '=' + paramsValue[i];
        }

        try {
            URL requestURL = new URL(request);


            HttpURLConnection http = (HttpURLConnection) requestURL.openConnection();
            http.setRequestMethod("GET");

            InputStream in = new BufferedInputStream(http.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            Gson gs = new Gson();
            answer = (T) gs.fromJson(reader, answer.getClass());


        } catch (Exception except){
            throw except;
        }

    }

}
