package com.hash.android.thejuapp.Utils;

import com.hash.android.thejuapp.Model.Club;

/**
 * Created by Spandita Ghosh on 8/4/2017.
 */

public class ClubUtils {


    public final static String JOURNAL = "@jujournal";
    public final static String SC = "@jusc";
    public final static String ECELL = "@juecell";
    public final static String DS = "@juds";
    public final static String PNC = "@jupnc";
    public final static String MC = "@jumc";
    public final static String PC = "@jupc";
    public final static String QC = "@juqc";
    public final static String MHC = "@jumhc";
    public final static String DC = "@judc";
    public final static String DF = "@judf";


    public Club findClub(String tag) {
        if (tag == null) return null;
        else {
            switch (tag) {
                case JOURNAL:
                    return new Club("The JU Journal", "https://www.facebook.com/thejujournal/?ref=br_rs", tag, "https://firebasestorage.googleapis.com/v0/b/the-ju-app.appspot.com/o/club_logo%2Fjournal%20logo%20(Small).jpg?alt=media&token=2c5eba1a-8f55-43ec-8912-4b5373f4bb67");

                case SC:
                    return new Club("JU Science Club", "https://www.facebook.com/ju.sc.club/", tag, "https://firebasestorage.googleapis.com/v0/b/the-ju-app.appspot.com/o/jusc.png?alt=media&token=98d717d1-4d56-4cd7-a621-7cde6f40d071");

                case ECELL:
                    return new Club("JU E-Cell", "https://www.facebook.com/juecell/", tag, "https://firebasestorage.googleapis.com/v0/b/the-ju-app.appspot.com/o/club_logo%2Fecell%20(Small).jpg?alt=media&token=296eecaf-0890-4e9c-b906-c864051624b7");

                case DS:
                    return new Club("JU Debate Society", "https://www.facebook.com/JUDSKolkata/", tag, "https://firebasestorage.googleapis.com/v0/b/the-ju-app.appspot.com/o/club_logo%2Fjuds.jpg?alt=media&token=1b2bfa36-17f1-4c1e-b0dd-d1a52f16bcf0");

                case PNC:
                    return new Club("JU Painting Club", "https://www.facebook.com/Jadavpur-University-Painting-Club-108691606490442/", tag, "https://firebasestorage.googleapis.com/v0/b/the-ju-app.appspot.com/o/club_logo%2Fjupnc.jpg?alt=media&token=32e77995-be60-4410-b03b-e4ebb26db837");

                case MC:
                    return new Club("JU Music Club", "http://facebook.com/jumcdc", tag, "https://firebasestorage.googleapis.com/v0/b/the-ju-app.appspot.com/o/club_logo%2Fjumc.jpg?alt=media&token=46acdc96-66f8-434b-8f68-3af03b45b60f");

                case PC:
                    return new Club("JU Photographic Club", "https://m.facebook.com/profile.php?id=179937755435381&ref=content_filter", tag, "https://firebasestorage.googleapis.com/v0/b/the-ju-app.appspot.com/o/club_logo%2Fjupc%20(Custom).jpg?alt=media&token=a3b99dc4-bbe8-4b17-b00e-7af1406d0710");

                case QC:
                    return new Club("JU Quiz Club", "https://m.facebook.com/TheJUQuizClub/", tag, "https://firebasestorage.googleapis.com/v0/b/the-ju-app.appspot.com/o/club_logo%2Fjuqc%20(Custom).jpg?alt=media&token=6b95afe0-1d03-4960-b97d-d710eceed721");

                case MHC:
                    return new Club("JU MHC", "https://www.facebook.com/JUMHC1966/", tag, "https://firebasestorage.googleapis.com/v0/b/the-ju-app.appspot.com/o/club_logo%2Fjumhc%20(Custom).jpg?alt=media&token=623cf789-ae5a-45a5-9de7-7dff75a8fb4e");

                case DC:
                    return new Club("JU Drama Club", "https://www.facebook.com/judramaclub/", tag, "https://firebasestorage.googleapis.com/v0/b/the-ju-app.appspot.com/o/club_logo%2Fjudc%20(Custom).jpg?alt=media&token=8e73b9ac-f2ac-499e-9862-85f70a4cc8ae");

                case DF:
                    return new Club("JU Dance Forum", "https://www.facebook.com/JUDFKolkata", tag, "https://firebasestorage.googleapis.com/v0/b/the-ju-app.appspot.com/o/club_logo%2Fjudf%20(Custom).jpg?alt=media&token=d0d91996-07f8-4a6b-a636-9afc7f897517");

                default:
                    return null;
            }
        }
    }
}
