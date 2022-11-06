package ch.nblotti.airtime.session.ui;

import static ch.nblotti.airtime.MessageEvent.EVENT_TYPE.JR_D_CLICK;
import static ch.nblotti.airtime.MessageEvent.EVENT_TYPE.JR_S_CLICK;
import static ch.nblotti.airtime.MessageEvent.EVENT_TYPE.JR_X_CLICK;
import static ch.nblotti.airtime.MessageEvent.EVENT_TYPE.R_C_CLICK;
import static ch.nblotti.airtime.MessageEvent.EVENT_TYPE.R_D_CLICK;
import static ch.nblotti.airtime.MessageEvent.EVENT_TYPE.R_M_CLICK;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ch.nblotti.airtime.MessageEvent;
import ch.nblotti.airtime.R;
import ch.nblotti.airtime.session.Session;

public class SessionListCustomAdapter extends RecyclerView.Adapter<SessionListCustomAdapter.ViewHolder> {

    private List<Session> mData = new ArrayList<>();
    private LayoutInflater mInflater;
    private SimpleDateFormat simpleDateFormat;
    private DecimalFormat doubleFormat;


    // data is passed into the constructor
    SessionListCustomAdapter(Context context, SimpleDateFormat simpleDateFormat, DecimalFormat doubleFormat) {
        this.mInflater = LayoutInflater.from(context);
        this.simpleDateFormat = simpleDateFormat;
        this.doubleFormat = doubleFormat;

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.session_text_row_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Session animal = mData.get(position);
        holder.session_id.setText(String.valueOf(animal.getUid()));
        holder.session_date.setText(simpleDateFormat.format(animal.getSessionDate()));


        holder.sessionRotationDoubleAverage.setText(String.valueOf(animal.getSessionAverage().intValue()));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setAnimalNames(List<Session> animals) {
        this.mData = animals;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView session_id;
        TextView session_date;
        TextView sessionJumpRopeSimpleAverage;
        TextView sessionJumpRopeDoubleAverage;
        TextView sessionJumpRopeCrossAverage;
        TextView sessionRotationMaxAverage;
        TextView sessionRotationControlledAverage;
        TextView sessionRotationDoubleAverage;

        ViewHolder(View itemView) {
            super(itemView);
            session_id = itemView.findViewById(R.id.session_id);
            session_date = itemView.findViewById(R.id.session_date);
            sessionJumpRopeSimpleAverage = itemView.findViewById(R.id.s_jr_s_average);
            sessionJumpRopeDoubleAverage = itemView.findViewById(R.id.s_jr_d_average);
            sessionJumpRopeCrossAverage = itemView.findViewById(R.id.s_jr_x_average);
            sessionRotationMaxAverage = itemView.findViewById(R.id.s_rm_average);
            sessionRotationControlledAverage = itemView.findViewById(R.id.s_cr_average);
            sessionRotationDoubleAverage = itemView.findViewById(R.id.s_da_average);

            sessionJumpRopeSimpleAverage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEvent(JR_S_CLICK, Integer.valueOf(session_id.getText().toString()));
                }
            });
            sessionJumpRopeDoubleAverage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEvent(JR_D_CLICK, Integer.valueOf(session_id.getText().toString()));
                }
            });
            sessionJumpRopeCrossAverage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEvent(JR_X_CLICK, Integer.valueOf(session_id.getText().toString()));
                }
            });
            sessionRotationMaxAverage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEvent(R_M_CLICK, Integer.valueOf(session_id.getText().toString()));
                }
            });
            sessionRotationControlledAverage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEvent(R_C_CLICK, Integer.valueOf(session_id.getText().toString()));
                }
            });
            sessionRotationDoubleAverage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEvent(R_D_CLICK, Integer.valueOf(session_id.getText().toString()));
                }
            });

        }

    }

    private void sendEvent(MessageEvent.EVENT_TYPE eventType, long session) {
        MessageEvent event = MessageEvent.build(eventType, session);
        EventBus.getDefault().post(event);
    }

    // convenience method for getting data at click position
    Session getItem(int id) {
        return mData.get(id);
    }


}
