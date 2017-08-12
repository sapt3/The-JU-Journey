package com.hash.android.thejuapp.HelperClass;

import com.hash.android.thejuapp.Model.Club;

/**
 * Created by Spandita Ghosh on 8/4/2017.
 */

public class ClubUtils {

    public Club findClub(String tag) {
        switch (tag) {
            case "@jujournal":
                return new Club("The JU Journal", "https://www.facebook.com/thejujournal/?ref=br_rs", tag, "https://firebasestorage.googleapis.com/v0/b/the-ju-app.appspot.com/o/club_logo%2Fjournal%20logo%20(Small).jpg?alt=media&token=2c5eba1a-8f55-43ec-8912-4b5373f4bb67");

            case "@jusc":
                return new Club("JU Science Club", "https://www.facebook.com/ju.sc.club/", tag, "https://firebasestorage.googleapis.com/v0/b/the-ju-app.appspot.com/o/jusc.png?alt=media&token=98d717d1-4d56-4cd7-a621-7cde6f40d071");

            case "@juecell":
                return new Club("JU E-Cell", "https://www.facebook.com/juecell/", tag, "https://firebasestorage.googleapis.com/v0/b/the-ju-app.appspot.com/o/club_logo%2Fecell%20(Small).jpg?alt=media&token=296eecaf-0890-4e9c-b906-c864051624b7");

            default:
                return null;
        }
    }
}
