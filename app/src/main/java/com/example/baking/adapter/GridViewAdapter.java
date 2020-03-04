package com.example.baking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baking.R;
import com.example.baking.properties.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<Recipe> modelList;
    private ArrayList<Recipe> arrayList;

    public GridViewAdapter(Context c, List<Recipe> modelData) {
        this.mContext = c;
        this.modelList = modelData;
        inflater = LayoutInflater.from( mContext );
        this.arrayList = new ArrayList<Recipe>(  );
        this.arrayList.addAll( modelList );
    }

    public class ViewHolder
    {
        ImageView mPackImg;
        TextView mPackRecipeName, mPackServingCount;
    }

    @Override
    public int getCount() {
        return modelList==null?0:modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final ViewHolder holder;
        if(view == null)
        {
            holder = new ViewHolder();
            view = inflater.inflate( R.layout.sticker_recipe,  null);
            holder.mPackImg = view.findViewById(R.id.sticker_img );
            holder.mPackRecipeName = view.findViewById( R.id.sticker_recipe_name );
            holder.mPackServingCount = view.findViewById( R.id.sticker_serving_count );
            view.setTag( holder );
        }
        else
        {
            holder = (ViewHolder)view.getTag();
        }
        try {
            if (!modelList.get(position).getImage().isEmpty())
                Picasso.get().load(modelList.get(position).getImage()).into(holder.mPackImg);
            else
                holder.mPackImg.setImageResource( R.drawable.img_plate );

            holder.mPackRecipeName.setText( modelList.get( position ).getName() );
            holder.mPackServingCount.setText(modelList.get( position ).getServings() +R.string.servings);
        }
        catch (Exception e){

        }
        return view;
    }
}
