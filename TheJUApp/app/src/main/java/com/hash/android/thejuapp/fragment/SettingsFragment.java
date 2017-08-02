package com.hash.android.thejuapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.hash.android.thejuapp.HelperClass.PreferenceManager;
import com.hash.android.thejuapp.R;

/**
 * Created by Spandita Ghosh on 7/27/2017.
 */

public class SettingsFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "SettingsFragment";
    Switch news, classUpdate, universityUpdate, departmentUpdate;
    private PreferenceManager mPrefs;

    public SettingsFragment() {

    }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * and before
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * <p>
     * <p>Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>Any restored child fragments will be created before the base
     * <code>Fragment.onCreate</code> method returns.</p>
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Settings");
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_fragment, container, false);
        mPrefs = new PreferenceManager(getActivity());

        news = rootView.findViewById(R.id.newsFeedNotification);
        classUpdate = rootView.findViewById(R.id.classNotification);
        departmentUpdate = rootView.findViewById(R.id.departmentNotification);
        universityUpdate = rootView.findViewById(R.id.universityNotification);

        updateUI();

        return rootView;
    }

    private void updateUI() {

        boolean notificationNews = mPrefs.getPrefsUserNotificationNews();
        boolean notificationClass = mPrefs.getPrefsUserNotificationClass();
        boolean notificationUniversity = mPrefs.getPrefsUserNotificationUniversity();
        boolean notficationDepartment = mPrefs.getPrefsUserNotificationDepartment();


        news.setChecked(notificationNews);
        classUpdate.setChecked(notificationClass);
        departmentUpdate.setChecked(notficationDepartment);
        universityUpdate.setChecked(notificationUniversity);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPrefs.setPrefsUserNotificationNews(news.isChecked());
        mPrefs.setPrefsUserNotificationClass(classUpdate.isChecked());
        mPrefs.setPrefsUserNotificationDepartment(departmentUpdate.isChecked());
        mPrefs.setPrefsUserNotificationUniversity(universityUpdate.isChecked());
        Log.d(TAG, "onPause:: Called");
    }
}
