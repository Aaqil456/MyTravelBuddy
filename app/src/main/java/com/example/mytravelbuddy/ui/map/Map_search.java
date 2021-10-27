package com.example.mytravelbuddy.ui.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mytravelbuddy.R;
import com.example.mytravelbuddy.RequestLocationUpdatesWithCallbackActivity;
import com.example.mytravelbuddy.databinding.FragmentMapsearchBinding;

public class Map_search extends Fragment {

    private FragmentMapsearchBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapsearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView tv=root.findViewById(R.id.text_notifications);
        tv.setText("Map Fragments");

        startActivity(new Intent(getActivity(), RequestLocationUpdatesWithCallbackActivity.class));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}