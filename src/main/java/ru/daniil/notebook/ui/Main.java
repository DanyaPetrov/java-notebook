package ru.daniil.notebook.ui;

import ru.daniil.notebook.service.NoteStorage;

import java.util.Scanner;


import static ru.daniil.notebook.ui.StorageMenu.StorageUI;

public class Main {


    public static void main(String[] args){
        Scanner scan = new Scanner(System.in); //инициализация инструмента ввода
        NoteStorage noteStorage = new NoteStorage(); //инициализация хранилища
       StorageUI(noteStorage, scan);//запуск интерфейса работы с Хранилищем
       scan.close();//закрытие инструмента ввода
    }
}