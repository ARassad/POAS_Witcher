package prin366_2018.client;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Dryush on 13.02.2018.
 */

class WitcherSettings {
    private static final WitcherSettings ourInstance = new WitcherSettings();

    private static final String FILENAME = "settings";



    private static final String DEFFAULT_SERVER_ADRESS = "localhost/";
    private static final String SERVER_ADDRESS_SETTING = "server_adress";


    private static SharedPreferences settings;
    public static void setSettings(Activity activity){
        settings =  activity.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
    }
    static WitcherSettings getInstance() {
        return ourInstance;
    }



    private WitcherSettings() {
        if ( settings.contains("server_adress")){

        } else {
            settings.edit().putString(SERVER_ADDRESS_SETTING, DEFFAULT_SERVER_ADRESS);
        }

    }

    public String getServerAddress(){
        return settings.getString(SERVER_ADDRESS_SETTING, DEFFAULT_SERVER_ADRESS);
    }

}
