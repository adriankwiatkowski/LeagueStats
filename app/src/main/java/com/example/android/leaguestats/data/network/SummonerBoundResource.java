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

public abstract class SummonerBoundResource<ResultType, RequestType> {

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    public SummonerBoundResource() {
        setValue(Resource.loading((ResultType) null));
        if (shouldFetch()) {
            fetchFromNetwork();
        } else {
            setValue(Resource.error("To many requests. Please try again later.", (ResultType) null));
            onFetchFailed();
        }
    }

    private void fetchFromNetwork() {
        final LiveData<ApiResponse<ResultType>> apiResponse = createCall();
        result.addSource(apiResponse, new Observer<ApiResponse<ResultType>>() {
            @Override
            public void onChanged(@Nullable final ApiResponse<ResultType> response) {
                result.removeSource(apiResponse);
                if (response != null && response.isSuccessful()) {
                    boolean shouldLoad = shouldLoadFromDb(processResponse(response));
                    if (shouldLoad) {
                        final LiveData<RequestType> dbSource = loadFromDb(processResponse(response));
                        if (dbSource != null) {
                            result.addSource(dbSource, new Observer<RequestType>() {
                                @Override
                                public void onChanged(@Nullable RequestType requestType) {
                                    result.removeSource(dbSource);
                                    ResultType dbResultType = saveLoadFromDbResult(processResponse(response), requestType);
                                    ResultType resultType = saveResult(dbResultType);
                                    result.setValue(Resource.success(resultType));
                                }
                            });
                        } else {
                            setValue(Resource.error(response.errorMessage, (ResultType) null));
                        }
                    } else {
                        ResultType resultType = saveResult(processResponse(response));
                        setValue(Resource.success(resultType));
                    }
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
        result.setValue(newValue);
    }

    protected void onFetchFailed() {

    }

    public LiveData<Resource<ResultType>> asLiveData() {
        return result;
    }

    @WorkerThread
    protected ResultType processResponse(ApiResponse<ResultType> response) {
        return response.body;
    }

    @MainThread
    protected abstract boolean shouldFetch();

    @MainThread
    protected abstract boolean shouldLoadFromDb(@NonNull ResultType data);

    @Nullable
    @MainThread
    protected abstract LiveData<RequestType> loadFromDb(@NonNull ResultType data);

    @NonNull
    @MainThread
    protected abstract ResultType saveLoadFromDbResult(@NonNull ResultType data, @Nullable RequestType requestType);

    @NonNull
    @MainThread
    protected abstract ResultType saveResult(@NonNull ResultType data);

    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<ResultType>> createCall();
}
