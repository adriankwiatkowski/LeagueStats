package com.example.android.leaguestats.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.leaguestats.data.network.api.ApiResponse;
import com.example.android.leaguestats.models.Resource;

public abstract class DataBoundResource<RequestType, ResultType> {

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    public DataBoundResource() {
        setValue(Resource.loading((ResultType) null));
        final LiveData<ApiResponse<RequestType>> apiResponse = createCall();
        result.addSource(apiResponse, new Observer<ApiResponse<RequestType>>() {
            @Override
            public void onChanged(@Nullable ApiResponse<RequestType> response) {
                result.removeSource(apiResponse);
                if (response != null && response.isSuccessful()) {
                    LiveData<Resource<ResultType>> liveResultType = parseResponse(response.body);
                    result.addSource(liveResultType, new Observer<Resource<ResultType>>() {
                        @Override
                        public void onChanged(@Nullable Resource<ResultType> resultType) {
                            result.setValue(resultType);
                            saveCallResult(resultType);
                        }
                    });
                } else {
                    onFetchFailed();
                    String errorMessage = "";
                    if (response != null) {
                        errorMessage = response.errorMessage;
                    }
                    setValue(Resource.error(errorMessage, (ResultType) null));
                }
            }
        });
    }

    @MainThread
    private void setValue(Resource<ResultType> newValue) {
        result.postValue(newValue);
    }

    public LiveData<Resource<ResultType>> asLiveData() {
        return result;
    }

    @MainThread
    protected void saveCallResult(@Nullable Resource<ResultType> result) {}

    @MainThread
    protected abstract LiveData<Resource<ResultType>> parseResponse(RequestType response);

    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> createCall();

    protected abstract void onFetchFailed();
}
