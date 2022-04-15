package com.example.igr203_interactive_application.Menu;

public class MenuItem {

    private String Title_item;
    private int id;
    private String price;
    private  int image_item;

    public MenuItem( int id,String title_item, String price, int image_item) {
        Title_item = title_item;
        this.id = id;
        this.price = price;
        this.image_item = image_item;
    }

    public String getTitle_item() {
        return Title_item;
    }

    public void setTitle_item(String title_item) {
        Title_item = title_item;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImage_item() {
        return image_item;
    }

    public void setImage_item(int image_item) {
        this.image_item = image_item;
    }
}
