package com.apps.dsb.radioadaja;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentReproductor extends Fragment {

    //private Button ButtonRight, ButtonLeft;
    public static final int RADIO = 0;
    public static final int PODCAST = 1;
    private LinearLayout linearlinear;
    private NonScrollListView lista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reproductor, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        //ButtonRight = (Button) getView().findViewById(R.id.right);
        //ButtonLeft = (Button) getView().findViewById(R.id.left);

        //Poner altura relativa al linearlayout
        linearlinear = (LinearLayout) getView().findViewById(R.id.lstCur);
        // Changes the height and width to the specified *pixels*
        linearlinear.getLayoutParams().width= ((android.view.WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        linearlinear.getLayoutParams().height = (int) (((android.view.WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight() * 0.7);
        linearlinear.setLayoutParams(linearlinear.getLayoutParams());


        lista = getView().findViewById(R.id.lista);
        ArrayList<Category> category = new ArrayList<>();
        for(int i = 0; i < 9; i++){
            Category c = new Category();
            c.setDescription("Descripción detallada del programa");
            c.setTittle("Programa " + (i+1));
            category.add(c);
        }

        AdapterCategory adapter = new AdapterCategory(getActivity(), category);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "" + position, Toast.LENGTH_LONG);
            }
        });
    }


}