package com.faizmuazzam.kemanisan;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.faizmuazzam.kemanisan.Adapter.AdapterDataBerita;
import com.faizmuazzam.kemanisan.Model.ModelData;
import com.faizmuazzam.kemanisan.Util.AppController;
import com.faizmuazzam.kemanisan.Util.ServerAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link News_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class News_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView mViewNews;
    private RecyclerView.Adapter mAdapterNews;
    private RecyclerView.LayoutManager mManagerNews;
    private ProgressDialog pdNews;
    private List<ModelData> mItemsNews;
    private Animation AnimDev;
    private LinearLayout ltDev;

    public News_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment News_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static News_Fragment newInstance(String param1, String param2) {
        News_Fragment fragment = new News_Fragment();
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
        View view;
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_news, container, false);
//        ltDev = view.findViewById(R.id.layoutDev);
//        AnimDev = AnimationUtils.loadAnimation(getContext(), R.anim.button_animation);
//        ltDev.setAnimation(AnimDev);
        //        Berita
        mViewNews = (RecyclerView) view.findViewById(R.id.recyclerviewNews);
        pdNews = new ProgressDialog(getActivity());
        mItemsNews = new ArrayList<>();

        loadNews();

        mManagerNews = new LinearLayoutManager(getActivity());
        mViewNews.setLayoutManager(mManagerNews);
        mAdapterNews = new AdapterDataBerita(getContext(), mItemsNews);
        mViewNews.setAdapter(mAdapterNews);
        return view;

        
    }

    private void loadNews() {
        JsonArrayRequest reqDataBerita = new JsonArrayRequest(Request.Method.POST, ServerAPI.URL_BERITA, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println("Length :" + response.length());
                        pdNews.cancel();
                        Log.d("volley", "response : " + response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject data = response.getJSONObject(i);
                                ModelData mdBerita = new ModelData();
                                mdBerita.setSub_judul_berita(data.getString("sub_judul"));
                                mdBerita.setJudul_berita(data.getString("judul"));
                                mdBerita.setIsi_berita(data.getString("isi_berita"));
                                mItemsNews.add(mdBerita);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.println("Length :" + response.length());
                        mAdapterNews.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pdNews.cancel();
                        Log.d("volley", "error : " + error.getMessage());
                    }
                });
        AppController.getInstance().addToRequestQueue(reqDataBerita);
    }
}