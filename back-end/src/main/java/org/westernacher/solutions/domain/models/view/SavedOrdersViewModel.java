package org.westernacher.solutions.domain.models.view;

import org.westernacher.solutions.domain.entities.User;

public class SavedOrdersViewModel
{
    private String id;
    private String size;
    private int sweatshirtId;
    private String sweatshirtName;
    private String sweatshirtNewPrice;
    private String sweatshirtImg1;
    private int sweatshirtRAvailability;
    private String sweatshirtSizes;
    private User user;

    public String getSweatshirtSizes()
    {
        return sweatshirtSizes;
    }

    public void setSweatshirtSizes(String sweatshirtSizes)
    {
        this.sweatshirtSizes = sweatshirtSizes;
    }

    public String getSweatshirtImg1()
    {
        return sweatshirtImg1;
    }

    public void setSweatshirtImg1(String sweatshirtImg1)
    {
        this.sweatshirtImg1 = sweatshirtImg1;
    }

    public String getSweatshirtNewPrice()
    {
        return sweatshirtNewPrice;
    }

    public void setSweatshirtNewPrice(String sweatshirtNewPrice)
    {
        this.sweatshirtNewPrice = sweatshirtNewPrice;
    }

    public String getSize()
    {
        return size;
    }

    public void setSize(String size)
    {
        this.size = size;
    }

    public int getSweatshirtId()
    {
        return sweatshirtId;
    }

    public void setSweatshirtId(int sweatshirtId)
    {
        this.sweatshirtId = sweatshirtId;
    }

    public String getSweatshirtName()
    {
        return sweatshirtName;
    }

    public void setSweatshirtName(String sweatshirtName)
    {
        this.sweatshirtName = sweatshirtName;
    }

    public User getUser()
    {
        return user;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setUser(User user)
    {
        this.user = user;
    }


    public int getSweatshirtRAvailability()
    {
        return sweatshirtRAvailability;
    }

    public void setSweatshirtRAvailability(int sweatshirtRAvailability)
    {
        this.sweatshirtRAvailability = sweatshirtRAvailability;
    }
}
