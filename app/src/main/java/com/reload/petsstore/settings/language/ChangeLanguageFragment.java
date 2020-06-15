package com.reload.petsstore.settings.language;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.reload.petsstore.R;
import com.reload.petsstore.databinding.FragmentChangeLanguageBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeLanguageFragment extends AppCompatDialogFragment {

    FragmentChangeLanguageBinding binding;

    public ChangeLanguageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_change_language,container,false);



        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.english_btn:
                        binding.englishBtn.setTextColor(getActivity().getResources().getColor(R.color.main_color));
                        binding.arabicBtn.setTextColor(getActivity().getResources().getColor(R.color.gray_color));
                        break;

                    case R.id.arabic_btn:
                        binding.arabicBtn.setTextColor(getActivity().getResources().getColor(R.color.main_color));
                        binding.englishBtn.setTextColor(getActivity().getResources().getColor(R.color.gray_color));
                        break;
                }
            }
        });

        return binding.getRoot();
    }

}
