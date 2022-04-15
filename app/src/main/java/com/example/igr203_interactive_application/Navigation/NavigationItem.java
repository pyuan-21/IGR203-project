package com.example.igr203_interactive_application.Navigation;

public class NavigationItem {

    private String Title_item;
    private  int icon_item;


    public NavigationItem(String title_item, int icon_item) {
        Title_item = title_item;
        this.icon_item = icon_item;
    }

    public String getTitle_item() {
        return Title_item;
    }

    public void setTitle_item(String title_item) {
        Title_item = title_item;
    }

    public int getIcon_item() {
        return icon_item;
    }

    public void setIcon_item(int icon_item) {
        this.icon_item = icon_item;
    }
}
