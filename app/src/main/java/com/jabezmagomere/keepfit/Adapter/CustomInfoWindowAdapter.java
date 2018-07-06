package com.jabezmagomere.keepfit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.jabezmagomere.keepfit.Models.Gym;
import com.jabezmagomere.keepfit.R;

import es.dmoral.toasty.Toasty;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private View mWindow;
    private Context context;
    public CustomInfoWindowAdapter(Context context){
        this.context=context;
        mWindow= LayoutInflater.from(context).inflate(R.layout.custom_info_window,null);

    }
    public void renderWindow(Marker marker, View view){
        TextView tvGymName, tvRating, tvPhone, tvOpen, tvClose;
        Gym gym=(Gym)marker.getTag();
        if(marker.getTag()==null){
            Toasty.info(view.getContext(),"User Location", Toast.LENGTH_SHORT,true).show();

        }else{
            tvGymName=(TextView)view.findViewById(R.id.tvGymName);
            if(!gym.getGymName().equals("")){
                tvGymName.setText(gym.getGymName());
            }
            tvRating=(TextView)view.findViewById(R.id.tvRating);
            if(!gym.getRating().equals("")){
                tvRating.setText(gym.getRating());

            }
            tvPhone=(TextView)view.findViewById(R.id.tvPhone);
            if(!gym.getPhone().equals("")){
                tvPhone.setText(gym.getPhone());
            }

            tvOpen=(TextView)view.findViewById(R.id.tvOpen);
            if(!gym.getOpen().equals("")){
                tvOpen.setText(gym.getOpen());
            }
            tvClose=(TextView)view.findViewById(R.id.tvClose);
            if(!gym.getClose().equals("")){
                tvClose.setText(gym.getClose());
            }

        }


    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindow(marker,mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {

        renderWindow(marker,mWindow);
        return mWindow;
    }
}
