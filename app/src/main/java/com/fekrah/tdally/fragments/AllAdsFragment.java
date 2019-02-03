package com.fekrah.tdally.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fekrah.tdally.R;
import com.fekrah.tdally.adapters.AdsAdapter;
import com.fekrah.tdally.models.Ad;
import com.fekrah.tdally.models.AllAdsResponse;
import com.fekrah.tdally.server.Apis;
import com.fekrah.tdally.server.BaseClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllAdsFragment extends Fragment {

    private ShowAllAdsListeneer mListener;

    @BindView(R.id.all_ads)
    RecyclerView allAdsRecyclerView;

    LinearLayoutManager linearLayoutManager;
    List<Ad> ads;
    AdsAdapter adsAdapter;
    public AllAdsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_ads, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        Apis apis = BaseClient.getBaseClient().create(Apis.class);
        apis.getAllAds("5","all").enqueue(new Callback<AllAdsResponse>() {
            @Override
            public void onResponse(Call<AllAdsResponse> call, Response<AllAdsResponse> response) {
                if (response.body()!=null){
                    linearLayoutManager = new LinearLayoutManager(getActivity());
                    adsAdapter = new AdsAdapter(getActivity(),response.body().getData());
                    allAdsRecyclerView.setLayoutManager(linearLayoutManager);
                    allAdsRecyclerView.setAdapter(adsAdapter);
                }
            }

            @Override
            public void onFailure(Call<AllAdsResponse> call, Throwable t) {

            }
        });


    }

    private void addAds() {
        ads.add(new Ad("","android developer","125","5","120","200000","13/8/2018"));
        ads.add(new Ad("","android developer","125","5","120","200000","13/8/2018"));
        ads.add(new Ad("","android developer","125","5","120","200000","13/8/2018"));
        ads.add(new Ad("","android developer","125","5","120","200000","13/8/2018"));
        ads.add(new Ad("","android developer","125","5","120","200000","13/8/2018"));
        ads.add(new Ad("","android developer","125","5","120","200000","13/8/2018"));
        ads.add(new Ad("","android developer","125","5","120","200000","13/8/2018"));
        ads.add(new Ad("","android developer","125","5","120","200000","13/8/2018"));
        ads.add(new Ad("","android developer","125","5","120","200000","13/8/2018"));
        ads.add(new Ad("","android developer","125","5","120","200000","13/8/2018"));
        ads.add(new Ad("","android developer","125","5","120","200000","13/8/2018"));
        ads.add(new Ad("","android developer","125","5","120","200000","13/8/2018"));
        ads.add(new Ad("","android developer","125","5","120","200000","13/8/2018"));
        ads.add(new Ad("","android developer","125","5","120","200000","13/8/2018"));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShowAllAdsListeneer) {
            mListener = (ShowAllAdsListeneer) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ShowAllAdsListeneer");
        }
    }

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
    public interface ShowAllAdsListeneer {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
