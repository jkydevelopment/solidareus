package adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import jkydevelop.solidareus.Main;
import jkydevelop.solidareus.R;
import projects.Project;
import user.Preferences;


/**
 * Created by Juan Carlos on 11/04/2017.
 *
 * http://www.journaldev.com/10024/android-recyclerview-android-cardview-example-tutorial
 */

public class AdapterProjects extends RecyclerView.Adapter<AdapterProjects.MyViewHolder> {

    private int lastPosition = -1;

    private Main main;
    private boolean animate = false;
    Preferences preferences = null;
    private ArrayList<Project> project;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView my_cardview;
        ImageView imageViewIcon;
        TextView tvTitulo;
        TextView tvDescripcion;
        TextView tvFecha;
        ImageView tvStar;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.my_cardview = (CardView) itemView.findViewById(R.id.my_cardview);
            this.tvTitulo = (TextView) itemView.findViewById(R.id.tvTitulo);
            this.tvDescripcion = (TextView) itemView.findViewById(R.id.tvDescripcion);
            this.tvFecha = (TextView) itemView.findViewById(R.id.tvFecha);
            this.tvStar = (ImageView) itemView.findViewById(R.id.tvStar);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.ivFoto);


        }
    }

    public AdapterProjects(ArrayList<Project> project, Main main, boolean animate) {
        this.project = project;
        this.main = main;
        this.animate = animate;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_proyecto, parent, false);

        //view.setOnClickListener(AFragmentAlert.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        preferences = new Preferences(main);

        TextView tvTitulo = holder.tvTitulo;
        TextView tvDescripcion = holder.tvDescripcion;
        TextView tvFecha = holder.tvFecha;
        ImageView tvStar = holder.tvStar;
        ImageView imageView = holder.imageViewIcon;
        CardView my_cardview = holder.my_cardview;

        tvTitulo.setText(project.get(listPosition).getTitulo());
        tvDescripcion.setText(project.get(listPosition).getDescripcion());
        tvFecha.setText(project.get(listPosition).getFecha());
        if (preferences.getUser().getId_project() == project.get(listPosition).getId()) {
            tvStar.setImageResource(R.drawable.ic_star_selected);
            main.setSelectedProject(project.get(listPosition).getId(), project.get(listPosition).getCategoria());
        }
        else
            tvStar.setImageResource(R.drawable.ic_star_not_selected);


        //tvTexto.setText("«"+alert.get(listPosition).getTexto()+"»");
        //tvTexto.setText("«"+navigationUser.getString(R.string.tv_touch_to_read)+"»");

        // Si hay una imagen almacenada en esta alerta
        //Log.d("listPosition", "imagen: " + alert.get(listPosition).getImagen());
        /*if (!project.get(listPosition).getImagen().equals("") && project.get(listPosition).getImagen()!=null){
            String url = URL_ALERT_IMAGE + alert.get(listPosition).getImagen()+".jpg";
            setImageView(url, imageView);
        }
        else {*/
        switch (project.get(listPosition).getCategoria()){
            case "Fauna": imageView.setImageResource(R.drawable.ic_cat_fauna); imageView.setBackgroundColor(ContextCompat.getColor(main, R.color.colorFauna)); break;
            case "Nutricion": imageView.setImageResource(R.drawable.ic_cat_nutricion); imageView.setBackgroundColor(ContextCompat.getColor(main, R.color.colorNutricion)); break;
            case "Medicina": imageView.setImageResource(R.drawable.ic_cat_medicina); imageView.setBackgroundColor(ContextCompat.getColor(main, R.color.colorMedicina)); break;
            case "Agua": imageView.setImageResource(R.drawable.ic_cat_agua); imageView.setBackgroundColor(ContextCompat.getColor(main, R.color.colorAgua)); break;
            case "MedioA": imageView.setImageResource(R.drawable.ic_cat_medioa); imageView.setBackgroundColor(ContextCompat.getColor(main, R.color.colorMedioAmbiente)); break;
            case "Flora": imageView.setImageResource(R.drawable.ic_cat_flora); imageView.setBackgroundColor(ContextCompat.getColor(main, R.color.colorFlora)); break;
            case "InvSocial": imageView.setImageResource(R.drawable.ic_cat_invsocial); imageView.setBackgroundColor(ContextCompat.getColor(main, R.color.colorInvestigacion)); break;
            case "Educacion": imageView.setImageResource(R.drawable.ic_cat_educacion); imageView.setBackgroundColor(ContextCompat.getColor(main, R.color.colorEducacion)); break;
            default: imageView.setImageResource(R.drawable.ic_cat_agua); imageView.setBackgroundColor(ContextCompat.getColor(main, R.color.colorAccent)); break;
        }

        //}

        tvStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectProject(view, listPosition);
            }
        });

        if (animate)
            setAnimation(holder.itemView, listPosition);


        holder.my_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.openProject(project.get(listPosition).getId(), listPosition);
                //Log.d("ITEM_CLICKED", "Holder: " + listPosition);
            }
        });
    }



    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(main, R.anim.slide_in_left_down);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        if(project!=null)
            return project.size();
        else
            return -1;
    }


    private void selectProject(final View view, final int position) {
        animate = true;
        main.selectProject(project.get(position).getId(), position);
    }

}