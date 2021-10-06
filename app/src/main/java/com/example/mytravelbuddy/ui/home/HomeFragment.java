package com.example.mytravelbuddy.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mytravelbuddy.Map_Activity;
import com.example.mytravelbuddy.R;
import com.example.mytravelbuddy.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {


    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView tv=root.findViewById(R.id.text_home);
        tv.setText("Home Fragments");
        Button btnMap=root.findViewById(R.id.buttonMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Map_Activity.class));
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}