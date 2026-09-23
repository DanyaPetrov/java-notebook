package ru.daniil.notebook.model;

public class Contact {
    private String name;//имя контакта
    private String phone;//номер телефона контакта

    public Contact(String name, String phone) {
        this.name = name; //установка имени
        this.phone = phone;//установка номера
    }

    public String getName() { return name; } //метод получения имени контакта
    public String getPhone() { return phone; }//метод получения номера контакта
    public void setName(String name) { this.name = name; }//метод установки имени контакта
    public void setPhone(String phone) { this.phone = phone; }//метод установки номера контакта

    @Override
    public String toString() {
        return name + " - " + phone;
    }         //переопределение метода для вывода информации

}