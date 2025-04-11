package ru.bsuedu.cad.app.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_")
public class Order extends AbstractEntity {
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public List<OrderProduct> getOrderProducts() { return orderProducts; }
    public void setOrderProducts(List<OrderProduct> orderProducts) { this.orderProducts = orderProducts; }
    public void addOrderProduct(OrderProduct orderProduct) { 
        orderProducts.add(orderProduct); 
        orderProduct.setOrder(this);
    }
}