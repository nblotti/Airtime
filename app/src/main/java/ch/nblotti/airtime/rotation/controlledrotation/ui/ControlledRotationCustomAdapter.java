package ch.nblotti.airtime.rotation.controlledrotation.ui;

import static ch.nblotti.airtime.MessageEvent.EVENT_TYPE.R_D_CLICK;
import static ch.nblotti.airtime.MessageEvent.EVENT_TYPE.UPDATE_CONTROLLED_ROTATION;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import ch.nblotti.airtime.MessageEvent;
import ch.nblotti.airtime.R;
import ch.nblotti.airtime.rotation.controlledrotation.ControlledRotation;
import ch.nblotti.airtime.sample.Sample;

public class ControlledRotationCustomAdapter extends RecyclerView.Adapter<ControlledRotationCustomAdapter.ViewHolder> {

    private List<ControlledRotation> mData;
    private LayoutInflater mInflater;
    // private ItemClickListener mClickListener;

    // data is passed into the constructor
    ControlledRotationCustomAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.controlled_rotation_text_row_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ControlledRotation animal = mData.get(position);
        holder.session_id.setText(String.valueOf(animal.getSessionId()));
        holder.sample_id.setText(String.valueOf(animal.getUid()));
        holder.measure.setText(String.valueOf(animal.getAirTime()));
        holder.statut.setText(String.valueOf(animal.getStatus().getStringValue()));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView session_id;
        TextView sample_id;
        TextView measure;
        TextView statut;

        ViewHolder(View itemView) {
            super(itemView);
            session_id = itemView.findViewById(R.id.session_id);
            sample_id = itemView.findViewById(R.id.sample_id);
            measure = itemView.findViewById(R.id.sample_measure);
            statut = itemView.findViewById(R.id.sample_result);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            sendEvent(UPDATE_CONTROLLED_ROTATION, Integer.valueOf(sample_id.getText().toString()));
        }

    }

    private void sendEvent(MessageEvent.EVENT_TYPE eventType, long session) {
        MessageEvent event = MessageEvent.build(eventType, session);
        EventBus.getDefault().post(event);
    }

    public void setSamples(List<ControlledRotation> samples) {
        this.mData = samples;
    }

    // convenience method for getting data at click position
    ControlledRotation getItem(int id) {
        return mData.get(id);
    }


}
