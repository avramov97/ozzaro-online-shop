package org.westernacher.solutions.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "saved_order")
public class SavedOrder extends BaseEntity
{
    private String size;
    private Sweatshirt sweatshirt;
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getSize()
    {
        return size;
    }

    public void setSize(String size)
    {
        this.size = size;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="sweatshirt_id")
    public Sweatshirt getSweatshirt()
    {
        return sweatshirt;
    }

    public void setSweatshirt(Sweatshirt sweatshirt)
    {
        this.sweatshirt = sweatshirt;
    }
}
