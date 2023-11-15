package org.westernacher.solutions.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="sweatshirt")
public class Sweatshirt
{
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="img1")
    private String img1;

    @Column(name="img2")
    private String img2;

    @Column(name="new_price")
    private int newPrice;

    @Column(name="old_price")
    private int oldPrice;

    @Column(name="discount_percentage")
    private int discountPercentage;

    @Column(name="sizes")
    private String sizes;

    @Column(name="likes")
    private int likes;

    @Column(name="rating_sum")
    private int ratingSum;

    @Column(name="times_rated")
    private int timesRated;

    @Column(name="аvailability")
    private int аvailability;

    @Column(name="r_аvailability")
    private int rAvailability;

    @Column(name="description")
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "sweatshirt")
    private Set<Order> orders;

    @PreRemove
    private void removeSweatshirtFromOrderAndCart()
    {
        for (Order order : orders)
        {
            order.setSweatshirt(null);
        }

        for (SavedOrder savedOrder : cart)
        {
            savedOrder.setSweatshirt(null);
        }
    }

    @JsonIgnore
    @OneToMany(mappedBy = "sweatshirt")
    private List<SavedOrder> cart;

    public List<SavedOrder> getCart()
    {
        return cart;
    }

    public void setCart(List<SavedOrder> cart)
    {
        this.cart = cart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getImg1()
    {
        return img1;
    }

    public void setImg1(String img1)
    {
        this.img1 = img1;
    }

    public String getImg2()
    {
        return img2;
    }

    public void setImg2(String img2)
    {
        this.img2 = img2;
    }

    public int getNewPrice()
    {
        return newPrice;
    }

    public void setNewPrice(int newPrice)
    {
        this.newPrice = newPrice;
    }

    public int getOldPrice()
    {
        return oldPrice;
    }

    public void setOldPrice(int oldPrice)
    {
        this.oldPrice = oldPrice;
    }

    public int getDiscountPercentage()
    {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage)
    {
        this.discountPercentage = discountPercentage;
    }

    public int getLikes()
    {
        return likes;
    }

    public void setLikes(int likes)
    {
        this.likes = likes;
    }

    public String getSizes()
    {
        return sizes;
    }

    public void setSizes(String sizes)
    {
        this.sizes = sizes;
    }

    public int getRatingSum()
    {
        return ratingSum;
    }

    public void setRatingSum(int ratingSum)
    {
        this.ratingSum = ratingSum;
    }

    public int getTimesRated()
    {
        return timesRated;
    }

    public void setTimesRated(int timesRated)
    {
        this.timesRated = timesRated;
    }

    public int getАvailability()
    {
        return аvailability;
    }

    public void setАvailability(int аvailability)
    {
        this.аvailability = аvailability;
    }

    public int getrAvailability()
    {
        return rAvailability;
    }

    public void setrAvailability(int rAvailability)
    {
        this.rAvailability = rAvailability;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Set<Order> getOrders()
    {
        return orders;
    }

    public void setOrders(Set<Order> orders)
    {
        this.orders = orders;
    }



}
