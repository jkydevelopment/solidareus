package jkydevelop.solidareus;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import projects.Project;
import user.Logros;
import user.Preferences;
import user.User;
import volley.CheckConnection;
import volley.ComunicationGetProjects;
import volley.ComunicationSetProject;
import volley.EndPoints;
import volley.MyVolley;

public class Main extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public static Main main;
    public static AppBarLayout appbar;
    private User user;
    private Preferences preferences;
    public static boolean animate = true; // Variable que controla la animacion de entrada de los cardview
    public static ViewPager mViewPager;
    private int tabActive = 1;
    public static ArrayList<Project> projectsArray = null;

    private boolean isNewLogros = false;
    private FGame fragmentGame;
    private FProjects fragmentProjects;
    private FUser fragmentUser;
    public int PROJECT_ID;
    public String PROJECT_CAT;

    private AdView adView;
    private InterstitialAd mInterstitialAd;
    private boolean adsLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        main = this;
        preferences = new Preferences(this);
        user = preferences.getUser();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        if (Main.projectsArray != null)
                            fragmentUser.setActualProject();
                        appbar.setExpanded(true, true);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        tabActive = 0;
                        if (adsLoaded) adView.setVisibility(View.INVISIBLE);
                        fragmentGame.cancelTimer();
                        if (FGame.isPlaying) fragmentGame.finishGame();
                        break;
                    case 1:
                        if (FGame.isPlaying){
                            appbar.setExpanded(false, true);
                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        }
                        else {
                            appbar.setExpanded(true, true);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        }
                        tabActive = 1;
                        if (adsLoaded) adView.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        appbar.setExpanded(true, true);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        loadProjects();
                        tabActive = 2;
                        if (adsLoaded) adView.setVisibility(View.INVISIBLE);
                        fragmentGame.cancelTimer();
                        if (FGame.isPlaying) fragmentGame.finishGame();
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(1);


        fragmentGame = new FGame();
        fragmentUser = new FUser();
        fragmentProjects = new FProjects();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animate = true;
                fragmentProjects.populate();
            }
        }, 500);

        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateDatabase(0);
            }
        }, 2000);
        // Al abrir la app actualizamos la app desde la bbdd aunque no hayamos jugado
        initializeAds();
    }


    @Override
    public void onBackPressed() {
        fragmentGame.cancelTimer();
        if (FGame.isPlaying) fragmentGame.finishGame();
        super.onBackPressed();
    }


    /**  ALERTS  **/


    public void openProject(int ID, int position){
        Intent intent = new Intent(this, ProjectRead.class);
        intent.putExtra("project", projectsArray.get(position));
        intent.putExtra("id", ID);
        startActivity(intent);

    }

    // Tengo que cargar solo las alertas no descartadas por el usuario
    public void loadProjects() {
        String email = preferences.getUser().getEmail();
        ComunicationGetProjects com = new ComunicationGetProjects(this);
        com.execute(EndPoints.URL_GET_PROJECT+"?email="+email);
    }


    // Se le llama despues de ejecutar el PHP
    public void updateProjects(JSONArray jsonArray){
        // Si hay datos en la bbdd, es decir, si hay notificaciones creadas...
        if (jsonArray!=null){
            projectsArray = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<Project>>(){}.getType());
            fragmentProjects.populateCardView(projectsArray, animate);
            if (Main.projectsArray!=null)
                fragmentUser.setActualProject();
        }
        else{
            fragmentProjects.populateCardView(null, animate);
        }
        animate = false;
        fragmentProjects.stopSwipeRefresh();
        fragmentGame.setCategoria();
    }

    // Recibo el id del alert que quiero leer
    public void selectProject(int ID, int position){
        user.setId_project(ID);
        user.setCategoria(projectsArray.get(position).getCategoria());
        preferences.setUser(user);
        //Log.e("CATEGORIA", "Cat: " + preferences.getUser().getCategoria());
        if (CheckConnection.getConnectivityStatus(this)!=0){
            ArrayList<User> aux = new ArrayList<>();
            aux.add(user);
            JsonArray myjsonarray = new Gson().toJsonTree(aux).getAsJsonArray();

            ComunicationSetProject com = new ComunicationSetProject(myjsonarray, this);
            com.execute(EndPoints.URL_SET_PROJECT);

        }
        else {
            toast(getString(R.string.no_conexion), 0);
        }
        if (CheckConnection.getConnectivityStatus(this)!=0)
            toast("Lo recaudado se destinará al proyecto: " + projectsArray.get(position).getTitulo(), 1);
    }

    public void setSelectedProject(int id, String cat){
        PROJECT_ID = id;
        PROJECT_CAT = cat;
    }


    public void updateDatabase(int total_puntos){
        // Tenga o no tenga conexion, almaceno los puntos en Preferences
        Logros logros = preferences.getLogros();
        logros.setTotal_puntos(preferences.getLogros().getTotal_puntos() + total_puntos);
        logros.setId_user(preferences.getUser().getEmail());
        logros.setId_project(preferences.getUser().getId_project());
        logros.setPuntos_actuales(total_puntos);
        setLogrosCategoria(logros, total_puntos);
        if (preferences.getLogros().getBest()<total_puntos) logros.setBest(total_puntos);
        preferences.setLogros(logros);

        // Actualizar la bbdd con los logros del usuario
        ArrayList<Logros> array = new ArrayList<>();
        array.add(logros);
        final JsonArray myjsonarray = new Gson().toJsonTree(array).getAsJsonArray();

        //Log.e("LOGLOGIN", "SENT: " + myjsonarray.toString());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_SET_LOGROS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("LOGROSRESPONSE", "response: " + response);
                        ArrayList<Logros> logrosArray;
                        try {
                            JSONArray jarray=new JSONArray(response);
                            logrosArray = new Gson().fromJson(jarray.toString(), new TypeToken<List<Logros>>(){}.getType());
                            Logros logros = logrosArray.get(0);
                            checkIfNewLogros(logros);
                            preferences.setLogros(logros);
                            if (isNewLogros){
                                fragmentUser.setMedallas();
                                isNewLogros = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("LOGLOGIN", "Error Response: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("logros", myjsonarray.toString());
                return params;
            }
        };
        MyVolley.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void checkIfNewLogros(Logros logros) {
        // logros lo recibo desde la bbdd tras llamar al php.
        // compruebo que los toString son diferentes, y si lo son miro que ha cambiado
        if (logros.getFauna()==1 && preferences.getLogros().getFauna()==0) alertNewLogro("Fauna");
        if (logros.getNutricion()==1 && preferences.getLogros().getNutricion()==0) alertNewLogro("Nutricion");
        if (logros.getMedicina()==1 && preferences.getLogros().getMedicina()==0) alertNewLogro("Medicina");
        if (logros.getAgua()==1 && preferences.getLogros().getAgua()==0) alertNewLogro("Agua");
        if (logros.getMedioambiente()==1 && preferences.getLogros().getMedioambiente()==0) alertNewLogro("MedioA");
        if (logros.getFlora()==1 && preferences.getLogros().getFlora()==0) alertNewLogro("Flora");
        if (logros.getInvestigacion()==1 && preferences.getLogros().getInvestigacion()==0) alertNewLogro("InvSocial");
        if (logros.getEducacion()==1 && preferences.getLogros().getEducacion()==0) alertNewLogro("Educacion");
        if (logros.getSemanaJuego()==1 && preferences.getLogros().getSemanaJuego()==0) alertNewLogro("Semana");
        if (logros.getMesJuego()==1 && preferences.getLogros().getMesJuego()==0) alertNewLogro("Mes");
        if (logros.getCincomil()==1 && preferences.getLogros().getCincomil()==0) alertNewLogro("Cincomil");
        if (logros.getPubli()==1 && preferences.getLogros().getPubli()==0) alertNewLogro("Publi");



    }

    private Logros setLogrosCategoria(Logros logros, int puntos) {
        switch (preferences.getUser().getCategoria()){
            case "Fauna": logros.setP_fauna(logros.getP_fauna() + puntos); break;
            case "Nutricion": logros.setP_nutricion(logros.getP_nutricion() + puntos); break;
            case "Medicina":  logros.setP_medicina(logros.getP_medicina() + puntos); break;
            case "Agua":  logros.setP_agua(logros.getP_agua() + puntos); break;
            case "MedioA":  logros.setP_medioambiente(logros.getP_medioambiente() + puntos); break;
            case "Flora":  logros.setP_flora(logros.getP_flora() + puntos); break;
            case "InvSocial":  logros.setP_investigacion(logros.getP_investigacion() + puntos); break;
            case "Educacion":  logros.setP_educacion(logros.getP_educacion() + puntos); break;
        }
        return logros;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void alertNewLogro(String cat){
        //toast("¡Enhorabuena! Tienes un nuevo logro", 1);
        isNewLogros = true;
        final Dialog dialog = new Dialog(this);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle(getString(R.string.alert_new_logro_title));
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.alert_new_logro);

        ImageView ivLogro = (ImageView) dialog.findViewById(R.id.ivLogro);
        ivLogro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);
                dialog.dismiss();
            }
        });
        switch (cat){
            case "Fauna": ivLogro.setImageResource(R.drawable.ic_insig_fauna); break;
            case "Nutricion": ivLogro.setImageResource(R.drawable.ic_insig_nutricion); break;
            case "Medicina": ivLogro.setImageResource(R.drawable.ic_insig_medicina); break;
            case "Agua": ivLogro.setImageResource(R.drawable.ic_insig_agua); break;
            case "MedioA": ivLogro.setImageResource(R.drawable.ic_insig_medio_amb); break;
            case "Flora": ivLogro.setImageResource(R.drawable.ic_insig_flora); break;
            case "InvSocial": ivLogro.setImageResource(R.drawable.ic_insig_inv_social); break;
            case "Educacion": ivLogro.setImageResource(R.drawable.ic_insig_educacion); break;
            case "Semana": ivLogro.setImageResource(R.drawable.ic_insig_semana); break;
            case "Mes": ivLogro.setImageResource(R.drawable.ic_insig_mes); break;
            case "Cincomil": ivLogro.setImageResource(R.drawable.ic_insig_cincomil); break;
            case "Publi": ivLogro.setImageResource(R.drawable.ic_insig_publi); break;
        }
        dialog.show();
    }

    public void logOut() {
        preferences.logOut();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    // Actualizo la bbdd cuando cambio el avatar
    public void updateUserDataBase() {
        ArrayList<User> array = new ArrayList<>();
        array.add(preferences.getUser());
        final JsonArray myjsonarray = new Gson().toJsonTree(array).getAsJsonArray();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_SET_USER_PARAMS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e("LOGLOGIN", "response: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("LOGLOGIN", "Error Response: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user", myjsonarray.toString());
                return params;
            }
        };
        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            /*View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;*/
            return null;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            //Log.d("GETITEM", "position: " + position);
            switch (position){
                case 0:
                    return fragmentUser;
                case 1:
                    return fragmentGame;
                case 2:
                    return fragmentProjects;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return ("Perfil");
                case 1:
                    return ("Juego");
                case 2:
                    return ("Proyectos");
            }
            return null;
        }
    }


    /** ADMMOB - BANNER AND INTERSTITIAL **/

    private void initializeAds() {
        // Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).
        showBanner();
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
    }

    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_id_main));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d("ADLOADED", "LOADED");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.d("ADLOADED", "ERROR: " + errorCode);
            }

            @Override
            public void onAdClosed() {
                // Proceed to the next level.
            }
        });
        return interstitialAd;
    }

    public void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            //Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            System.out.println("Ad not loaded");
        }
    }

    private void loadInterstitial() {
        // Disable the next level button and load the ad.
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("66DBFD5A9B7E2004DCBDEB7A323EA793") // Galaxy S4
                .addTestDevice("6F2F08CC20A70C897081A01C81B17613") // Galaxy S5 mini
                .addTestDevice("0BB9867268009257F9A4C74345D550F4") // Ulefone Future
                .addTestDevice("B7BECA03AC4EC0385641442DF0119D03") // Tablet ACER
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void showBanner() {
        adView = (AdView) findViewById(R.id.adViewNav);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("66DBFD5A9B7E2004DCBDEB7A323EA793") // Galaxy S4
                .addTestDevice("6F2F08CC20A70C897081A01C81B17613") // Galaxy S5 mini
                .addTestDevice("0BB9867268009257F9A4C74345D550F4") // Ulefone Future
                .addTestDevice("B7BECA03AC4EC0385641442DF0119D03") // Tablet ACER
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);
        adsLoaded = true;
    }

    private void toast(String texto, int length){
        Toast.makeText(this, "" + texto, length).show();
    }
}
