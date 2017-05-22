package jkydevelop.solidareus;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

import adapter.AdapterProjects;
import projects.Project;
import volley.CheckConnection;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FProjects.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FProjects#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FProjects extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayList<Project> project = new ArrayList<>();
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;

    private View myView = null;
    private FrameLayout layoutLoading, layoutConnect, layoutNoAlerts;
    private SwipeRefreshLayout swipeLayout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FProjects() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FPremios.
     */
    // TODO: Rename and change types and number of parameters
    public static FProjects newInstance(String param1, String param2) {
        FProjects fragment = new FProjects();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_projects, container, false);
        swipeLayout = (SwipeRefreshLayout) myView.findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(this);

        layoutLoading = (FrameLayout) myView.findViewById(R.id.layoutLoading);
        layoutLoading.setVisibility(View.VISIBLE);
        layoutConnect = (FrameLayout) myView.findViewById(R.id.layoutConnect);
        layoutConnect.setVisibility(View.INVISIBLE);
        layoutNoAlerts = (FrameLayout) myView.findViewById(R.id.layoutNoAlerts);
        layoutNoAlerts.setVisibility(View.INVISIBLE);
        ImageView bCheckConnection = (ImageView) myView.findViewById(R.id.bCheckConnection);
        bCheckConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect();
            }
        });




        return myView;
    }

    public void populate(){
        if (CheckConnection.getConnectivityStatus(getActivity())!=0){
            Main.main.loadProjects();
        }
        else {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(layoutLoading!=null) layoutLoading.setVisibility(View.INVISIBLE);
                    if(layoutConnect!=null) layoutConnect.setVisibility(View.VISIBLE);
                }
            }, 2000);
        }
    }

    public void stopSwipeRefresh(){
        if (swipeLayout!=null)
            if (swipeLayout.isRefreshing())
                swipeLayout.setRefreshing(false);
    }

    private void connect() {
        layoutLoading.setVisibility(View.VISIBLE);
        layoutConnect.setVisibility(View.INVISIBLE);
        layoutNoAlerts.setVisibility(View.INVISIBLE);
        if (CheckConnection.getConnectivityStatus(getActivity())!=0){
            Main.main.loadProjects();
        }
        else {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    layoutLoading.setVisibility(View.INVISIBLE);
                    layoutConnect.setVisibility(View.VISIBLE);
                }
            }, 2000);
        }
    }






    public void populateCardView(ArrayList<Project> p, boolean animate){
        project = p;
        //item = array;
        if (project==null){
            if (layoutNoAlerts!=null) layoutNoAlerts.setVisibility(View.VISIBLE);
            if (layoutLoading!=null) layoutLoading.setVisibility(View.INVISIBLE);
            //return;
        }
        else{
            if (layoutNoAlerts!=null) layoutNoAlerts.setVisibility(View.INVISIBLE);
        }
        //Log.d("POPULATING", "array:" + array.toString());
        /*for(int i=0; i<alert.size(); i++){
            String dni = alert.get(i).getTitulo().toLowerCase();
            int image = getResources().getIdentifier("team_"+ dni, "drawable", getActivity().getPackageName());
            if (image!=0)
                alert.get(i).setImage(image);
            else
                alert.get(i).setImage(R.drawable.person);
        }*/
        //myOnClickListener = new MyOnClickListener(getActivity());

        recyclerView = (RecyclerView) myView.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        adapter = new AdapterProjects(project, Main.main, animate);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        layoutLoading.setVisibility(View.INVISIBLE);
        layoutConnect.setVisibility(View.INVISIBLE);


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

    @Override
    public void onRefresh() {
        new myTask().execute();

    }

    private class myTask extends AsyncTask<Void, Void, Void> {

        //initiate vars
        public myTask() {
            super();
            //my params here
        }

        protected Void doInBackground(Void... params) {
            //do stuff
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Main.animate = true;
            Main.main.loadProjects();
        }
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
