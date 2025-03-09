package de.aittr.g_52_shop.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

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

    /*
    Мы хотим, чтобы название продукта соответствовало требованиям:
    1. Не должно быть короче трёх символов.
    2. Не должно содержать цифры и спец.символы.
    3. Первая буква должна быть в верхнем регистре.
    4. Остальные буквы должны быть в нижнем регистре.
     */
    @Column(name = "title")
    @NotNull(message = "Product title cannot be null")
    @NotBlank(message = "Product title cannot be empty")
    @Pattern(
            regexp = "[A-Z][a-z ]{2,}",
            message = "Product title should be at least three characters length and start with capital letter"
    )
    private String title;

    @Column(name = "price")
    @DecimalMin(
            value = "1.00",
            message = "Product price should be greater or equal than 1"
    )
    @DecimalMax(
            value = "1000.00",
            inclusive = false,
            message = "Product price should be lesser than 1000"
    )
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
