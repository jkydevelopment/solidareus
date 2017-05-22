package jkydevelop.solidareus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaderFactory;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import projects.Project;
import volley.EndPoints;

public class ProjectRead extends AppCompatActivity {

    private Project project;
    private int id = -1;
    ImageView ivMenu;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_read);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        project = (Project) getIntent().getSerializableExtra("project");
        id = getIntent().getIntExtra("id", -1);

        setTitle(project.getTitulo());

        TextView tvTitulo = (TextView) findViewById(R.id.tvTitulo);
        TextView tvDescripcion = (TextView) findViewById(R.id.tvDescripcion);
        TextView tvTexto = (TextView) findViewById(R.id.tvTexto);
        TextView tvPuntos = (TextView) findViewById(R.id.tvPuntos);
        ImageView ivImagen = (ImageView) findViewById(R.id.ivImagen);

        Button bLink = (Button) findViewById(R.id.bLink);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (project.getEnlace() == null){
            bLink.setVisibility(View.INVISIBLE);
        }
        else if (project.getEnlace().equals("")){
            bLink.setVisibility(View.INVISIBLE);
        }
        else if (!project.getEnlace().equals("")){
            bLink.setVisibility(View.VISIBLE);
            bLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String link = project.getEnlace();
                    //if(Patterns.WEB_URL.matcher(link).matches()){
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(link));
                        startActivity(i);
                    //}
                    //else {
                    //    toast(getString(R.string.toast_impossible_open_website), 0);
                    //}

                }
            });
        }

        // Si hay una imagen almacenada en esta alerta
        if (!project.getImagen().equals("") && project.getImagen()!=null){
            progressBar.setVisibility(View.VISIBLE);
            String url = EndPoints.URL_IMAGE_PROJECT + project.getImagen()+".jpg";
            setImageView(url, ivImagen);
        }
        else
            progressBar.setVisibility(View.GONE);



        tvTitulo.setText(project.getTitulo());
        tvDescripcion.setText("«"+ project.getDescripcion()+"»");
        tvTexto.setText(project.getTexto());
        tvPuntos.setText("«"+getString(R.string.tv_puntos_conseguidos) + " " + project.getPuntos()+"»");


    }

    private void setImageView(String url, ImageView imageView){
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Authorization", new LazyHeaderFactory() {
                    @Override
                    public String buildHeader() {
                        String expensiveAuthHeader = "Basic " +  Base64.encodeToString(EndPoints.AUTH_USER_PASS.getBytes(), Base64.NO_WRAP);
                        return expensiveAuthHeader;
                    }
                })

                .build());
        Glide.with(this).load(glideUrl).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                .listener(new RequestListener<GlideUrl, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, GlideUrl model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, GlideUrl model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void toast(String texto, int length){
        Toast.makeText(this, "" + texto, length).show();
    }

}


