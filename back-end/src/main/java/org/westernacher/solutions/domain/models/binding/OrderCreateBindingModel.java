package org.westernacher.solutions.domain.models.binding;

import org.westernacher.solutions.domain.entities.Sweatshirt;

public class OrderCreateBindingModel
{
    private String name;
    private String number;
    private String city;
    private String address;
    private String email;
    private String size;
    private Sweatshirt sweatshirt;

    public String getSize()
    {
        return size;
    }

    public void setSize(String size)
    {
        this.size = size;
    }

    private boolean done;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getNumber()
    {
        return number;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public boolean isDone()
    {
        return done;
    }

    public void setDone(boolean done)
    {
        this.done = done;
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
