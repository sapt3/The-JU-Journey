package com.hash.android.thejuapp.Model;

/**
 * Created by Spandita Ghosh on 7/11/2017.
 */

public class MenuItem {
    public final String itemName;
    public final String itemPrice;
    public boolean isItemSelected = false;

    public MenuItem(String itemName, String itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }
}
