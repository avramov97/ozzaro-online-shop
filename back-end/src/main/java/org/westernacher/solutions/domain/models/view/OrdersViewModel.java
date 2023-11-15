package org.westernacher.solutions.domain.models.view;

public class OrdersViewModel
{
    private int id;
    private String name;
    private String number;
    private String city;
    private String address;
    private String email;
    private String size;
    private String sweatshirtId;
    private String sweatshirtName;
    private String sweatshirtImg1;
    private boolean delivered;

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getSweatshirtImg1()
    {
        return sweatshirtImg1;
    }

    public void setSweatshirtImg1(String sweatshirtImg1)
    {
        this.sweatshirtImg1 = sweatshirtImg1;
    }

    public String getSweatshirtName()
    {
        return sweatshirtName;
    }

    public void setSweatshirtName(String sweatshirtName)
    {
        this.sweatshirtName = sweatshirtName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMail() {
        return email;
    }

    public void setMail(String mail) {
        this.email = mail;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public String getSweatshirtId()
    {
        return sweatshirtId;
    }

    public void setSweatshirtId(String sweatshirtId)
    {
        this.sweatshirtId = sweatshirtId;
    }
}
