package de.aittr.g_52_shop.service.interfaces;

import de.aittr.g_52_shop.domain.dto.CustomerDto;
import de.aittr.g_52_shop.domain.entity.Customer;
import de.aittr.g_52_shop.domain.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public interface CustomerService {
    //  Сохранить покупателя в базе данных (при сохранении покупатель автоматически считается активным).
    CustomerDto save(CustomerDto customer);

    // Вернуть всех покупателей из базы данных (активных).
    List<CustomerDto> getAllActiveCustomers();

    // Вернуть одного покупателя из базы данных по его идентификатору (если он активен).
    CustomerDto getById(Long customerId);

    // Изменить одного покупателя в базе данных по его идентификатору.
    void update(CustomerDto customer);

    // Удалить покупателя из базы данных по его идентификатору.
    void deleteById(Long customerId);

    // Удалить покупателя из базы данных по его имени.
    void deleteByName(String name);

    // Восстановить удалённого покупателя в базе данных по его идентификатору.
    void restoreById(Long customerId);

    // Вернуть общее количество покупателей в базе данных (активных).
    long getAllActiveCustomerTotalCount();

    // Вернуть стоимость корзины покупателя по его идентификатору (если он активен).
    BigDecimal getTotalPriceInCartByProductId(Long productId);

    // Вернуть среднюю стоимость продукта в корзине покупателя по его идентификатору (если он активен)
    BigDecimal getAveragePriceInCartByProductId();

    // Добавить товар в корзину покупателя по их идентификаторам (если оба активны)
    Product saveProductByProductIdInCartByCustomerId(Long productId, Long customerId);

    // Удалить товар из корзины покупателя по их идентификаторам
    void deleteProductByProductIdFromCartByCustomerId(Long productId, Long customerId);

    // Полностью очистить корзину покупателя по его идентификатору (если он активен)*/
    void deleteAllFromCartByCustomerId(Long customerId);

}
