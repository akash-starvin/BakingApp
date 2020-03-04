package com.example.baking.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.baking.Constants;
import com.example.baking.R;
import com.squareup.picasso.Picasso;

public class InstructionDetailimageFrag extends Fragment {

    private String imageUrl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if (getArguments().containsKey( Constants.IMAGE_URL )) {
            imageUrl = getArguments().getString( Constants.IMAGE_URL );
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate( R.layout.instruction_detail_image_frag, container, false );

        ImageView imageView = rootView.findViewById(R.id.image_frag);
        if (!imageUrl.isEmpty())
            Picasso.get().load( imageUrl ).into( imageView );
        return rootView;
    }
}
