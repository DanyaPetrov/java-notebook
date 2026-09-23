package ru.daniil.notebook.ui;

import ru.daniil.notebook.service.Notebook;

    public record StorageInfo(String bookname, Notebook note, int noteNum){} //класс хранилища информации о книжке

