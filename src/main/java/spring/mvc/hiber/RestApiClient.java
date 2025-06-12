package spring.mvc.hiber;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import spring.mvc.hiber.model.User;

public class RestApiClient {

    public static void main(String[] args) {
        // Создаем экземпляр RestTemplate - основного инструмента для HTTP-запросов
        RestTemplate restTemplate = new RestTemplate();

        // Базовый URL API сервера
        String apiUrl = "http://94.198.50.185:7081/api/users";

        // StringBuilder для сбора итогового кода
        StringBuilder resultCode = new StringBuilder();

        // --- ШАГ 1: Получение списка пользователей ---
        // Выполняем GET-запрос для получения всех пользователей
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

        // Извлекаем session ID из заголовка Set-Cookie
        String sessionId = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

        // Выводим полученные данные для отладки
        System.out.println("1. GET Response: " + response.getBody());
        System.out.println("Session ID: " + sessionId);
        System.out.println("----------------------------");

        // --- ШАГ 2: Создание нового пользователя ---
        // Создаем заголовки HTTP-запроса
        HttpHeaders headers = new HttpHeaders();

        // Устанавливаем Cookie с полученным session ID
        headers.set(HttpHeaders.COOKIE, sessionId);

        // Указываем тип контента - JSON
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Создаем объект нового пользователя
        User newUser = new User(3L, "James", "Brown", (byte) 30);

        // Формируем HTTP-запрос: заголовки + тело (объект пользователя)
        HttpEntity<User> createRequest = new HttpEntity<>(newUser, headers);

        // Выполняем POST-запрос для создания пользователя
        String createResponse = restTemplate.postForObject(apiUrl, createRequest, String.class);

        // Выполняем GET-запрос для получения всех пользователей внутри сессии, с учётом добавленного юзера
        HttpEntity<String> getRequest = new HttpEntity<>(headers);
        ResponseEntity<String> response_2 = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                getRequest,
                String.class
        );
        // Добавляем полученный фрагмент кода к результату
        resultCode.append(createResponse);
        System.out.println("2. POST Response: " + createResponse);
        System.out.println("Тут должно быть 3 записи: " + response_2.getBody());
        System.out.println("----------------------------");

// --- ШАГ 3: Обновление пользователя ---
        User updatedUser = new User(3L, "Thomas", "Shelby", (byte) 30);
        HttpEntity<User> updateRequest = new HttpEntity<>(updatedUser, headers);

// Выполняем PUT-запрос и получаем ответ (используем exchange, т.к. PUT сам по себе ответов не возвращает!)
        ResponseEntity<String> updateResponse = restTemplate.exchange(
                apiUrl,
                HttpMethod.PUT,
                updateRequest,
                String.class
        );

// Проверяем обновленные данные
        HttpEntity<String> getRequest_2 = new HttpEntity<>(headers);
        ResponseEntity<String> response_3 = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                getRequest_2,
                String.class
        );

// Добавляем полученный фрагмент кода к результату
        resultCode.append(updateResponse.getBody());
        System.out.println("3. PUT Response: " + updateResponse.getBody());
        System.out.println("Тут третий должен быть Томас: " + response_3.getBody());
        System.out.println("----------------------------");

        // --- ШАГ 4: Удаление пользователя ---
        // Формируем HTTP-запрос только с заголовками (тело не требуется)
        HttpEntity<String> deleteRequest = new HttpEntity<>(headers);

        // Выполняем DELETE-запрос для удаления пользователя с id=3
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
                apiUrl + "/3",         // URL с указанием ID
                HttpMethod.DELETE,      // Метод DELETE
                deleteRequest,          // Заголовки запроса
                String.class            // Ожидаемый тип ответа
        );

        // Добавляем последний фрагмент кода
        resultCode.append(deleteResponse.getBody());
        System.out.println("4. DELETE Response: " + deleteResponse.getBody());
        System.out.println("----------------------------");

        // --- Финальный результат ---
        System.out.println("Итоговый код: " + resultCode);
        System.out.println("Длина кода: " + resultCode.length() + " символов");

        // Проверка длины кода (должно быть 18 символов)
        if (resultCode.length() == 18) {
            System.out.println("Поздравлямба! Код собран!");
        } else {
            System.out.println("ОШИБКА: Длина кода неверная!");
        }
    }
}