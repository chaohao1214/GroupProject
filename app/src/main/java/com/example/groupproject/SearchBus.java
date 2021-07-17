package com.example.groupproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SearchBus extends AppCompatActivity {
    RecyclerView searchView;
    MyBusAdapter busAdt = new MyBusAdapter();
    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
    ArrayList<BusMessage> searchMessage = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_search);

        //receive info from previous page
        Intent fromPreOC = getIntent();
        // save the content that entered before
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String searchBusInfo = prefs.getString("BusNumber","");
        EditText busMsg = findViewById(R.id.searchBox);
        busMsg.setText(searchBusInfo);


        Button searchBusBtn = findViewById(R.id.searchBusButton);
        searchView = findViewById(R.id.searchView);
        LinearLayoutManager mgr = new LinearLayoutManager(this);
        mgr.setStackFromEnd(true);
        mgr.setReverseLayout(true);

        searchView.setLayoutManager(mgr);

        searchView.setAdapter(busAdt);

        searchBusBtn.setOnClickListener(clk ->{
            BusMessage thisMessage = new BusMessage(busMsg.getText().toString(),1, new Date());
            searchMessage.add(thisMessage);

            busAdt.notifyItemInserted(searchMessage.size()-1);
           // searchView.smoothScrollToPosition(0);
            SharedPreferences.Editor editor = prefs.edit();
            EditText busMsgText = findViewById(R.id.searchBox);
            editor.putString("BusNumber", busMsgText.getText().toString());
            busMsg.setText("");
            editor.apply();

        });

    }

    private class RowViews extends RecyclerView.ViewHolder {

        TextView busInfo;
        TextView timeInfo;
        int position = -1;

        public RowViews(View itemView) {
            super(itemView);

            busInfo = itemView.findViewById(R.id.busInfo);
            timeInfo = itemView.findViewById(R.id.timeView);

            itemView.setOnClickListener(click ->{
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchBus.this);
                builder.setMessage("Do you want to delete this message: " + busInfo.getText())
                        .setTitle("Queestions:")
                        .setPositiveButton("Yes",(dialog, cl)->{
                            //position = getAbsoluteAdapterPosition(); always turn red
                            position = getAdapterPosition();
                            BusMessage removeMessage = searchMessage.get(position);
                            searchMessage.remove(position);
                            busAdt.notifyItemRemoved(position);
                            Snackbar.make(busInfo,"You deleted message #" + position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", clk ->{
                                        searchMessage.add(position,removeMessage);
                                        busAdt.notifyItemInserted(position);
                                    })
                                    .show();
                        })
                        .setNegativeButton("No",(dialog, cl) ->{

                        })
                        .create().show();
            });
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }



    private class MyBusAdapter extends RecyclerView.Adapter{

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = getLayoutInflater().inflate(R.layout.search_view, parent,false);//row in a recyclerview, bus
        return new RowViews(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RowViews busRowLayout = (RowViews) holder;
        busRowLayout.busInfo.setText(searchMessage.get(position).getMessage());
        busRowLayout.timeInfo.setText(sdf.format(searchMessage.get(position).getTimeSearch()));
        busRowLayout.setPosition(position);
    }

        @Override
        public int getItemCount() {
            return searchMessage.size();
        }
    }
    
    
    private class BusMessage{
        String message;
        int busMesg;
        Date timeSearch;

        public BusMessage(String message, int busMsg, Date timeSearch){
            this.message = message;
            this.busMesg = busMsg;
            this.timeSearch = timeSearch;
        }

        public String getMessage(){
            return  message;
        }
        public int getBusMesg(){
            return busMesg;
        }

        public Date getTimeSearch() {
            return timeSearch;
        }
    }
}