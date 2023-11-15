package org.westernacher.solutions.domain.models.binding;

import org.westernacher.solutions.domain.entities.Sweatshirt;

public class SavedOrderBindingModel
{
    private String size;
    private Sweatshirt sweatshirt;
    private String username;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getSize()
    {
        return size;
    }

    public void setSize(String size)
    {
        this.size = size;
    }

    public Sweatshirt getSweatshirt()
    {
        return sweatshirt;
    }

    public void setSweatshirt(Sweatshirt sweatshirt)
    {
        this.sweatshirt = sweatshirt;
    }
}
