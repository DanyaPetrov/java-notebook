package ru.daniil.notebook.model;

import java.time.LocalDate;

public class BookRef {
    private String path; //путь к книжке
    private String name; //имя книжки
    private LocalDate lastEdit; //дата последнего изменения

    public BookRef(String path, String name, LocalDate lastEdit) {
       //конструктор
        this.path = path;
        this.name = name;
        this.lastEdit = lastEdit;

    }

    public String getName() {
      //метод получения имени книжки
        return name;
    }

    public String getPath() {

        //метод получения пути к книжке

        return path;
    }

    public LocalDate getLastEdit() {
        //метод получения даты последнего изменения книжки

        return lastEdit;
    }

    public void setName(String name) {
        //метод установки имени книжки
        this.name = name;
    }

    public void setPath(String path) {
        //метод установки пути к книжке

        this.path = path;
    }

    public void setLastEdit(LocalDate lastEdit) {

        //метод установки даты последнего изменения книжки
        this.lastEdit = lastEdit;
    }

    @Override
    public String toString() {

        //переопределение метода для вывода информации

        return name + " (" + path + ", последнее изменение: " + lastEdit + ")";
    }

}

