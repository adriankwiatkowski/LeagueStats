package com.example.android.leaguestats.utilities;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;

public class QueryHandler extends AsyncQueryHandler {

    private static final String LOG_TAG = QueryHandler.class.getSimpleName();

    public QueryHandler(ContentResolver cr) {
        super(cr);
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        super.onInsertComplete(token, cookie, uri);

        if (uri == null) {
            Log.d(LOG_TAG, "Failed insertion for token " + token);
        } else {
            Log.d(LOG_TAG, "Insertion successful for token " + token);
        }
    }
}

