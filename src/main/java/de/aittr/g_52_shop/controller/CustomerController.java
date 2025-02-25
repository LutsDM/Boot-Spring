package de.aittr.g_52_shop.controller;

import de.aittr.g_52_shop.domain.entity.Customer;
import de.aittr.g_52_shop.domain.entity.Product;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/customer")
public class CustomerController {
    // Сохранить покупателя в базе данных (при сохранении покупатель автоматически считается активным).
    @PostMapping
    public Customer save(@RequestBody Customer customer) {
        return null;
    }

    // Вернуть всех покупателей из базы данных (активных).
    @GetMapping("/all")
    public List<Customer> getAll() {
        return null;
    }

    // Вернуть одного покупателя из базы данных по его идентификатору (если он активен).
    @GetMapping("/{id}")
    public Customer getById(@PathVariable Long id) {
        return null;
    }

    //Изменить одного покупателя в базе данных по его идентификатору.
    @PutMapping
    public void update(@RequestBody Customer customer) {

    }

    //Удалить покупателя из базы данных по его идентификатору.
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {

    }

    // Удалить покупателя из базы данных по его имени.
    @DeleteMapping("/by-name/{name}")
    public void deleteByName(@PathVariable String name) {

    }

    //Восстановить удалённого покупателя в базе данных по его идентификатору.
    @PutMapping("/{id}/restore")
    public void restoreById(@PathVariable Long id) {

    }

    //Вернуть общее количество покупателей в базе данных (активных).
    @GetMapping("/count")
    public long getCustomerCount() {
        return 0;
    }

    //Вернуть стоимость корзины покупателя по его идентификатору (если он активен).
    @GetMapping("{id}/cart/total-cost")
    public BigDecimal getTotalCostById(@PathVariable Long id) {
        return BigDecimal.valueOf(0);
    }

    //Вернуть среднюю стоимость продукта в корзине покупателя по его идентификатору (если он активен)
    @GetMapping("{id}/cart/avg-cost")
    public BigDecimal getAvgCostById() {
        return BigDecimal.valueOf(0);
    }

    //Добавить товар в корзину покупателя по их идентификаторам (если оба активны)
    @PostMapping("{customerId}/cart/product/{productId}")
    public Product saveProductInCartByCustomerId(@PathVariable Long customerId, @PathVariable Long productId) {
        return null;
    }

    //Удалить товар из корзины покупателя по их идентификаторам
    @DeleteMapping("{id}/cart/product/{id}")
    public void deleteProductFromCartByIdCustomer(@PathVariable Long CustomerId, @PathVariable Long ProductId) {


    }

    //Полностью очистить корзину покупателя по его идентификатору (если он активен)*/
    @DeleteMapping("{id}/cart/product/all")
    public void deleteProductFromCartByIdCustomer(@PathVariable Long id) {

    }

}