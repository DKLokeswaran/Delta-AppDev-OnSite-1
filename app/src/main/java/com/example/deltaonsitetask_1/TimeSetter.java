package com.example.deltaonsitetask_1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class TimeSetter extends AppCompatDialogFragment  {
    private TextView[] formattedTime;
    private ImageButton[] incrementers,decrementers;
    private long[] time;
    private Communicator communicator;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.time_input,null);
        time=new long[]{0L,0L,0L};
        formattedTime= new TextView[]{view.findViewById(R.id.Hour),view.findViewById(R.id.Min),view.findViewById(R.id.Sec)};
        incrementers=new ImageButton[]{view.findViewById(R.id.upHour),view.findViewById(R.id.upMin),view.findViewById(R.id.upSec)};
        decrementers=new ImageButton[]{view.findViewById(R.id.downHour),view.findViewById(R.id.downMin),view.findViewById(R.id.downSec)};
        updateTextView();
        for(int i=0;i<3;i++){
            int finalI = i;
            incrementers[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    time[finalI]++;
                    formatString(time);
                    updateTextView();
                }
            });
            decrementers[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    time[finalI]--;
                    if(isTimeNegative()){
                        time[finalI]++;
                    }
                    else{
                        formatString(time);
                        updateTextView();
                    }
                }
            });
        }
        builder.setView(view).setTitle("Select Duration").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                communicator.timeGetter(time);
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            communicator = (Communicator) context;
        } catch (Exception ignored) {

        }
    }

    public boolean isTimeNegative() {
        for(int i=0;i<3;i++){
            if(time[i]<0){
                return true;
            }
        }
        return false;
    }

    public void updateTextView() {
        for(int i=0;i<3;i++){
            formattedTime[i].setText(String.format("%02d", time[i]));
        }
    }

    public void formatString(long[] arr) {
        if(arr[2]>=60){
            arr[2]-=60;
            arr[1]++;
        }
        if(arr[1]>=60){
            arr[1]-=60;
            arr[0]++;
        }
    }

}
