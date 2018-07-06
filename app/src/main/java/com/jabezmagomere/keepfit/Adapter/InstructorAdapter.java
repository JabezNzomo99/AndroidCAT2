package com.jabezmagomere.keepfit.Adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jabezmagomere.keepfit.Models.Instructor;
import com.jabezmagomere.keepfit.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class InstructorAdapter extends RecyclerView.Adapter<InstructorAdapter.InstructorViewHolder> {
    public class InstructorViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName,tvGender,tvPhone,tvEmail;
        private CircleImageView ivInstructor;
        private Button btnText, btnDial, btnMail;

        public InstructorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=(TextView)itemView.findViewById(R.id.tvName);
            tvGender=(TextView)itemView.findViewById(R.id.tvGender);
            tvPhone=(TextView)itemView.findViewById(R.id.tvPhone);
            tvEmail=(TextView)itemView.findViewById(R.id.tvMail);
            ivInstructor=(CircleImageView)itemView.findViewById(R.id.instructor_image);
            btnText=(Button)itemView.findViewById(R.id.btnText);
            btnDial=(Button)itemView.findViewById(R.id.btnDial);
            btnMail=(Button)itemView.findViewById(R.id.btnMail);

        }
    }
    private List<Instructor> instructors;
    private Intent intent;

    public InstructorAdapter(List<Instructor> instructors) {
        this.instructors = instructors;
    }

    @NonNull
    @Override
    public InstructorAdapter.InstructorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.instructor_view,parent,false);
        return new InstructorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructorAdapter.InstructorViewHolder holder, int position) {
        final Instructor instructor=instructors.get(position);
        holder.tvName.setText(instructor.getFirstName()+" "+instructor.getLastName());
        holder.tvGender.setText(instructor.getGender());
        holder.tvEmail.setText(instructor.getEmail());
        holder.tvPhone.setText(instructor.getPhoneNumber());
        Picasso.get().load(instructor.getPhotoURL()).into(holder.ivInstructor);
        holder.btnDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+instructor.getPhoneNumber()));
                view.getContext().startActivity(Intent.createChooser(intent,"Make call using..."));


            }
        });
        holder.btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:"+instructor.getPhoneNumber()));
                intent.putExtra("sms_body","Hi "+instructor.getFirstName()+"!");
                view.getContext().startActivity(Intent.createChooser(intent,"Send text using..."));
            }
        });
        holder.btnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.setType("vnd.android.cursor.item/email");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {instructor.getEmail()});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "INSTRUCTOR BOOKING");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Dear "+instructor.getFirstName());
                view.getContext().startActivity(Intent.createChooser(emailIntent, "Send mail using..."));


            }
        });


    }
    @Override
    public int getItemCount() {
        return instructors.size();
    }
}
