package com.example.mytravel;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.mytravel.models.Travel;

import java.util.List;


//reference : The following code is from our lab 7
public class TravelRecyclerAdapter extends RecyclerView.Adapter<TravelRecyclerAdapter.ViewHolder> {

    //Interface for callbacks
    interface ActionCallback{
        void onLongClickListener(Travel travel);
    }

    private int[] colors;
    private Context context;
    private List<Travel> travelList;
    private ActionCallback mActionCallbacks;

    TravelRecyclerAdapter(Context context,List<Travel> travelList, int[] colors){
        this.context = context;
        this.travelList = travelList;
        this.colors = colors;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.travel_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){holder.bindData(position);}

    public int getItemCount(){return travelList.size();}

    void updateData(List<Travel> travels){
        this.travelList = travels;
        notifyDataSetChanged();
    }

    //View Holder
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        private TextView mCountryTextView;
        private TextView mContinent;
        private GradientDrawable mBackground;

        ViewHolder(View itemView){
            super(itemView);

            itemView.setOnLongClickListener(this);
            mCountryTextView = itemView.findViewById(R.id.label);
            mContinent = itemView.findViewById(R.id.cont);
            mBackground = (GradientDrawable) mContinent.getBackground();
        }

        void bindData(int position){
            Travel travel = travelList.get(position);

            String country = travel.getCountry();
            mCountryTextView.setText(country);

            String continent = travel.getContinent();
            if (continent.equalsIgnoreCase("Europe")) {
                mBackground.setColor(colors[0]);
                mContinent.setText("E");
            } else if (continent.equalsIgnoreCase("America")) {
                mBackground.setColor(colors[1]);
                mContinent.setText("Am");
            } else if (continent.equalsIgnoreCase("Asia")) {
                mBackground.setColor(colors[2]);
                mContinent.setText("As");
            } else if (continent.equalsIgnoreCase("Africa")){
                mBackground.setColor(colors[3]);
                mContinent.setText("Af");
            }
        }

        @Override
        public boolean onLongClick(View v){
            if(mActionCallbacks != null){
                mActionCallbacks.onLongClickListener(travelList.get(getAdapterPosition()));
            }
            return true;
        }
    }
    void addActionCallback(ActionCallback actionCallbacks){ mActionCallbacks = actionCallbacks; }
}
///reference complete
