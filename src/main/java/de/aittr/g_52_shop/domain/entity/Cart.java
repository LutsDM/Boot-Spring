package de.aittr.g_52_shop.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Связь один-к-одному со стороны той таблицы, в которой есть
    // колонка, которая ссылается на другую таблицу
    // Аннотация @JsonIgnore говорит Джексону о том, что когда он
    // создаёт JSON корзины, ему не нужно внутрь этого JSON включать
    // ещё и JSON покупателя как внутреннего объекта.
    // Иначе произойдёт зацикливание, и Джексон будет постоянно внутри
    // покупателя прописывать корзину, внутри которой опять будет покупатель,
    // внутри которого опять будет корзина, и так до бесконечности
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToMany
    @JoinTable(
            name = "cart_product",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    public Cart() {
    }

    public void addProduct(Product product) {
        if (product.isActive()) {
            products.add(product);
        }
    }

    public List<Product> getAllActiveProducts() {
        return products
                .stream()
                .filter(Product::isActive)
                .toList();
    }

    public void removeProductById(Long id) {
        Iterator<Product> iterator = products.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId().equals(id)) {
                iterator.remove();
                break;
            }
        }
    }

    public void clear() {
        products.clear();
    }

    public BigDecimal getActiveProductsTotalCost() {
        return products
                .stream()
                .filter(Product::isActive)
                .map(Product::getPrice)
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal(0));
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getActiveProductsAveragePrice() {
        long productCount = products
                .stream()
                .filter(Product::isActive)
                .count();

        if (productCount == 0) {
            return new BigDecimal(0);
        }

        return getActiveProductsTotalCost().divide(new BigDecimal(productCount), RoundingMode.CEILING);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(id, cart.id) && Objects.equals(customer, cart.customer) && Objects.equals(products, cart.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, products);
    }

    @Override
    public String toString() {
        return String.format("Корзина: ИД - %d.", id);
    }
}
