package de.aittr.g_52_shop.exception_handling.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
2 способ обработки исключений
Заключается в том, что на сам класс исключения мы вешаем аннотацию @ResponseStatus
и указываем, какой именно статус ответа должен получить клиент,
если у нас выброшен этот эксепшен

ПЛЮС -  очень легко и быстро создаём обработчик эксепшенов вообще без создания
        каких-либо методов. Отлично подходит для простейших случаев.
МИНУС - мы не можем отправить клиенту какое-то информативное сообщение, поэтому
        клиент в более сложных ситуациях может не понять причину ошибки, т.к.
        не видит её детального описания
 */
//@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super(String.format("Product with id %d not found", id));
    }
}
