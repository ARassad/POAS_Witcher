package prin366_2018.client.Settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

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


}
