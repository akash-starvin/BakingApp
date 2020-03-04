package com.example.baking.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baking.Constants;
import com.example.baking.R;

public class InstructionDetailTextFrag extends Fragment {

    private String data, header, data2;

    public InstructionDetailTextFrag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        if (getArguments().containsKey( Constants.DATA )) {
            data = getArguments().getString( Constants.DATA );
        }else if (getArguments().containsKey( Constants.DESCRIPTION )) {
            data = getArguments().getString( Constants.DESCRIPTION );
        }
        if (getArguments().containsKey( Constants.HEADER )) {
            header = getArguments().getString( Constants.HEADER );
        }else if (getArguments().containsKey( Constants.HEADER2 )) {
            header = getArguments().getString( Constants.HEADER2 );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate( R.layout.instruction_detail_text_frag, container, false );

        ((TextView) rootView.findViewById( R.id.instruction_detail )).setText( data );
        ((TextView) rootView.findViewById( R.id.instruction_header )).setText( header );

        return rootView;
    }
}
