package jkydevelop.solidareus;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import splashscreen.SplashScreen;
import user.Logros;
import user.Preferences;
import user.User;
import volley.CheckConnection;
import volley.EndPoints;
import volley.MyVolley;

public class Login extends AppCompatActivity {

    private Preferences preferences;
    private ArrayList<User> userArray = new ArrayList<>();

    private ScrollView lyLogin, lyRegister;
    private EditText etName, etSurname, etEmail, etPass, etPassConfirm;
    private AutoCompleteTextView etCity;
    private EditText etUser, etUserPass;
    private TextView tvDate;
    private Button bLogin_Register, bRegister_Login;
    private RadioButton rbM, rbF;

    private boolean isFechaSelected = false;
    int yearSelected=2000, monthSelected=01, daySelected=01;
    LocalDate fechaNacimiento = new LocalDate(yearSelected, monthSelected, daySelected);
    String fechaString;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setTitle(getString(R.string.title_login));
        preferences = new Preferences(this);
        MultiDex.install(this);

        lyRegister = (ScrollView) findViewById(R.id.lyRegister);
        lyLogin = (ScrollView) findViewById(R.id.lyLogin);
        etName = (EditText) findViewById(R.id.etName);
        etSurname = (EditText) findViewById(R.id.etSurname);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etCity = (AutoCompleteTextView) findViewById(R.id.etCity);
        etPass = (EditText) findViewById(R.id.etPass);
        etUserPass = (EditText) findViewById(R.id.etUserPass);
        etPassConfirm = (EditText) findViewById(R.id.etPassConfirm);
        tvDate = (TextView) findViewById(R.id.tvDate);
        ImageView ivTarta = (ImageView) findViewById(R.id.ivTarta);
        bLogin_Register = (Button) findViewById(R.id.bLogin_Register);
        bRegister_Login = (Button) findViewById(R.id.bRegister_Login);
        etUser = (EditText) findViewById(R.id.etUser);
        etPass = (EditText) findViewById(R.id.etPass);
        rbF = (RadioButton) findViewById(R.id.rbF);
        rbM = (RadioButton) findViewById(R.id.rbM);
        TextView tvPassLost = (TextView) findViewById(R.id.tvPassLost);
        tvPassLost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertLostPassword();
            }
        });
        // Get the string array
        //String[] countries = getResources().getStringArray(R.array.cities_array);
        String[] countries = getResources().getStringArray(R.array.provincias);
// Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        etCity.setAdapter(adapter);


        bLogin_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lyLogin.setVisibility(View.GONE);
                lyRegister.setVisibility(View.VISIBLE);
            }
        });

        bRegister_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lyLogin.setVisibility(View.VISIBLE);
                lyRegister.setVisibility(View.GONE);
            }
        });

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFecha();
            }
        });
        ivTarta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFecha();
            }
        });

        Button bLogin = (Button) findViewById(R.id.bLogin);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = etUser.getText().toString().toLowerCase().replaceAll("\\s+$", "");
                String pass = etUserPass.getText().toString().replaceAll("\\s+$", "");
                login(user, pass);
            }
        });

        Button bRegister = (Button) findViewById(R.id.bRegister);
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().replaceAll("\\s+$", "");
                String surname = etSurname.getText().toString().replaceAll("\\s+$", "");
                String email = etEmail.getText().toString().toLowerCase().replaceAll("\\s+$", "");
                String pass = etPass.getText().toString().replaceAll("\\s+$", "");
                String passConfirm = etPassConfirm.getText().toString().replaceAll("\\s+$", "");
                String city = etCity.getText().toString().replaceAll("\\s+$", "");
                String genre = "O";
                if (rbF.isChecked())
                    genre = "F";
                else if (rbM.isChecked())
                    genre = "M";

                if (name.equals("") || surname.equals("") || email.equals("") || pass.equals("") || passConfirm.equals("") || city.equals("")){
                    toast(getString(R.string.toast_complete_form), 1);
                    return;
                }
                if (!isFechaSelected){
                    toast(getString(R.string.toast_complete_form_date), 1);
                    return;
                }

                if (!pass.equals(passConfirm))
                    toast(getString(R.string.toast_error_contraseña_no_coincide), 0);
                else{
                    if (pass.length()<6)
                        toast(getString(R.string.toast_error_contraseña_seis_caracteres), 0);
                    else
                        register(name, surname, email, pass, genre, city);
                }
            }
        });


        yearSelected = fechaNacimiento.getYear();
        monthSelected = fechaNacimiento.getMonthOfYear()-1;
        daySelected = fechaNacimiento.getDayOfMonth();

        if (preferences.isSplashScreen()){
            Intent intent = new Intent(this, SplashScreen.class);
            startActivity(intent);
            finish();
        }
        else {
            checkLogin();
        }
    }


    public void setFecha(){
        //creamos y mostramos cuadro de diálogo
        //de fecha
        new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        //la fecha seleccionada la
                        //mostramos en el TextView, hay que tener
                        ///en cuenta que los meses comienzan por 0
                        String a="", b="";
                        if (day<9)
                            a = "0";
                        if (month+1<9)
                            b = "0";
                        tvDate.setText(""+a+day+"-"+b+(month+1)+"-"+year);
                        yearSelected =  year;
                        monthSelected = month+1;
                        daySelected = day;
                        isFechaSelected = true;
                    }
                },
                yearSelected,
                monthSelected-1,
                daySelected
        ).show();
    }



    private void register(String name, String surname, String email, String pass, String genre, String city) {
        if (monthSelected<1 || monthSelected>12) monthSelected=1; //Sino peta porque tengo -1
        fechaNacimiento = new LocalDate(yearSelected, monthSelected, daySelected);
        Timestamp timestamp = new Timestamp(fechaNacimiento.toDateTimeAtStartOfDay().getMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");


        User myUser = new User();
        myUser.setName(name);
        myUser.setSurname(surname);
        myUser.setEmail(email);
        myUser.setPassword(pass);
        myUser.setGenre(genre);
        myUser.setCity(city);
        myUser.setDate(fechaNacimiento.toDate());
        ArrayList<User> array = new ArrayList<>();
        array.add(myUser);
        final JsonArray myjsonarray = new Gson().toJsonTree(array).getAsJsonArray();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.alert_registering));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("REGISTERED", "response: " + response);
                        progressDialog.dismiss();
                        if (response.equals("1")){
                            toast(getString(R.string.toast_error_user_already_registered), 1);
                            return;
                        }
                        try{
                            JSONArray jarray=new JSONArray(response);
                            userArray.clear();
                            userArray = new Gson().fromJson(jarray.toString(), new TypeToken<List<User>>() {
                            }.getType());
                            User user = userArray.get(0);
                            preferences.setUser(user);
                            openLogin();
                        }
                        catch(JSONException ex){
                            ex.printStackTrace();
                        }

                        Log.e("LOGREGISTER", "Response: " + response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.e("LOGREGISTER", "Response: " + error.getMessage());
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



    private void login(String user, String pass) {
        User myUser = new User();
        myUser.setEmail(user);
        myUser.setPassword(pass);
        ArrayList<User> array = new ArrayList<>();
        array.add(myUser);
        JsonArray myjsonarray = new Gson().toJsonTree(array).getAsJsonArray();

        //Log.e("LOGLOGIN", "SENT: " + myjsonarray.toString());

        getUserFromServer(myjsonarray);
        getLogrosFromServer(myUser);
    }

    private void getUserFromServer(final JsonArray myjsonarray){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.alert_logging));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (response){
                            case "1": toast(getString(R.string.toast_error_user_inexistent), 1); break;
                            case "2": toast(getString(R.string.toast_error_incorrect_pass), 1); break;
                            default: break;
                        }

                        //Log.e("LOGLOGIN", "Response: " + response);
                        progressDialog.dismiss();

                        try {
                            JSONArray jarray=new JSONArray(response);
                            userArray.clear();
                            userArray = new Gson().fromJson(jarray.toString(), new TypeToken<List<User>>(){}.getType());
                            toast(getString(R.string.toast_welcome) + " " + userArray.get(0).getName(), 0);
                            User user = userArray.get(0);
                            preferences.setUser(user);
                            openApp();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.e("LOGLOGIN", "Error Response: " + error.getMessage());
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

    private void getLogrosFromServer(final User user){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_GET_LOGROS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.e("LOGLOGROS", "Response: " + response);

                        try {
                            JSONArray jarray=new JSONArray(response);

                            ArrayList<Logros> logrosArray = new Gson().fromJson(jarray.toString(), new TypeToken<List<Logros>>(){}.getType());
                            Logros logros = logrosArray.get(0);
                            preferences.setLogros(logros);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.e("LOGLOGIN", "Error Response: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", user.getEmail());
                return params;
            }
        };
        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void checkLogin() {
        if (preferences.getUser()!=null){
            if (!preferences.getUser().getEmail().equals("")){
                openApp();
            }
        }

    }

    private void openLogin() {
        lyRegister.setVisibility(View.GONE);
        lyLogin.setVisibility(View.VISIBLE);
        etUser.setText(userArray.get(0).getEmail());
        etUserPass.setText(userArray.get(0).getPassword());
    }


    private void openApp() {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
        this.finish();
    }

    private void alertLostPassword () {
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.alert_insert_email, null);
        final EditText subEditText = (EditText)subView.findViewById(R.id.etEmail);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.alert_lost_pass_title));
        builder.setMessage(getString(R.string.alert_lost_pass_message));
        builder.setCancelable(false);
        //builder.setMessage(R.string.alert_vaciar_ayuda);
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();
        builder.setPositiveButton(R.string.b_send, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = subEditText.getText().toString().replaceAll("\\s+$", "");
                recoverPassword(email);
            }


        });

        builder.setNegativeButton(R.string.b_cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(NavigationActivity.this, "Cancelado", Toast.LENGTH_LONG).show();
            }
        });

        builder.show();
    }

    private void recoverPassword(final String email) {
        if (CheckConnection.getConnectivityStatus(this)!=0){
            toast(getString(R.string.no_conexion), 0);
            return;
        }
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.alert_registering));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_RECOVER_PASS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("REGISTERED", "response: " + response);
                        progressDialog.dismiss();
                        if (response.contains("1")){
                            toast(getString(R.string.toast_login_error_user_not_exist), 1);
                        }
                        if (response.contains("0")){
                            toast(getString(R.string.toast_email_password_sent), 1);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.e("LOGREGISTER", "Response: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };
        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void toast(String texto, int length){
        Toast.makeText(this, "" + texto, length).show();
    }

}
