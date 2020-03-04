package com.example.baking;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import android.view.MenuItem;
import com.example.baking.fragment.InstructionDetailTextFrag;
import com.example.baking.fragment.InstructionDetailVideoFrag;
import com.example.baking.fragment.InstructionDetailimageFrag;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InstructionDetailActivity extends AppCompatActivity {

    @BindView( R.id.detail_toolbar ) Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_instruction_detail );
        ButterKnife.bind( this );

        Intent intent = getIntent();
        String s = intent.getStringExtra( Constants.STEP );
        toolbar.setTitle( s );
        setSupportActionBar( toolbar );

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled( true );
        }

        if (savedInstanceState == null) {

            Bundle argsIngredients = new Bundle();
            argsIngredients.putString( Constants.DATA, getIntent().getStringExtra( Constants.DATA ) );
            argsIngredients.putString( Constants.HEADER, getIntent().getStringExtra( Constants.HEADER) );
            InstructionDetailTextFrag fragIngredients = new InstructionDetailTextFrag();
            fragIngredients .setArguments( argsIngredients );
            getSupportFragmentManager().beginTransaction().add( R.id.instruction_detail_container_ingredients, fragIngredients  ).commit();

            Bundle argsDescription = new Bundle();
            argsDescription.putString( Constants.DESCRIPTION, getIntent().getStringExtra( Constants.DESCRIPTION ) );
            argsDescription.putString( Constants.HEADER2, getIntent().getStringExtra( Constants.HEADER2 ) );
            InstructionDetailTextFrag fragDescription = new InstructionDetailTextFrag();
            fragDescription.setArguments( argsDescription );
            getSupportFragmentManager().beginTransaction().add( R.id.instruction_detail_container_des, fragDescription ).commit();

            if (getIntent().getStringExtra( Constants.VIDEO_URL ) != null) {
                Bundle argsVideoUrl = new Bundle();
                argsVideoUrl.putString(Constants.VIDEO_URL, getIntent().getStringExtra( Constants.VIDEO_URL ) );
                InstructionDetailVideoFrag fragVideo = new InstructionDetailVideoFrag();
                fragVideo.setArguments( argsVideoUrl );
                getSupportFragmentManager().beginTransaction().add( R.id.instruction_detail_container_video, fragVideo ).commit();
            }
            if (getIntent().getStringExtra( Constants.IMAGE_URL ) != null) {
                Bundle argsImage = new Bundle();
                argsImage.putString( Constants.IMAGE_URL, getIntent().getStringExtra( Constants.IMAGE_URL ) );
                InstructionDetailimageFrag fragImage = new InstructionDetailimageFrag();
                fragImage.setArguments( argsImage );
                getSupportFragmentManager().beginTransaction().add( R.id.instruction_detail_container_img, fragImage ).commit();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo( new Intent( this, InstructionListActivity.class ) );
            return true;
        }
        return super.onOptionsItemSelected( item );
    }
}
