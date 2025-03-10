package de.aittr.g_52_shop.exception_handling;

import de.aittr.g_52_shop.exception_handling.exceptions.CustomerNotFoundException;
import de.aittr.g_52_shop.exception_handling.exceptions.CustomerValidationException;
import de.aittr.g_52_shop.exception_handling.exceptions.ProductNotFoundException;
import de.aittr.g_52_shop.exception_handling.exceptions.ProductValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/*
Аннотация @ControllerAdvice говорит о том, что перед нами - адвайс,
глобальный обработчик ошибок, которые возникают во всём проекте.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /*
    ResponseEntity - это специальный объект, внутрь которого мы можем
    заложить статус ответа, который получит наш клиент, а также
    любой объект, какой хотим, который будет отправлен клиенту.
    В данном случае помимо статуса мы в объект ResponseEntity закладываем
    ещё и объект своего Response, заложив в него сообщение об ошибке.
     */

        /*
        ПЛЮС -  мы создаём глобальный обработчик ошибок, который умеет ловить
                ошибки, возникающие во всем проекте и обрабатывать их в одном месте
        ПЛЮС -  логика обработки ошибок вынесена в отдельный класс, таким образом
                исходные методы содержат только чистую бизнес-логику, не
                нагруженную обработкой ошибок
        МИНУС - такой подход нам не подойдёт, если нам нужна разная логика обработки
                ошибок для разных контроллеров. В таком случае лучше воспользоваться
                первыми двумя способами
         */
        @ExceptionHandler(ProductNotFoundException.class)
        public ResponseEntity<Response> handleException(ProductNotFoundException e) {
                Response response = new Response(e.getMessage());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(ProductValidationException.class)
        public ResponseEntity<Response> handleException(ProductValidationException e) {
                Response response = new Response(e.getMessage());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(CustomerNotFoundException.class)
        public ResponseEntity<Response> handleException(CustomerNotFoundException e) {
                Response response = new Response(e.getMessage());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(CustomerValidationException.class)
        public ResponseEntity<Response> handleException(CustomerValidationException e) {
                Response response = new Response(e.getMessage());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
}
