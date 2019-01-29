package com.example.android.leaguestats.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Filter;

import java.util.List;

abstract class ValueFilter<T> extends Filter {

    private List<T> mList;
    private List<T> mFilteredList;

    ValueFilter(@NonNull List<T> list) {
        mList = list;
    }

    @NonNull
    protected abstract List<T> filterResult(String query);

    protected abstract void publishResults(@Nullable List<T> filteredList);

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        String charString = constraint.toString();
        if (charString.isEmpty()) {
            mFilteredList = mList;
        } else {
            mFilteredList = filterResult(charString);
        }

        FilterResults filterResults = new FilterResults();
        filterResults.count = mFilteredList.size();
        filterResults.values = mFilteredList;
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        mFilteredList = (List<T>) results.values;
        publishResults(mFilteredList);
    }
}
