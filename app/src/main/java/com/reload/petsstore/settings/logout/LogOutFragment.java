package com.reload.petsstore.settings.logout;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.reload.petsstore.R;
import com.reload.petsstore.common.SessionMangment;
import com.reload.petsstore.databinding.FragmentLogOutBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogOutFragment extends AppCompatDialogFragment {

    FragmentLogOutBinding binding;
    SessionMangment mSessionMangment;

    public LogOutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_log_out,container,false);
        mSessionMangment = new SessionMangment(getActivity());
        binding.logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSessionMangment.logoutUser();
            }
        });

        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return binding.getRoot();
    }

}
