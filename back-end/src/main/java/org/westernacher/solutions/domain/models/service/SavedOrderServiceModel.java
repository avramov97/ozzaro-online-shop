package org.westernacher.solutions.domain.models.service;

import org.westernacher.solutions.domain.entities.Sweatshirt;
import org.westernacher.solutions.domain.entities.User;

public class SavedOrderServiceModel
{
    private String id;
    private String size;
    private Sweatshirt sweatshirt;
    private User user;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Sweatshirt getSweatshirt()
    {
        return sweatshirt;
    }

    public void setSweatshirt(Sweatshirt sweatshirt)
    {
        this.sweatshirt = sweatshirt;
    }

    public String getSize()
    {
        return size;
    }

    public void setSize(String size)
    {
        this.size = size;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}
