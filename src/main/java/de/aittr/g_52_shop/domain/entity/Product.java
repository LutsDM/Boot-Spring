package de.aittr.g_52_shop.domain.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

/*
Эта аннотация сообщает Спрингу о том, что перед нами энтити-сущность,
то есть такая сущность, для которой существует таблица в БД.
И надо объекты этого класса сопоставлять с БД.
 */
@Entity
/*
Эта аннотация сообщает Спрингу, в какой таблице в БД лежат продукты.
 */
@Table(name = "product")
public class Product {

    /*
    @Id - указываем, что именно это поле является идентификатором
    @GeneratedValue - указываем, что генерацией идентификаторов занимается сама БД
    @Column - указываем, в какой именно колонке таблицы лежат значения этого поля
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "active")
    private boolean active;

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
        Product product = (Product) o;
        return active == product.active && Objects.equals(id, product.id) && Objects.equals(title, product.title) && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, price, active);
    }

    @Override
    public String toString() {
        return String.format("Продукт: ИД - %d, наименование - %s, цена - %.2f, активен - %s.",
                id, title, price, active ? "да" : "нет");
    }
}
