package com.hash.android.thejuapp.Model;

/**
 * Created by Spandita Ghosh on 7/22/2017.
 */

public class Magazine {
    public final String editionName;
    public final int coverPage;
    public final String editionDate;
    public final String downloadURL;


    public Magazine(String editionName, int coverPage, String editionDate, String downloadURL) {
        this.editionName = editionName;
        this.coverPage = coverPage;
        this.editionDate = editionDate;
        this.downloadURL = downloadURL;
    }

}
