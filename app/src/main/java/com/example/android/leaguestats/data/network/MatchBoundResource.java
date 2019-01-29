package com.example.android.leaguestats.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.example.android.leaguestats.data.network.api.ApiResponse;
import com.example.android.leaguestats.models.Resource;

import java.util.ArrayList;
import java.util.List;

public abstract class MatchBoundResource<ResultType, RequestType> {

    private final MediatorLiveData<Resource<List<Resource<ResultType>>>> result = new MediatorLiveData<>();

    @MainThread
    public MatchBoundResource() {
        setValue(Resource.loading((List<Resource<ResultType>>) null));
        if (shouldFetch()) {
            fetchFromNetwork();
        } else {
            setValue(Resource.error("To many requests. Please try again later.", (List<Resource<ResultType>>) null));
            onFetchFailed();
        }
    }

    private void fetchFromNetwork() {
        final LiveData<ApiResponse<RequestType>> apiResponse = createCall();
        result.addSource(apiResponse, new Observer<ApiResponse<RequestType>>() {
            @Override
            public void onChanged(@Nullable ApiResponse<RequestType> listResponse) {
                result.removeSource(apiResponse);
                if (listResponse != null && listResponse.isSuccessful()) {
                    final List<LiveData<ApiResponse<ResultType>>> calls = createCalls(listResponse.body);
                    if (calls == null || calls.isEmpty()) {
                        throw new IllegalArgumentException("Calls cannot be null or empty.");
                    }
                    final List<Resource<ResultType>> resultList = new ArrayList<>();
                    for (int i = 0; i < calls.size(); i++) {
                        final LiveData<ApiResponse<ResultType>> call = calls.get(i);
                        final int finalI = i;
                        result.addSource(call, new Observer<ApiResponse<ResultType>>() {
                            @Override
                            public void onChanged(@Nullable ApiResponse<ResultType> resultResponse) {
                                result.removeSource(call);
                                if (resultResponse != null && resultResponse.isSuccessful()) {
                                    resultList.add(Resource.success(processResponse(resultResponse)));
                                } else {
                                    String errorMessage = "";
                                    if (resultResponse != null) {
                                        errorMessage = resultResponse.errorMessage;
                                    }
                                    resultList.add(Resource.error(errorMessage, (ResultType) null));
                                }
                                if (finalI >= calls.size() - 1) {
                                    setValue(Resource.success(resultList));
                                }
                            }
                        });
                    }
                } else {
                    onFetchFailed();
                    String errorMessage = "";
                    if (listResponse != null) {
                        errorMessage = listResponse.errorMessage;
                    }
                    setValue(Resource.error(errorMessage, (List<Resource<ResultType>>) null));
                }
            }
        });
    }

    @MainThread
    private void setValue(Resource<List<Resource<ResultType>>> newValue) {
        result.setValue(newValue);
    }

    protected abstract void onFetchFailed();

    public LiveData<Resource<List<Resource<ResultType>>>> asLiveData() {
        return result;
    }

    @WorkerThread
    protected ResultType processResponse(ApiResponse<ResultType> response) {
        return response.body;
    }

    @MainThread
    protected abstract boolean shouldFetch();

    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> createCall();

    @NonNull
    @MainThread
    protected abstract List<LiveData<ApiResponse<ResultType>>> createCalls(@NonNull RequestType requestType);
}
