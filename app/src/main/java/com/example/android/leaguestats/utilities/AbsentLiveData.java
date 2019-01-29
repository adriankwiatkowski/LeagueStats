package com.example.android.leaguestats.utilities;

import android.arch.lifecycle.LiveData;

import com.example.android.leaguestats.models.Resource;

public class AbsentLiveData<T> extends LiveData<Resource<T>> {

    private AbsentLiveData(Resource<T> resource) {
        postValue(resource);
    }

    public static <T> LiveData<Resource<T>> create(Resource<T> resource) {
        return new AbsentLiveData(resource);
    }
}
