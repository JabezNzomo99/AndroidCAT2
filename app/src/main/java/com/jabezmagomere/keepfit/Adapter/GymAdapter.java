package com.jabezmagomere.keepfit.Adapter;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jabezmagomere.keepfit.Models.Gym;
import com.jabezmagomere.keepfit.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GymAdapter extends RecyclerView.Adapter<GymAdapter.MyViewHolder> {
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvGym, tvRate, tvOpen, tvClose;
        private Button btnBook;
        private ImageView ivGym;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGym = (TextView) itemView.findViewById(R.id.tvGym);
            tvRate = (TextView) itemView.findViewById(R.id.tvRate);
            btnBook = (Button) itemView.findViewById(R.id.btnBook);
            ivGym = (ImageView) itemView.findViewById(R.id.ivGym);
        }
    }

    private List<Gym> gymList;

    public GymAdapter(List<Gym> gymList) {
        this.gymList = gymList;
    }

    @NonNull
    @Override
    public GymAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gym_view, null);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GymAdapter.MyViewHolder holder, int position) {
        final Gym gym = gymList.get(position);
        holder.tvGym.setText(gym.getGymName());
        holder.tvRate.setText(gym.getRating());
        Picasso.get().load(R.drawable.dumbbell).into(holder.ivGym);
        holder.btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog=new ProgressDialog(view.getContext());
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+gym.getPhone()));
                view.getContext().startActivity(Intent.createChooser(intent,"Make call using..."));
                if(progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }


            }
        });



    }

    @Override
    public int getItemCount() {
        return gymList.size();
    }
}
