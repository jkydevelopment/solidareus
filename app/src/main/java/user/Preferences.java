package user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.google.gson.Gson;

/**
 * Created by Juan Carlos on 09/05/2017.
 */

public class Preferences extends PreferenceActivity {

    private Context mContext;
    private final String SHARED_PREFS_FILE = "Preferences";

    private final String SPLASH_SCREEN = "splash_screen";
    private final String HELP_GAME = "help_game";
    private final String USER = "user";
    private final String LOGROS = "logros";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Preferences(Context context){
        mContext = context;
    }

    private SharedPreferences getSettings(){
        return mContext.getSharedPreferences(SHARED_PREFS_FILE, 0);
    }

    public void setSplashScreen(boolean valor) { // la primera vez que inicie la app mostrar un splash2 screen
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putBoolean(SPLASH_SCREEN, valor);
        editor.commit();
    }
    public boolean isSplashScreen () {
        boolean valor = getSettings().getBoolean(SPLASH_SCREEN, true);
        return valor;
    }

    public void setHelpGame(boolean valor) { // la primera vez que inicie la app mostrar un splash2 screen
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putBoolean(HELP_GAME, valor);
        editor.commit();
    }
    public boolean isHelpGame () {
        boolean valor = getSettings().getBoolean(HELP_GAME, true);
        return valor;
    }

    public void setUser(User user) {
        SharedPreferences.Editor editor = getSettings().edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(USER, json);
        editor.commit();
    }
    public User getUser() {
        Gson gson = new Gson();
        String json = getSettings().getString(USER, "");
        User user = gson.fromJson(json, User.class);
        return user;
    }

    public void setLogros(Logros logros) {
        SharedPreferences.Editor editor = getSettings().edit();
        Gson gson = new Gson();
        String json = gson.toJson(logros);
        editor.putString(LOGROS, json);
        editor.commit();
    }
    public Logros getLogros() {
        Gson gson = new Gson();
        String json = getSettings().getString(LOGROS, "");
        Logros logros = gson.fromJson(json, Logros.class);
        return logros;
    }



    /** LOG OUT FROM THE APP **/
    public void logOut(){
        setUser(null);
        setLogros(null);
    }
}
