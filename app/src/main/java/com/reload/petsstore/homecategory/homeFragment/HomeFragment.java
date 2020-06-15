package com.reload.petsstore.homecategory.homeFragment;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reload.petsstore.R;
import com.reload.petsstore.common.ApiService;
import com.reload.petsstore.common.WebServiceClient;
import com.reload.petsstore.databinding.FragmentHomeBinding;
import com.reload.petsstore.homecategory.HomeActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    ArrayList<HomeCatResult> mList;
    HomeAdapter mAdapter;
    HomeViewModel homeViewModel;

    FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_home,container,false);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        initViews();
        return binding.getRoot();
    }

    private void initViews() {
        mList = new ArrayList<>();
        homeViewModel.getCatResponse().observe(this, new Observer<HomeCatResponse>() {
            @Override
            public void onChanged(HomeCatResponse homeCatResponse) {
                mList = (ArrayList<HomeCatResult>) homeCatResponse.getResult();
                mAdapter.setList(mList);
            }
        });
        mAdapter = new HomeAdapter(mList, getActivity());
        binding.homeRv.setAdapter(mAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        binding.homeRv.setLayoutManager(layoutManager);

    }

}
