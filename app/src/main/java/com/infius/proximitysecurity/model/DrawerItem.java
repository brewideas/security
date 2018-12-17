package com.infius.proximitysecurity.model;

/**
 * Created by kshemendra on 13/02/18.
 */

public class DrawerItem {

    String itemName;
    int icon;
    int action;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }
}
