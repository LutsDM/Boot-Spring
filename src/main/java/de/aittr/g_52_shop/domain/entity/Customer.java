package de.aittr.g_52_shop.domain.entity;

import jakarta.persistence.*;

import java.util.Objects;
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column (name = "active")
    private boolean active;

    public Customer() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return active == customer.active && Objects.equals(id, customer.id) && Objects.equals(name, customer.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, active);
    }

    @Override
    public String toString() {
        return String.format("Покупатель: ИД - %d, имя - %s, активен - %s.",
                id, name, active ? "да" : "нет");
    }
}