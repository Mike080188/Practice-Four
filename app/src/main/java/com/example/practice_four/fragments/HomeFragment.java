package com.example.practice_four.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.practice_four.R;

public class HomeFragment extends Fragment {

    private TextView mTextViewEmail;

    View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_home, container, false);
        mTextViewEmail = root.findViewById(R.id.text_view_email);
        String email = getActivity().getIntent().getStringExtra("email");
        mTextViewEmail.setText("User: " + email);

        return root;
    }
}
