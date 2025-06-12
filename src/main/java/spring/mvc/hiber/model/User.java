package spring.mvc.hiber.model;
public class User {
    private Long id;        // Уникальный идентификатор пользователя
    private String name;    // Имя пользователя
    private String lastName;// Фамилия пользователя
    private Byte age;       // Возраст пользователя (byte для экономии памяти)

    // Конструктор по умолчанию (обязателен для десериализации)
    public User() {
    }

    // Конструктор с параметрами для удобного создания объектов
    public User(Long id, String name, String lastName, Byte age) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
    }

    // --- Геттеры и сеттеры ---
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Byte getAge() {
        return age;
    }

    public void setAge(Byte age) {
        this.age = age;
    }
}