package org.westernacher.solutions.domain.models.binding;

public class SweatshirtAdd
{
    private String name;
    private String img;
    private int newPrice;
    private int oldPrice;
    private String sizes;
    private int аvailability;
    private int rAvailability;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(int newPrice) {
        this.newPrice = newPrice;
    }

    public int getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(int oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getSizes() {
        return sizes;
    }

    public void setSizes(String sizes) {
        this.sizes = sizes;
    }

    public int getАvailability() {
        return аvailability;
    }

    public void setАvailability(int аvailability) {
        this.аvailability = аvailability;
    }

    public int getrAvailability() {
        return rAvailability;
    }

    public void setrAvailability(int rAvailability) {
        this.rAvailability = rAvailability;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
