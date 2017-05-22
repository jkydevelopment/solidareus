package jkydevelop.solidareus;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import user.Preferences;
import volley.CheckConnection;

import static jkydevelop.solidareus.Main.main;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FGame.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FGame#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FGame extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Preferences preferences;
    ImageView ivTarget, ivPuntos, ivPuntos1;
    TextView tvTarget, tvPuntos, tvPuntos1, tvRestarPuntos, tvTotalPuntos, tvRecord;
    LinearLayout lyPuntos;
    FrameLayout lyFondo, lyBeforeGame;
    Button bStart;

    private int TIME_GAME = 30;
    private int PUNTOS_A_RESTAR = 5;
    private int times_touched;
    private int times_random;
    private int total_times_touched;
    ProgressBar progressBarTimer;
    CountDownTimer mCountDownTimer;
    public static boolean isPlaying = false;
    int i=0;

    private View myView;

    private int timesToShowAds = 0;

    private OnFragmentInteractionListener mListener;

    public FGame() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FGame.
     */
    // TODO: Rename and change types and number of parameters
    public static FGame newInstance(String param1, String param2) {
        FGame fragment = new FGame();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        preferences = new Preferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_game, container, false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        bStart = (Button) myView.findViewById(R.id.bStart);
        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Si no tengo conexion muestro un mensaje
                if (CheckConnection.getConnectivityStatus(getActivity()) == 0)
                    alertNoConnection(width, height);
                else
                    startGame(width, height);

            }
        });

        lyFondo = (FrameLayout) myView.findViewById(R.id.lyFondo);
        lyFondo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restarPuntos();

            }
        });

        ivTarget = (ImageView) myView.findViewById(R.id.ivTarget);
        tvTarget = (TextView) myView.findViewById(R.id.tvTarget);
        ivTarget.setVisibility(View.GONE);
        tvTarget.setVisibility(View.GONE);

        ivTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                touchTarget(width, height);
            }
        });
        progressBarTimer = (ProgressBar) myView.findViewById(R.id.progressBarTimer);
        progressBarTimer.setProgress(i);
        progressBarTimer.setVisibility(View.GONE);

        ivPuntos = (ImageView) myView.findViewById(R.id.ivPuntos);
        ivPuntos1 = (ImageView) myView.findViewById(R.id.ivPuntos1);
        tvPuntos = (TextView) myView.findViewById(R.id.tvPuntos);
        tvPuntos1 = (TextView) myView.findViewById(R.id.tvPuntos1);
        tvRestarPuntos = (TextView) myView.findViewById(R.id.tvRestarPuntos);
        tvTotalPuntos = (TextView) myView.findViewById(R.id.tvTotalPuntos);
        tvRecord = (TextView) myView.findViewById(R.id.tvRecord);
        lyPuntos = (LinearLayout) myView.findViewById(R.id.lyPuntos);
        lyPuntos.setVisibility(View.GONE);
        tvRestarPuntos.setVisibility(View.GONE);

        lyBeforeGame = (FrameLayout) myView.findViewById(R.id.lyBeforeGame);
        lyBeforeGame.setVisibility(View.VISIBLE);
        tvTotalPuntos.setText(""+preferences.getLogros().getTotal_puntos());
        tvRecord.setText(""+preferences.getLogros().getBest());

        // Mostrar ayuda del juego
        final FrameLayout lyHelp = (FrameLayout) myView.findViewById(R.id.lyHelp);
        lyHelp.setVisibility(View.GONE);
        if (preferences.isHelpGame()){
            lyHelp.setVisibility(View.VISIBLE);
            ImageView ivHelp = (ImageView) myView.findViewById(R.id.ivHelp);
            ImageView ivCloseHelp = (ImageView) myView.findViewById(R.id.ivCloseHelp);
            ivCloseHelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    preferences.setHelpGame(false);
                    lyHelp.setVisibility(View.GONE);
                }
            });
        }


        return myView;
    }

    private void restarPuntos() {
        if (isPlaying){
            lyFondo.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWrong));
            //toast("Mal", 0);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    lyFondo.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorTransparent));
                    tvRestarPuntos.setVisibility(View.GONE);
                }
            }, 250);
            tvRestarPuntos.setText("-" + PUNTOS_A_RESTAR);
            tvRestarPuntos.setVisibility(View.VISIBLE);
            /*final Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 450);*/

            if (total_times_touched-PUNTOS_A_RESTAR>=0)
                total_times_touched = total_times_touched-PUNTOS_A_RESTAR;
            else
                total_times_touched = 0;
            tvPuntos.setText("x " + total_times_touched);
            tvPuntos1.setText("x " + total_times_touched);
        }
    }

    private void touchTarget(int width, int height) {
        total_times_touched ++;
        times_touched --;
        tvTarget.setText(String.valueOf(times_touched));
        if (times_touched<=0){
            times_random = new Random().nextInt(10-1)+1;
            times_touched = times_random;
            tvTarget.setText(String.valueOf(times_random));
            randomPosition(width, height);
        }
        tvPuntos.setText("x " + total_times_touched);
    }

    private void startGame(int width, int height) {

        // Primero comprobamos que se ha elegido un proyecto
        String categoria = preferences.getUser().getCategoria();
        if (categoria == null || categoria.equals("")){
            toast(getString(R.string.toast_select_project_first), 0);
            return;
        }
        main.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        main.appbar.setExpanded(false, true);

        isPlaying = true;
        total_times_touched = 0;
        // Hago visibles los elementos necesarios
        lyPuntos.setVisibility(View.VISIBLE);
        progressBarTimer.setVisibility(View.VISIBLE);
        bStart.setVisibility(View.INVISIBLE);
        ivTarget.setVisibility(View.VISIBLE);
        tvTarget.setVisibility(View.VISIBLE);
        tvRestarPuntos.setVisibility(View.GONE);
        lyBeforeGame.setVisibility(View.GONE);
        randomPosition(width, height);

        tvPuntos.setText("x 0");
        setCategoria();

        // Inicializo los contadores
        times_random = new Random().nextInt(10-1)+1;
        times_touched = times_random;
        tvTarget.setText(String.valueOf(times_random));

        // Inicializo el timer
        //progressBarTimer.setMax(TIME_GAME * 100);
        i=0;
        progressBarTimer.setProgress(0);
        progressBarTimer.setMax(TIME_GAME);
        mCountDownTimer=new CountDownTimer(TIME_GAME*1000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);
                i++;
                progressBarTimer.setProgress(i);
                //setProgressAnimate(progressBarTimer, i);

            }

            @Override
            public void onFinish() {
                //Do what you want
                finishGame();

            }
        };
        mCountDownTimer.start();
    }

    public void setCategoria() {
        // Cambio la imagen del target en funcion de la categoria del proyecto seleccionada
        switch (preferences.getUser().getCategoria()){
            case "Fauna": ivTarget.setImageResource(R.drawable.btn_medicina); ivPuntos.setImageResource(R.drawable.puntos_fauna); ivPuntos1.setImageResource(R.drawable.puntos_fauna); break;
            case "Nutricion": ivTarget.setImageResource(R.drawable.btn_nutricion); ivPuntos.setImageResource(R.drawable.puntos_nutricion); ivPuntos1.setImageResource(R.drawable.puntos_nutricion); break;
            case "Medicina": ivTarget.setImageResource(R.drawable.btn_medicina); ivPuntos.setImageResource(R.drawable.puntos_medicina); ivPuntos1.setImageResource(R.drawable.puntos_medicina); break;
            case "Agua": ivTarget.setImageResource(R.drawable.btn_agua); ivPuntos.setImageResource(R.drawable.puntos_agua); ivPuntos1.setImageResource(R.drawable.puntos_agua); break;
            case "MedioA": ivTarget.setImageResource(R.drawable.btn_medio_amb); ivPuntos.setImageResource(R.drawable.puntos_medio_ambiente); ivPuntos1.setImageResource(R.drawable.puntos_medio_ambiente); break;
            case "Flora": ivTarget.setImageResource(R.drawable.btn_flora); ivPuntos.setImageResource(R.drawable.puntos_flora); ivPuntos1.setImageResource(R.drawable.puntos_flora); break;
            case "InvSocial": ivTarget.setImageResource(R.drawable.btn_inv_social); ivPuntos.setImageResource(R.drawable.puntos_inv_social); ivPuntos1.setImageResource(R.drawable.puntos_inv_social); break;
            case "Educacion": ivTarget.setImageResource(R.drawable.btn_educacion); ivPuntos.setImageResource(R.drawable.puntos_educacion); ivPuntos1.setImageResource(R.drawable.puntos_educacion); break;
            default: ivTarget.setImageResource(R.drawable.btn_medicina); ivPuntos.setImageResource(R.drawable.btn_medicina); ivPuntos1.setImageResource(R.drawable.btn_medicina); break;
        }
        tvPuntos1.setText("x 0");
    }

    public void finishGame() {
        main.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        main.appbar.setExpanded(true, true);
        i++;
        progressBarTimer.setProgress(i);
        //setProgressAnimate(progressBarTimer, i);
        lyBeforeGame.setVisibility(View.VISIBLE);
        progressBarTimer.setVisibility(View.GONE);
        lyPuntos.setVisibility(View.GONE);
        ivTarget.setVisibility(View.GONE);
        tvTarget.setVisibility(View.GONE);
        tvRestarPuntos.setVisibility(View.GONE);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bStart.setVisibility(View.VISIBLE);
            }
        }, 1500);



        isPlaying = false;

        main.updateDatabase(total_times_touched);
        tvRecord.setText(""+preferences.getLogros().getBest());
        tvTotalPuntos.setText(""+preferences.getLogros().getTotal_puntos());
        tvPuntos1.setText("x " + total_times_touched);
        if (timesToShowAds == 0){
            timesToShowAds = 4;
            main.showInterstitial();
        }
        else
            timesToShowAds--;
    }

    public void cancelTimer(){
        if (mCountDownTimer!=null){
            mCountDownTimer.cancel();
            if (progressBarTimer!=null)
                progressBarTimer.setProgress(0);
        }
    }



    private void setProgressAnimate(ProgressBar pb, int progressTo)
    {
        ObjectAnimator animation = ObjectAnimator.ofInt(pb, "progress", pb.getProgress(), progressTo * 100);
        animation.setDuration(10000);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    private void randomPosition(int width, int height) {
        FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) ivTarget
                .getLayoutParams();
        params.leftMargin = new Random().nextInt(width - 200);
        params.topMargin = 250 + new Random().nextInt(height - 600);
        //params.topMargin = 250;
        ivTarget.setLayoutParams(params);
        tvTarget.setLayoutParams(params);
    }

    void alertNoConnection(final int width, final int height){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.alert_no_conection_message)
                .setTitle(getString(R.string.alert_no_connection_title))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.b_jugar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startGame(width, height);
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void toast(String texto, int length){
        Toast.makeText(getActivity(), "" + texto, length).show();
    }
}
