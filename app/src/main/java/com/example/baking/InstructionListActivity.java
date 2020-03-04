package com.example.baking;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.example.baking.fragment.InstructionDetailTextFrag;
import com.example.baking.fragment.InstructionDetailVideoFrag;
import com.example.baking.fragment.InstructionDetailimageFrag;
import com.example.baking.properties.Ingredient;
import com.example.baking.properties.Recipe;
import com.example.baking.properties.Step;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * An activity representing a list of Instructions. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link InstructionDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class InstructionListActivity extends AppCompatActivity {

    @BindView( R.id.instruction_list ) RecyclerView recyclerView;
    @BindView( R.id.toolbar ) Toolbar toolbar;
    @BindView( R.id.fab  ) FloatingActionButton fabWidget;

    private boolean mTwoPane;
    private StringBuilder result = new StringBuilder();
    private int iRecipeId;
    public static String sJsonId,sJsonRecipeName,sJsonShortDes,sJsonDes, sJsonVideoUrl, sJsonIngredients;
    private ModelSteps model;
    private ArrayList<ModelSteps> arrayList = new ArrayList<ModelSteps>(  );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_instruction_list );
        ButterKnife.bind(this);
        setSupportActionBar( toolbar );

        fabWidget.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
                Bundle bundle = new Bundle();
                int appWidgetId = bundle.getInt(
                        AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
                Toast.makeText(getApplicationContext(), "Added " + sJsonRecipeName +" to Widget.", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_TAG, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString( Constants.SHARED_PREFERENCE_RECIPE_NAME, sJsonRecipeName );
                editor.putString( Constants.SHARED_PREFERENCE_RECIPE_INGREDIENTS, result.toString() );
                editor.commit();
                RecipesWidget.updateAppWidget(getApplicationContext(), appWidgetManager, appWidgetId, sJsonRecipeName, result.toString());

            }
        } );
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById( R.id.instruction_detail_container_ingredients ) != null) {
            mTwoPane = true;
        }

        assert recyclerView != null;
        iRecipeId = getIntent().getIntExtra( Constants.RECIPE_ID,0 );
        if (checkInternet())
            loadJSON();
        else
            Toast.makeText( this, "No internet access", Toast.LENGTH_SHORT ).show();
    }
    private boolean checkInternet()
    {
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }
    private void loadJSON() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl( Constants.BASE_URL ).client( new OkHttpClient() ).addConverterFactory( GsonConverterFactory.create() ).build();

        RequestInterface request = retrofit.create( RequestInterface.class );
        Call<List<Recipe>> call = request.getJSON();

        call.enqueue( new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {

                List<Recipe> jsonResponse = response.body();
                sJsonRecipeName = jsonResponse.get( iRecipeId ).getName();
                Constants.IMAGE_URL2 = jsonResponse.get( iRecipeId ).getImage();
                toolbar.setTitle(sJsonRecipeName  );
                for (Ingredient ingredient :  jsonResponse.get( iRecipeId ).getIngredients()){
                    result.append(ingredient.getDoseStr()).append(" ").append(ingredient.getIngredient()).append("\n");
                }
                sJsonIngredients = result.toString();
                int i = 0;
                for (Step step :  jsonResponse.get( iRecipeId ).getSteps()){
                    sJsonId = "Step "+step.getId();
                    sJsonShortDes = jsonResponse.get( iRecipeId ).getSteps().get( i ).getShortDescription() + "";
                    sJsonDes = jsonResponse.get( iRecipeId ).getSteps().get( i ).getDescription() + "";
                    sJsonVideoUrl = jsonResponse.get( iRecipeId ).getSteps().get( i ).getVideoURL() + "";
                    model = new ModelSteps(sJsonId,sJsonShortDes, sJsonDes,sJsonVideoUrl);
                    i++;
                    arrayList.add( model );
                }
                SimpleItemRecyclerViewAdapter simpleItemRecyclerViewAdapter = new SimpleItemRecyclerViewAdapter(InstructionListActivity.this,arrayList,mTwoPane);
                recyclerView.setAdapter( simpleItemRecyclerViewAdapter );
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                Toast.makeText( InstructionListActivity.this, "Error fetching data", Toast.LENGTH_SHORT ).show();
            }
        } );
    }

    public static class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final InstructionListActivity mParentActivity;
        private final List<ModelSteps> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModelSteps modelSteps = (ModelSteps) view.getTag();

                if (mTwoPane) {
                    Bundle argsIngredients = new Bundle();
                    argsIngredients.putString( Constants.DATA, sJsonIngredients );
                    argsIngredients.putString( Constants.HEADER, "Ingredients" );
                    InstructionDetailTextFrag fragIngredients = new InstructionDetailTextFrag();
                    fragIngredients.setArguments( argsIngredients );
                    mParentActivity.getSupportFragmentManager().beginTransaction().replace( R.id.instruction_detail_container_ingredients, fragIngredients ).commit();

                    Bundle argsDescription = new Bundle();
                    argsDescription.putString( Constants.DATA, modelSteps.des );
                    argsDescription.putString( Constants.HEADER2, "Step Description" );
                    InstructionDetailTextFrag fragDescription = new InstructionDetailTextFrag();
                    fragDescription.setArguments( argsDescription );
                    mParentActivity.getSupportFragmentManager().beginTransaction().replace( R.id.instruction_detail_container_des, fragDescription ).commit();

                    Bundle argsVideoUrl = new Bundle();
                    InstructionDetailVideoFrag fragVideo = new InstructionDetailVideoFrag();
                    if (modelSteps.videoUrl.isEmpty())
                        argsVideoUrl.putString( Constants.VIDEO_URL, "000" );
                    else
                        argsVideoUrl.putString( Constants.VIDEO_URL, modelSteps.videoUrl );
                    fragVideo.setArguments( argsVideoUrl );
                    mParentActivity.getSupportFragmentManager().beginTransaction().replace( R.id.instruction_detail_container_video, fragVideo).commit();

                    if (!Constants.IMAGE_URL.isEmpty() ) {
                        Bundle argsImage = new Bundle();
                        argsImage.putString( Constants.IMAGE_URL,Constants.IMAGE_URL2 );
                        InstructionDetailimageFrag fragImageUrl = new InstructionDetailimageFrag();
                        fragImageUrl.setArguments( argsImage );
                        mParentActivity.getSupportFragmentManager().beginTransaction().replace( R.id.instruction_detail_container_img, fragImageUrl ).commit();
                    }
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent( context, InstructionDetailActivity.class );
                    intent.putExtra( Constants.STEP, modelSteps.id+"" );
                    intent.putExtra( Constants.HEADER, "Ingredients" );
                    intent.putExtra( Constants.DATA, sJsonIngredients);

                    intent.putExtra( Constants.HEADER2, "Step Description" );
                    intent.putExtra( Constants.DESCRIPTION, modelSteps.des );

                    if (modelSteps.videoUrl.isEmpty())
                        intent.putExtra(Constants.VIDEO_URL , "000" );
                    else
                        intent.putExtra( Constants.VIDEO_URL, modelSteps.videoUrl );

                    if (!Constants.IMAGE_URL.isEmpty())
                        intent.putExtra( Constants.IMAGE_URL, Constants.IMAGE_URL2);
                    context.startActivity( intent );
                }
            }
        };

        SimpleItemRecyclerViewAdapter( InstructionListActivity parent,List<ModelSteps> items, boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.sticker_step, parent, false );
            return new ViewHolder( view );
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText( mValues.get( position ).getId() );
            holder.mContentView.setText( mValues.get( position ).getShortDes() );

            holder.itemView.setTag( mValues.get( position ) );
            holder.itemView.setOnClickListener( mOnClickListener );
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super( view );
                mIdView = view.findViewById( R.id.sticker_tv_step_count );
                mContentView = view.findViewById( R.id.sticker_tv_step_description );
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

}
