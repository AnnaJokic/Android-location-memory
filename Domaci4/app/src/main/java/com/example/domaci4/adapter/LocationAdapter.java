package com.example.domaci4.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.domaci4.R;
import com.example.domaci4.model.LocationModel;
import com.example.domaci4.util.LocationDiffCallback;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationHolder> {

    private List<LocationModel> mDataSet;
    private OnLocationClickedListener mOnLocationClickedListener;

    public LocationAdapter() {
        mDataSet = new ArrayList<>();
    }

    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new LocationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationHolder holder, int position) {
        LocationModel locationModel = mDataSet.get(position);

        holder.mName.setText(locationModel.getName());
        holder.mDate.setText(locationModel.getDate());
        holder.mOpis.setText(locationModel.getOpis());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void setData(List<LocationModel> locationModelList){
        LocationDiffCallback callback = new LocationDiffCallback(mDataSet, locationModelList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        mDataSet.clear();
        mDataSet.addAll(locationModelList);
        result.dispatchUpdatesTo(this);
    }

    public void updateItem(LocationModel locationModel) {

        List<LocationModel> oldDataSet = new ArrayList<>(mDataSet);

        for (LocationModel e : mDataSet) {
            if (e.getId().equals(locationModel.getId())){
                e.setName(locationModel.getName());
            }
        }

        LocationDiffCallback callback = new LocationDiffCallback(oldDataSet, mDataSet);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        mDataSet.clear();
        mDataSet.addAll(oldDataSet);
        result.dispatchUpdatesTo(this);
    }

    public class LocationHolder extends RecyclerView.ViewHolder {

        TextView mName;
        TextView mDate;
        TextView mOpis;

        ImageView mMore;

        public LocationHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.lokacija);
            mDate = itemView.findViewById(R.id.datum);
            mOpis = itemView.findViewById(R.id.opis);
            mMore= itemView.findViewById(R.id.iv_list_item_edit);

            mMore.setOnClickListener(v -> {
                if (mOnLocationClickedListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        LocationModel locationModel = mDataSet.get(position);
                        mOnLocationClickedListener.onLocationMore(locationModel);
                    }
                }
            });

        }
    }

    public void setOnLocationClickedListener(OnLocationClickedListener listener) {
        mOnLocationClickedListener = listener;
    }

    public interface OnLocationClickedListener {
        void onLocationMore(LocationModel locationModel);
    }
}
