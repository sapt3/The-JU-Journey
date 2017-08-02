package com.hash.android.thejuapp.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hash.android.thejuapp.DashboardActivity;
import com.hash.android.thejuapp.HelperClass.ClubUtils;
import com.hash.android.thejuapp.Model.Club;
import com.hash.android.thejuapp.R;


public class AboutClubFragmentFragment extends Fragment {

    String aboutJournal = "<p class=\"_3-8w\" style=\"box-sizing: border-box; margin: 0px 0px 1.25rem; padding: 0px; font-family: 'Open Sans', sans-serif; font-size: 16px; line-height: 1.8; text-rendering: optimizeLegibility; color: #252525;\">The JU Journal is a student-run media body that attempts to bring forth the various aspects of life on campus with an aim to highlight the excellence of the university.</p><h2 class=\"_50f4\" style=\"box-sizing: border-box; margin: 0px 0px 1rem; padding: 0px; font-family: 'Open Sans', sans-serif; font-weight: lighter; color: #353535; text-rendering: optimizeLegibility; line-height: 1.4; font-size: 2rem;\">Mission</h2><p class=\"_3-8w\" style=\"box-sizing: border-box; margin: 0px 0px 1.25rem; padding: 0px; font-family: 'Open Sans', sans-serif; font-size: 16px; line-height: 1.8; text-rendering: optimizeLegibility; color: #252525;\">The Journal strives to provide an unbiased, unified platform for the students of Jadavpur University. The aim of The Journal is to be a source of information, opinions and creative space for and by the students of the University.</p><h2 class=\"_3-8w\" style=\"box-sizing: border-box; margin: 0px 0px 1rem; padding: 0px; font-family: 'Open Sans', sans-serif; font-weight: lighter; color: #353535; text-rendering: optimizeLegibility; line-height: 1.4; font-size: 2rem;\">Our Story</h2><p style=\"box-sizing: border-box; margin: 0px 0px 1.25rem; padding: 0px; font-family: 'Open Sans', sans-serif; font-size: 16px; line-height: 1.8; text-rendering: optimizeLegibility; color: #252525;\">The Journal initially began to address the acute lack of information for the students where the clubs and societies were concerned. Moreover, there remained a communication gap between the alumni and the current students, bridging which could perhaps be a major source of guidance for the students. As an institution that has, despite its reputation, been the victim of poor sensationalised journalism; we as students truly believed in the need for an alternative, unbiased narrative of our Alma Mater.</p><p style=\"box-sizing: border-box; margin: 0px 0px 1.25rem; padding: 0px; font-family: 'Open Sans', sans-serif; font-size: 16px; line-height: 1.8; text-rendering: optimizeLegibility; color: #252525;\">The journey has been long and eventful, and over the years, we hope the Journal will be able to bring this vast, multifaceted University closer together.</p>";
    String aboutECell = "<h2 style=\"background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;\"><span style=\"font-family: 'Arial',sans-serif; color: #353535; font-weight: normal;\">Our Story</span></h2><p class=\"MsoNormal\" style=\"margin-bottom: 22.5pt; line-height: normal; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;\"><span style=\"color: #252525; font-family: Arial, sans-serif;\">An Entrepreneurship Development Cell has been created as a joint initiative of university students and the Business Incubation Centre, Jadavpur University, to nurture and inspire the creative minds of the students in the university by providing them the opportunities to showcase their ideas and helping them in turning their ideas into entrepreneurial ventures. The initiative to form an Entrepreneurship Development Cell started in 2016. The Cell was formed on 20th July, 2016 through a workshop held in Salt Lake Campus in presence of Prof. Suranjan Das, Vice Chancellor, Jadavpur University, Prof. Asis Mazumdar, Member Secretary, Business Incubation Centre (MSME), Jadavpur University, several other distinguished faculty members and officers of the university and other well-known personalities in the start-up community such as Mr. Aloke Mookherjea, Former Chairman, Confederation of Indian Industry (ER) and Mr. Ravi Ranjan, Sr. Associate, NASSCOM 10,000 Start-ups.&nbsp;</span>&nbsp;</p>";
    String aboutJUSC = "<h2 style=\"background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;\"><span style=\"font-family: 'Arial',sans-serif; color: #353535; font-weight: normal;\">Our Story</span></h2><p style=\"box-sizing: border-box; margin: 1.25rem; text-rendering: optimizeLegibility;\"><span style=\"font-family: 'Arial',sans-serif; color: #252525;\">Ever wondered about the complexity of the mechanism of a clock, or the myriads of the processors and circuits that makes up your PC?</span></p><p><span style=\"font-family: 'Arial',sans-serif; color: #252525;\">Ever looked up in exasperation from the text book, and thought how good it would be if the education system was a bit more practical?</span></p><p><span style=\"font-family: 'Arial',sans-serif; color: #252525;\">Jadavpur University Science Club, one of the most prominent and active student organizations, is all about people like you.</span></p><p><span style=\"font-family: 'Arial',sans-serif; color: #252525;\">As a non-profit student organization, we aim to promote science and technology among students. JUSC conducts various workshops, seminars, and lectures from professors and scientist&rsquo;s famous in their field of work throughout the year, so that scientific knowledge can be propagated. JUSC aims to involve students in practical applications of the theories text books don't bother to make you understand, as well as developing different skillsets that will be important for people with a scientific frame of mind in different field of research and development works.</span></p><p>&nbsp;</p><p><span style=\"font-family: 'Arial',sans-serif; color: #252525;\">Along with these, JUSC encourages the members to think, tinker, build, and experiment with science and technology as well as actively incubate scientific ideas and innovations by promoting them to a large extent through proper mentorship and financial aid.</span></p>";

    public AboutClubFragmentFragment() {

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
        String key = getArguments().getString("TAG");
        if (key != null) {
            Club club = new ClubUtils().findClub(key);
            getActivity().setTitle(club.clubName);
        }
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

        View rootView = inflater.inflate(R.layout.club_layout, container, false);
        ImageView clubImageView = rootView.findViewById(R.id.clubImageView);
        TextView about = rootView.findViewById(R.id.aboutClubTextView);
        TextView clubName = rootView.findViewById(R.id.clubNameTextView);
        TextView clubTag = rootView.findViewById(R.id.clubTagTextView);
        Button button = rootView.findViewById(R.id.back);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/opensans.ttf");

        about.setTypeface(typeface);
        about.setTextSize(18f);

        String key = getArguments().getString("TAG");
        if (key != null) {
            final Club club = new ClubUtils().findClub(key);
            Glide.with(this)
                        .load(club.clubImage)
                        .placeholder(R.drawable.placeholder)
                        .crossFade()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(clubImageView);


            clubName.setText(club.clubName);


            clubTag.setText(club.clubTag);

            switch (club.clubTag) {
                    case "@juecell":
                        about.setText(Html.fromHtml(aboutECell));
                        break;

                    case "@jujournal":
                        about.setText(Html.fromHtml(aboutJournal));
                        break;

                    case "@jusc":
                        about.setText(Html.fromHtml(aboutJUSC));
                        break;

                    default:

            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uriUrl = null;
                        uriUrl = Uri.parse(club.clubLink);
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                        startActivity(launchBrowser);

                }
            });
        } else {
            startActivity(new Intent(getActivity(), DashboardActivity.class));
            getActivity().finish();
        }


        return rootView;

    }
}
