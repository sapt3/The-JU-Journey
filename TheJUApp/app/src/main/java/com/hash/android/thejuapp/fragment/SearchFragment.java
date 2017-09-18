package com.hash.android.thejuapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;

import org.json.JSONException;
import org.json.JSONObject;


public class SearchFragment extends Fragment {

    private static final String TAG = SearchFragment.class.getSimpleName();

    public SearchFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Student Profile");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Client client = new Client("TXBD9WWLH0", "933b378c91e407426dd6bee014eff479");
        Index index = client.getIndex("users");
        index.enableSearchCache();
        index.searchAsync(new Query("Saptarshi"), new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject jsonObject, AlgoliaException e) {
                try {
                    Log.d(TAG, "requestCompleted: " + jsonObject.toString(2));
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
