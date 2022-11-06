package ch.nblotti.airtime.sample.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ch.nblotti.airtime.R;
import ch.nblotti.airtime.sample.Sample;

public class SampleListCustomAdapter extends RecyclerView.Adapter<SampleListCustomAdapter.ViewHolder> {

    private List<Sample> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    SampleListCustomAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.sample_text_row_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Sample animal = mData.get(position);
        holder.session_id.setText(String.valueOf(animal.sessionId));
        holder.sample_id.setText(String.valueOf(animal.getUid()));
        holder.measure.setText(String.valueOf(animal.sampleMeasure));
    }

    // total number of rows
    @Override
    public int getItemCount() {
       return  mData == null?0: mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView session_id;
        TextView sample_id;
        TextView measure;

        ViewHolder(View itemView) {
            super(itemView);
            session_id = itemView.findViewById(R.id.session_id);
            sample_id = itemView.findViewById(R.id.sample_id);
            measure = itemView.findViewById(R.id.sample_measure);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    public void setSamples(List<Sample> samples) {
        this.mData = samples;
    }

    // convenience method for getting data at click position
    Sample getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
