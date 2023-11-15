package org.westernacher.solutions.domain.entities;

import javax.persistence.*;

@Entity
@Table(name="orders")
public class Order
{
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="number")
    private String number;

    @Column(name="city")
    private String city;

    @Column(name="address")
    private String address;

    @Column(name="email")
    private String email;

    @Column(name="size")
    private String size;

    @Column(name="delivered")
    private boolean delivered;

    @ManyToOne
    @JoinColumn(name="sweatshirt_id")
    private Sweatshirt sweatshirt;

    public Sweatshirt getSweatshirt()
    {
        return sweatshirt;
    }

    public void setSweatshirt(Sweatshirt sweatshirt)
    {
        this.sweatshirt = sweatshirt;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public boolean isDelivered()
    {
        return delivered;
    }

    public void setDelivered(boolean delivered)
    {
        this.delivered = delivered;
    }

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

    public String getSize()
    {
        return size;
    }

    public void setSize(String size)
    {
        this.size = size;
    }
}
