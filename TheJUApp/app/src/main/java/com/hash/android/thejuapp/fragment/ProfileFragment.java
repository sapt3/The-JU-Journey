package com.hash.android.thejuapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hash.android.thejuapp.HelperClass.CircleTransform;
import com.hash.android.thejuapp.HelperClass.PreferenceManager;
import com.hash.android.thejuapp.Model.User;
import com.hash.android.thejuapp.R;


public class ProfileFragment extends android.support.v4.app.Fragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();
    private boolean isEditing = true;
    private PreferenceManager mPrefsManager;

    public ProfileFragment() {
    }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * {@link #onAttach(Activity)} and before
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
        getActivity().setTitle("My Profile");
//        this.setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_profile, menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.nav_edit) {

//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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
//        return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        mPrefsManager = new PreferenceManager(getActivity());
        ImageView profileImageView = rootView.findViewById(R.id.profileImageViewProfile);
        final User user = new PreferenceManager(getActivity()).getUser();
        String profileImage = user.getPhotoURL();
        String name = user.getName();
        TextView nameTV = rootView.findViewById(R.id.nameTextViewProfile);
        nameTV.setText(name);
        Glide.with(this)
                .load(profileImage)
                .crossFade()
                .bitmapTransform(new CircleTransform(getActivity()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profileImageView);

        final TextView phoneTextView = rootView.findViewById(R.id.phoneNumberProfile);
        TextView departmentTextView = rootView.findViewById(R.id.departmentTextViewProfile);
        TextView yearOfJoiningTextView = rootView.findViewById(R.id.yearOfJoiningTextViewProfile);
        TextView emailTextView = rootView.findViewById(R.id.emailTextViewProfile);
        TextView facultyTextView = rootView.findViewById(R.id.facultyTextViewProfile);
        Button facebook = rootView.findViewById(R.id.facebookProfile);
        final ImageView privateButton = rootView.findViewById(R.id.privateAccessButton);

        final EditText statusEditText = rootView.findViewById(R.id.statusEditTextProfile);
        final ImageView editStatusButton = rootView.findViewById(R.id.editButtonStatus);
        final TextView privacyTV = rootView.findViewById(R.id.privacyTextView);
        final FrameLayout privacyMessage = rootView.findViewById(R.id.messagePrivacyFrameLayout);

        statusEditText.setEnabled(false); //Set to disabled at load

        if (mPrefsManager.getAbout().length() > 0) {
            //If about is not empty
            statusEditText.setText(mPrefsManager.getAbout());
        } else {
            statusEditText.setText("Hey there! A pleasure to meet you. :)");
        }

        editStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditing) {
                    editStatusButton.setImageResource(R.drawable.ic_check_pink_24px);
                    statusEditText.setEnabled(true);
                    isEditing = false;
                } else {
                    editStatusButton.setImageResource(R.drawable.ic_edit_white_24dp);
                    statusEditText.setEnabled(false);
                    String rawStatus = statusEditText.getText().toString().trim();
                    String formattedStatus = "\"" + rawStatus + "\""; //Enclose withing quotes
                    mPrefsManager.setAbout(formattedStatus);
                    mPrefsManager.saveUser();
                    isEditing = true;
                }
            }
        });


        if (user.getDepartment() != null) {
            departmentTextView.setText(user.getDepartment());
        } else {
            //TODO: Remove this
            departmentTextView.setText("Computer Science Engineering");
        }
        phoneTextView.setText(user.getPhoneNumber());
        facultyTextView.setText(user.getFaculty());
        yearOfJoiningTextView.setText(user.getYearOfPassing());
        emailTextView.setText(user.getEmail());
        privateButton.setImageResource((user.isPrivate()) ? R.drawable.ic_lock_outline_black_24dp : R.drawable.ic_lock_open_black_24dp);
        privacyMessage.setVisibility((user.isPrivate()) ? View.VISIBLE : View.INVISIBLE);
        privacyTV.setText((user.isPrivate()) ? "Your phone number is private" : "Your phone number is public");

        privateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.isPrivate()) {
                    privateButton.setImageResource(R.drawable.ic_lock_open_black_24dp);
                    user.setPrivate(false);
                    privacyTV.setText("Your phone number is public");
                    privacyMessage.setVisibility(View.INVISIBLE);
                    mPrefsManager.setPrivate(false);
                    mPrefsManager.saveUser();
                } else {
                    privateButton.setImageResource(R.drawable.ic_lock_outline_black_24dp);
                    user.setPrivate(true);
                    privacyTV.setText("Your phone number is private");
                    privacyMessage.setVisibility(View.VISIBLE);
                    mPrefsManager.setPrivate(true);
                    mPrefsManager.saveUser();
                }
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUrl = Uri.parse(user.getLink());
                //TODO: Convert to facebook uri
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });
//        mTextView.setError("Your phone number is private.");

        return rootView;
    }
}
