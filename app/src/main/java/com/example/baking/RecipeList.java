package com.example.baking;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.baking.adapter.GridViewAdapter;
import com.example.baking.properties.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class RecipeList extends AppCompatActivity {

    @BindView( R.id.gvRecipes) GridView gridView;
    private static String TAG = "RecipeList";
    private GridViewAdapter gridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_recipe_list );
        ButterKnife.bind(this);

        if (checkInternet())
            loadJSON();
        else
            Toast.makeText( this, "No internet access", Toast.LENGTH_SHORT ).show();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent( RecipeList.this, InstructionListActivity.class);
                intent.putExtra( Constants.RECIPE_ID,position );
                startActivity(intent);
            }
        });

    }
    private boolean checkInternet()
    {
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }
    private void loadJSON() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( Constants.BASE_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<List<Recipe>> call = request.getJSON();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                List<Recipe> jsonResponse = response.body();
                gridViewAdapter = new GridViewAdapter( getApplicationContext(), jsonResponse );
                try {
                    gridView.setAdapter( gridViewAdapter );
                } catch (NullPointerException e) {
                    Log.e( TAG, "Execute Null pointer" );
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                Log.e(TAG, "FAIL: " + t.getMessage());
                Toast.makeText( RecipeList.this, "Error fetching data", Toast.LENGTH_SHORT ).show();
            }
        });
    }
}
