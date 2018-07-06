package com.jabezmagomere.keepfit.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jabezmagomere.keepfit.Models.WorkOut;
import com.jabezmagomere.keepfit.R;
import com.jabezmagomere.keepfit.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class WorkOutAdapter extends RecyclerView.Adapter<WorkOutAdapter.MyViewHolder> {
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvExercise, tvDate, tvLocation, tvReps, tvSets;
        ImageView ivWorkOut;
        ImageButton btnDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExercise=(TextView)itemView.findViewById(R.id.tvExcercise);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLocation);
            tvReps=(TextView)itemView.findViewById(R.id.tvReps);
            tvSets=(TextView)itemView.findViewById(R.id.tvSets);
            ivWorkOut=(ImageView) itemView.findViewById(R.id.ivWorkOut);
            btnDelete=(ImageButton)itemView.findViewById(R.id.btnDelete);

        }
    }
    private List<WorkOut> workOutList;
    private RequestQueue requestQueue;
    private static final String DELETE_URL="https://sleepy-beyond-36756.herokuapp.com/api/DeleteWorkOut";
    SessionManager sessionManager;
    private static final String TOKEN = "token";

    public WorkOutAdapter(List<WorkOut> workOutList) {
        this.workOutList = workOutList;
    }

    @NonNull
    @Override
    public WorkOutAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkOutAdapter.MyViewHolder holder, final int position) {
        WorkOut workOut=workOutList.get(position);
        final String workout_id=workOut.getId();
        holder.tvExercise.setText(workOut.getExcerciseName());
        holder.tvDate.setText(workOut.getDate());
        holder.tvLocation.setText(workOut.getLocation());
        holder.tvReps.setText(holder.itemView.getResources().getString(R.string.workout_reps)+": "+workOut.getReps());
        holder.tvSets.setText(holder.itemView.getResources().getString(R.string.workout_sets)+": "+workOut.getSets());
        Picasso.get().load(R.drawable.notepad).into(holder.ivWorkOut);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                builder.setMessage(view.getResources().getString(R.string.alert_dialog_stmt));
                builder.setCancelable(true);
                builder.setPositiveButton(view.getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                final ProgressDialog progressDialog=new ProgressDialog(view.getContext());
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.setMessage(view.getResources().getString(R.string.alert_dialog_stmt));
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                sessionManager=new SessionManager(view.getContext());
                                HashMap<String,String> user=sessionManager.getUserDetails();
                                final String token=user.get(TOKEN);
                                requestQueue= Volley.newRequestQueue(view.getContext());
                                HashMap<String,String> params=new HashMap<String, String>();
                                params.put("workout_id",workout_id);
                                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, DELETE_URL, new JSONObject(params), new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String res=response.getString("response");
                                            if(progressDialog!=null && progressDialog.isShowing()){
                                                progressDialog.dismiss();
                                            }
                                            removeAt(position);
                                            Toasty.success(view.getContext(),res, Toast.LENGTH_SHORT,true).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        if(progressDialog!=null && progressDialog.isShowing()){
                                            progressDialog.dismiss();
                                        }
                                        Toasty.error(view.getContext(),view.getResources().getString(R.string.error_occured), Toast.LENGTH_SHORT,true).show();
                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        HashMap<String,String> params=new HashMap<String, String>();
                                        params.put("workout_id",workout_id);
                                        return params;
                                    }

                                    @Override
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        HashMap<String,String> headers=new HashMap<String, String>();
                                        headers.put("Content-Type","application/json");
                                        headers.put("Authorization","Bearer"+" "+token);
                                        return headers;
                                    }
                                };
                                requestQueue.add(jsonObjectRequest);
                            }
                        });
                builder.setNegativeButton(view.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();

            }
        });
    }
    public void removeAt(int position){
        workOutList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,workOutList.size());
    }

    @Override
    public int getItemCount() {
        return workOutList.size();
    }
}
