package com.example.domaci4.util;

import androidx.recyclerview.widget.DiffUtil;
import com.example.domaci4.model.LocationModel;

import java.util.List;

public class LocationDiffCallback extends DiffUtil.Callback {

    private List<LocationModel> mOldList;
    private List<LocationModel> mNewList;

    public LocationDiffCallback(List<LocationModel> oldList, List<LocationModel> newList){
        mOldList = oldList;
        mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
//        LocationModel oldLocationModel = mOldList.get(oldItemPosition);
//        LocationModel newLocationModel = mNewList.get(newItemPosition);
//        return  oldLocationModel.getId().equals(newLocationModel.getId());

        return false;
    }


    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
//        LocationModel employee = mOldList.get(oldItemPosition);
//        LocationModel newMovie = mNewList.get(newItemPosition);
//        return employee.getName().equals(newMovie.getName());
        return false;
    }

}
