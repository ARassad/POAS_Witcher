package prin366_2018.client.Settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Dryush on 13.02.2018.
 */

public class AppSettings{
    private static final AppSettings ourInstance = new AppSettings();

    private static final String FILENAME = "settings";

    private static SharedPreferences settings;
    private static SharedPreferences.Editor settingsEditor;

    public static void setSettings(Activity activity){
        settings =  activity.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        settingsEditor = settings.edit();
    }
    public static AppSettings get() {
        return ourInstance;
    }

    protected void setSetting(String name, String value){
        settingsEditor.putString(name, value);
    }
    protected void setSettings(String[] names, String[] values){
        int len = names.length;
        for (int i =0; i < len; i++) {
            settingsEditor.putString(names[i], values[i]);
        }
    }



    //protected void defaultValues(String name, String value){
    //    if ( !settings.contains())
    //}


    protected AppSettings() {
        //Здесь прописать деффолтные настройки
    }

    public String getIpServer (){
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard,"file.txt");
        //StringBuilder text = new StringBuilder();
        String ipServer = new String();

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            ipServer = br.readLine();
            //String line;
            /*while ((line = br.readLine()) != null)
            {
                text.append(line);
                text.append('\n');
            }*/
            br.close();
        }
        catch (IOException e)
        {
            //Ошибка!!!
        }
        return ipServer;
    }
}
