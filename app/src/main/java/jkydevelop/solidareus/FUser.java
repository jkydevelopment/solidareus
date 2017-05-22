package jkydevelop.solidareus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import projects.Project;
import user.Preferences;
import user.User;

import static jkydevelop.solidareus.Main.main;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FUser.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FUser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FUser extends Fragment {

    View myView = null;
    Preferences preferences;
    int posMision = 0;

    // Proyecto actual
    ImageView ivFoto, ivAvatar;
    TextView tvTitulo, tvDescripcion, tvFecha;

    private OnFragmentInteractionListener mListener;

    public FUser() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FUser.
     */
    // TODO: Rename and change types and number of parameters
    public static FUser newInstance(String param1, String param2) {
        FUser fragment = new FUser();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new Preferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_user, container, false);

        TextView tvName = (TextView) myView.findViewById(R.id.tvName);
        TextView tvSurname = (TextView) myView.findViewById(R.id.tvSurname);
        ImageView bLogout = (ImageView) myView.findViewById(R.id.bLogout);
        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertLogOut();
            }
        });

        tvName.setText(preferences.getUser().getName());
        tvSurname.setText(preferences.getUser().getSurname());

        ivFoto = (ImageView) myView.findViewById(R.id.ivFoto);
        ivAvatar = (ImageView) myView.findViewById(R.id.ivAvatar);
        tvTitulo = (TextView) myView.findViewById(R.id.tvTitulo);
        tvDescripcion = (TextView) myView.findViewById(R.id.tvDescripcion);
        tvFecha = (TextView) myView.findViewById(R.id.tvFecha);
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertSetAvatar();
            }
        });

        ImageView ivFacebook = (ImageView) myView.findViewById(R.id.ivFacebook);
        ivFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.facebook.com/solidareus"));
                startActivity(intent);
            }
        });
        ImageView ivTwitter = (ImageView) myView.findViewById(R.id.ivTwitter);
        ivTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Intent.ACTION_VIEW,Uri.parse("https://twitter.com/solidareus"));
                startActivity(intent);
            }
        });
        ImageView ivWeb = (ImageView) myView.findViewById(R.id.ivWeb);
        ivWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Intent.ACTION_VIEW,Uri.parse("https://solidareus.com"));
                startActivity(intent);
            }
        });
        ImageView ivYoutube = (ImageView) myView.findViewById(R.id.ivYoutube);


        ivFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProject();
            }
        });
        tvTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProject();
            }
        });
        tvDescripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProject();
            }
        });

        ImageView ivLogroInvSoc = (ImageView) myView.findViewById(R.id.ivLogroInvSoc);
        ImageView ivLogroMedicina = (ImageView) myView.findViewById(R.id.ivLogroMedicina);
        ImageView ivLogroNutri = (ImageView) myView.findViewById(R.id.ivLogroNutri);
        ImageView ivLogroEduc = (ImageView) myView.findViewById(R.id.ivLogroEduc);
        ImageView ivLogroFauna = (ImageView) myView.findViewById(R.id.ivLogroFauna);
        ImageView ivLogroFlora = (ImageView) myView.findViewById(R.id.ivLogroFlora);
        ImageView ivLogroMedioA = (ImageView) myView.findViewById(R.id.ivLogroMedioA);
        ImageView ivLogroAgua = (ImageView) myView.findViewById(R.id.ivLogroAgua);
        ImageView ivLogroClicks = (ImageView) myView.findViewById(R.id.ivLogroClicks);
        ImageView ivLogroSemana = (ImageView) myView.findViewById(R.id.ivLogroSemana);
        ImageView ivLogroMes = (ImageView) myView.findViewById(R.id.ivLogroMes);
        ImageView ivLogroPubli = (ImageView) myView.findViewById(R.id.ivLogroPubli);
        ivLogroInvSoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertOpenLogro("InvSocial");
            }
        });
        ivLogroMedicina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertOpenLogro("Medicina");
            }
        });
        ivLogroNutri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertOpenLogro("Nutricion");
            }
        });
        ivLogroEduc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertOpenLogro("Educacion");
            }
        });
        ivLogroFauna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertOpenLogro("Fauna");
            }
        });
        ivLogroFlora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertOpenLogro("Flora");
            }
        });
        ivLogroMedioA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertOpenLogro("MedioA");
            }
        });
        ivLogroAgua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertOpenLogro("Agua");
            }
        });
        ivLogroClicks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertOpenLogro("Cincomil");
            }
        });
        ivLogroSemana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertOpenLogro("Semana");
            }
        });
        ivLogroMes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertOpenLogro("Mes");
            }
        });
        ivLogroPubli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertOpenLogro("Publi");
            }
        });

        setMedallas();


        final TextView tvMision = (TextView) myView.findViewById(R.id.tvMision);
        ImageView ivPrev = (ImageView) myView.findViewById(R.id.ivPrev);
        ImageView ivNext = (ImageView) myView.findViewById(R.id.ivNext);
        ivPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (posMision == 0) posMision = 12;
                posMision--;
                updateMision(tvMision);
            }
        });
        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (posMision == 11) posMision = -1;
                posMision++;
                updateMision(tvMision);
            }
        });

        setAvatar();

        return myView;
    }

    public void setMedallas() {
        // Inicializo las medallas y establezco el alpha de las mismas
        int ALPHA = 50;
        ImageView ivLogroSemana = (ImageView) myView.findViewById(R.id.ivLogroSemana);
        if (preferences.getLogros().getSemanaJuego()==1) ivLogroSemana.setImageAlpha(255); else ivLogroSemana.setImageAlpha(ALPHA);
        ImageView ivLogroInvSoc = (ImageView) myView.findViewById(R.id.ivLogroInvSoc);
        if (preferences.getLogros().getInvestigacion()==1) ivLogroInvSoc.setImageAlpha(255); else ivLogroInvSoc.setImageAlpha(ALPHA);
        ImageView ivLogroMedicina = (ImageView) myView.findViewById(R.id.ivLogroMedicina);
        if (preferences.getLogros().getMedicina()==1) ivLogroMedicina.setImageAlpha(255); else ivLogroMedicina.setImageAlpha(ALPHA);
        ImageView ivLogroMes = (ImageView) myView.findViewById(R.id.ivLogroMes);
        if (preferences.getLogros().getMesJuego()==1) ivLogroMes.setImageAlpha(255); else ivLogroMes.setImageAlpha(ALPHA);
        ImageView ivLogroNutri = (ImageView) myView.findViewById(R.id.ivLogroNutri);
        if (preferences.getLogros().getNutricion()==1) ivLogroNutri.setImageAlpha(255); else ivLogroNutri.setImageAlpha(ALPHA);
        ImageView ivLogroEduc = (ImageView) myView.findViewById(R.id.ivLogroEduc);
        if (preferences.getLogros().getEducacion()==1) ivLogroEduc.setImageAlpha(255); else ivLogroEduc.setImageAlpha(ALPHA);
        ImageView ivLogroClicks = (ImageView) myView.findViewById(R.id.ivLogroClicks);
        if (preferences.getLogros().getCincomil()==1) ivLogroClicks.setImageAlpha(255); else ivLogroClicks.setImageAlpha(ALPHA);
        ImageView ivLogroFauna = (ImageView) myView.findViewById(R.id.ivLogroFauna);
        if (preferences.getLogros().getFauna()==1) ivLogroFauna.setImageAlpha(255); else ivLogroFauna.setImageAlpha(ALPHA);
        ImageView ivLogroFlora = (ImageView) myView.findViewById(R.id.ivLogroFlora);
        if (preferences.getLogros().getFlora()==1) ivLogroFlora.setImageAlpha(255); else ivLogroFlora.setImageAlpha(ALPHA);
        ImageView ivLogroPubli = (ImageView) myView.findViewById(R.id.ivLogroPubli);
        if (preferences.getLogros().getPubli()==1) ivLogroPubli.setImageAlpha(255); else ivLogroPubli.setImageAlpha(ALPHA);
        ImageView ivLogroMedioA = (ImageView) myView.findViewById(R.id.ivLogroMedioA);
        if (preferences.getLogros().getMedioambiente()==1) ivLogroMedioA.setImageAlpha(255); else ivLogroMedioA.setImageAlpha(ALPHA);
        ImageView ivLogroAgua = (ImageView) myView.findViewById(R.id.ivLogroAgua);
        if (preferences.getLogros().getAgua()==1) ivLogroAgua.setImageAlpha(255); else ivLogroAgua.setImageAlpha(ALPHA);
    }

    private void updateMision(TextView tvMision) {
        String [] misiones = getResources().getStringArray(R.array.misiones_array);
        tvMision.setText(misiones[posMision]);
    }

    private void openProject(){
        for (int i=0; i<Main.projectsArray.size(); i++){
            if (Main.projectsArray.get(i).getId() == preferences.getUser().getId_project()){
                main.openProject(preferences.getUser().getId_project(), i);
                return;
            }
        }
    }

    public void setActualProject(){
        Project project = null;
        for (int i=0; i<Main.projectsArray.size(); i++){
            if (Main.projectsArray.get(i).getId() == preferences.getUser().getId_project())
                project = Main.projectsArray.get(i);
        }
        if (project!=null){
            //Log.d("MYARRAY", "Projects2: " + Main.projectsArray.toString());
            tvTitulo.setText(project.getTitulo());
            tvDescripcion.setText(project.getDescripcion());

            switch (project.getCategoria()){
                case "Fauna": ivFoto.setImageResource(R.drawable.ic_cat_fauna); ivFoto.setBackgroundColor(ContextCompat.getColor(main, R.color.colorFauna)); break;
                case "Nutricion": ivFoto.setImageResource(R.drawable.ic_cat_nutricion); ivFoto.setBackgroundColor(ContextCompat.getColor(main, R.color.colorNutricion)); break;
                case "Medicina": ivFoto.setImageResource(R.drawable.ic_cat_medicina); ivFoto.setBackgroundColor(ContextCompat.getColor(main, R.color.colorMedicina)); break;
                case "Agua": ivFoto.setImageResource(R.drawable.ic_cat_agua); ivFoto.setBackgroundColor(ContextCompat.getColor(main, R.color.colorAgua)); break;
                case "MedioA": ivFoto.setImageResource(R.drawable.ic_cat_medioa); ivFoto.setBackgroundColor(ContextCompat.getColor(main, R.color.colorMedioAmbiente)); break;
                case "Flora": ivFoto.setImageResource(R.drawable.ic_cat_flora); ivFoto.setBackgroundColor(ContextCompat.getColor(main, R.color.colorFlora)); break;
                case "InvSocial": ivFoto.setImageResource(R.drawable.ic_cat_invsocial); ivFoto.setBackgroundColor(ContextCompat.getColor(main, R.color.colorInvestigacion)); break;
                case "Educacion": ivFoto.setImageResource(R.drawable.ic_cat_educacion); ivFoto.setBackgroundColor(ContextCompat.getColor(main, R.color.colorEducacion)); break;
                default: ivFoto.setImageResource(R.drawable.ic_cat_agua); ivFoto.setBackgroundColor(ContextCompat.getColor(main, R.color.colorAccent)); break;
            }
        }

    }


    private void alertSetAvatar(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.alert_avatar);

        //TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        //text.setText(msg);

        ImageButton av0 = (ImageButton) dialog.findViewById(R.id.av0);
        ImageButton av1 = (ImageButton)dialog.findViewById(R.id.av1);
        ImageButton av2 = (ImageButton)dialog.findViewById(R.id.av2);
        ImageButton av3 = (ImageButton)dialog.findViewById(R.id.av3);
        ImageButton av4 = (ImageButton)dialog.findViewById(R.id.av4);
        ImageButton av5 = (ImageButton)dialog.findViewById(R.id.av5);
        ImageButton av6 = (ImageButton)dialog.findViewById(R.id.av6);
        ImageButton av7 = (ImageButton)dialog.findViewById(R.id.av7);

        av0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAvatar(dialog, "av_0");
            }
        });
        av1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAvatar(dialog, "av_1");
            }
        });
        av2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAvatar(dialog, "av_2");
            }
        });
        av3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAvatar(dialog, "av_3");
            }
        });
        av4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAvatar(dialog, "av_4");
            }
        });
        av5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAvatar(dialog, "av_5");
            }
        });
        av6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAvatar(dialog, "av_6");
            }
        });
        av7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAvatar(dialog, "av_7");
            }
        });

        dialog.show();
    }

    private void changeAvatar (Dialog dialog, String avatar){
        dialog.dismiss();
        ivAvatar.setImageResource(getResources().getIdentifier( avatar , "drawable" , getActivity().getPackageName()));
        User user = preferences.getUser();
        user.setAvatar(avatar);
        preferences.setUser(user);
        Main.main.updateUserDataBase();
    }

    public void setAvatar (){
        String avatar = preferences.getUser().getAvatar();
        ivAvatar.setImageResource(getResources().getIdentifier( avatar , "drawable" , getActivity().getPackageName()));
    }


    private void alertLogOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.alert_logout__message))
                .setTitle(getString(R.string.alert_logout_title))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.b_cancelar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton(getString(R.string.b_logout),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                main.logOut();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void alertOpenLogro(String cat){
        //toast("¡Enhorabuena! Tienes un nuevo logro", 1);
        final Dialog dialog = new Dialog(getActivity());
        int clicks = 0;
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle(getString(R.string.alert_new_logro_title));
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.alert_open_logro);

        ImageView ivLogro = (ImageView) dialog.findViewById(R.id.ivLogro);
        TextView tvProgreso = (TextView) dialog.findViewById(R.id.tvProgreso);
        TextView tvTitulo = (TextView) dialog.findViewById(R.id.tvTitulo);
        ProgressBar pbProgreso = (ProgressBar) dialog.findViewById(R.id.pbProgreso);
        switch (cat){
            case "Fauna": ivLogro.setImageResource(R.drawable.ic_insig_fauna); clicks = preferences.getLogros().getP_fauna(); break;
            case "Nutricion": ivLogro.setImageResource(R.drawable.ic_insig_nutricion); clicks = preferences.getLogros().getP_nutricion(); break;
            case "Medicina": ivLogro.setImageResource(R.drawable.ic_insig_medicina); clicks = preferences.getLogros().getP_medicina(); break;
            case "Agua": ivLogro.setImageResource(R.drawable.ic_insig_agua); clicks = preferences.getLogros().getP_agua(); break;
            case "MedioA": ivLogro.setImageResource(R.drawable.ic_insig_medio_amb); clicks = preferences.getLogros().getP_medioambiente(); break;
            case "Flora": ivLogro.setImageResource(R.drawable.ic_insig_flora); clicks = preferences.getLogros().getP_flora(); break;
            case "InvSocial": ivLogro.setImageResource(R.drawable.ic_insig_inv_social); clicks = preferences.getLogros().getP_investigacion(); break;
            case "Educacion": ivLogro.setImageResource(R.drawable.ic_insig_educacion); clicks = preferences.getLogros().getP_educacion(); break;
            case "Cincomil": ivLogro.setImageResource(R.drawable.ic_insig_cincomil); clicks = preferences.getLogros().getTotal_puntos(); break;
            case "Semana": ivLogro.setImageResource(R.drawable.ic_insig_semana); clicks = -1; break;
            case "Mes": ivLogro.setImageResource(R.drawable.ic_insig_mes); clicks = -1; break;
            case "Publi": ivLogro.setImageResource(R.drawable.ic_insig_publi); clicks = -1; break;
        }

        if (cat.equals("Cincomil")){
            pbProgreso.setMax(5000);
            if (clicks<5000){
                tvTitulo.setText(getString(R.string.tv_logro_falta));
                tvProgreso.setText(""+String.valueOf(clicks)+"/5000");
                pbProgreso.setProgress(clicks);
            }
            else {
                tvTitulo.setText(getString(R.string.tv_logro_conseguido));
                tvProgreso.setVisibility(View.GONE);
                pbProgreso.setVisibility(View.GONE);
            }
        }
        else if (clicks == -1){
            tvTitulo.setText(getString(R.string.tv_logro_no_conseguido));
            tvProgreso.setVisibility(View.GONE);
            pbProgreso.setVisibility(View.GONE);
        }
        else if (clicks<2000){
            tvTitulo.setText(getString(R.string.tv_logro_falta));
            tvProgreso.setText(""+String.valueOf(clicks)+"/2000");
            pbProgreso.setProgress(clicks);
        }
        else {
            tvTitulo.setText(getString(R.string.tv_logro_conseguido));
            tvProgreso.setVisibility(View.GONE);
            pbProgreso.setVisibility(View.GONE);
        }

        dialog.show();
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
}
