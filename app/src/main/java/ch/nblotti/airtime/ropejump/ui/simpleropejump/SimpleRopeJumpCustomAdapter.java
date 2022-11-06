package ch.nblotti.airtime.ropejump.ui.simpleropejump;

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
import ch.nblotti.airtime.ropejump.RopeJump;
import ch.nblotti.airtime.rotation.controlledrotation.ControlledRotation;

public class SimpleRopeJumpCustomAdapter extends RecyclerView.Adapter<SimpleRopeJumpCustomAdapter.ViewHolder> {

    private List<RopeJump> mData;
    private LayoutInflater mInflater;
    // private ItemClickListener mClickListener;

    // data is passed into the constructor
    SimpleRopeJumpCustomAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.rope_jump_text_row_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RopeJump animal = mData.get(position);
        holder.session_id.setText(String.valueOf(animal.getSessionId()));
        holder.sample_id.setText(String.valueOf(animal.getUid()));
        holder.points.setText(String.valueOf(animal.getPoints()));
        holder.number.setText(String.valueOf(animal.getNumber()));
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
        TextView points;
        TextView measure;
        TextView statut;
        TextView number;

        ViewHolder(View itemView) {
            super(itemView);
            session_id = itemView.findViewById(R.id.session_id);
            sample_id = itemView.findViewById(R.id.sample_id);
            points = itemView.findViewById(R.id.points);
            measure = itemView.findViewById(R.id.sample_measure);
            statut = itemView.findViewById(R.id.sample_result);
            number = itemView.findViewById(R.id.number);
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

    public void setSamples(List<RopeJump> samples) {
        this.mData = samples;
    }

    // convenience method for getting data at click position
    RopeJump getItem(int id) {
        return mData.get(id);
    }


}
