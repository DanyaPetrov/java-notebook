package ru.daniil.notebook.ui;

import ru.daniil.notebook.service.NoteStorage;
import ru.daniil.notebook.service.Notebook;


import java.nio.file.Path;
import java.util.Scanner;

import static ru.daniil.notebook.service.FileUtils.newPath;


public class StorageMenu {

    public static StorageInfo NoteOpener(NoteStorage noteStorage, Scanner scan){
        String bookName; //имя книжки
        Notebook note = null; //объект книжки
        int noteNum; //индекс
        while (true){
            System.out.println("Введите номер книжки для открытия.");
            System.out.println("0. Назад");
            noteStorage.printAll(); //вывод списка книжек
            if (scan.hasNextInt()) {
                noteNum = scan.nextInt(); //получение индекса или 0
                if (noteNum == 0) {
                    //выход из программы
                    System.out.println("До свидания!");
                    return null;
                }
                if(noteNum <= noteStorage.getSize() && noteNum > 0){ //проверка ввода
                    scan.nextLine();
                    note = new Notebook(noteStorage.getBookPath(noteNum));
                    bookName = noteStorage.getBookName(noteNum);
                    break;
                } else {
                    System.out.println("Ввод вне числа записных книжек.");
                    scan.nextLine();
                }
            }else {
                System.out.println("Неверный ввод.");
                scan.nextLine();
            }
        }
//возврат объекта с информацией о выбранной книжке
        return new StorageInfo(bookName, note, noteNum);
    }


public static void noteEdit(NoteStorage noteStorage, Scanner scan)  {
    StorageInfo storage = null; //хранилище информации о книжке
    storage = NoteOpener(noteStorage, scan); //интерфейс выбора книжки
    if(storage == null) return;
    while(true) {
        //вывод меню редактирования
        System.out.println("Действия с записной книжкой \"" + storage.bookname() + "\"");
        System.out.println("1.Удалить книжку из реестра");
        System.out.println("2.Переименовать книжку.");
        System.out.println("3.Редактирование книжки.");
        System.out.println("4.Выход");
        if (scan.hasNextInt()) {
            int input = scan.nextInt();
            scan.nextLine();
            switch (input) {
                case 1:
                    //удаление книжки
                        noteStorage.deleteNote(storage.noteNum(), scan);
                return;
                case 2:
                    //переименовка книжки
String name = noteStorage.setNoteName(scan);
                    if (name != null) {
                        noteStorage.NoteRename(storage.noteNum(), name);
                    }
break;
                case 3:
                    //запуск меню редактирования книжки
                    NoteMenu menu = new NoteMenu();
                    menu.noteMenu(noteStorage, scan,  storage);
                    break;
                default:
                    System.out.println("До свидания!");
                    return;
            }
        }else {
            System.out.println("Введите число от 1 до 3.");
            scan.nextLine();
        }
    }
}

public static void newNote(NoteStorage noteStorage, Scanner scan){
        //создание новой книжки
    Path path = newPath(scan); //получение пути
    if (path == null) return;

    String name = noteStorage.setNoteName(scan); //получение имени
    if (name == null) return;

    Notebook note = new Notebook(path); //создание книжки
    noteStorage.addAddress(note, name); //добавление книжки в реестр
}

    public static void StorageUI(NoteStorage noteStorage, Scanner scan){
//основной интерфейс программы
        int input;
        noteStorage.load();//загрузка файла хранилища книжек
        while(true){
            //вывод меню
            System.out.println("Программа Записная книжка");
            System.out.println("1.Создать новую записную книжку.");
            System.out.println("2.Действия с книжкой.");
            System.out.println("3.Показать все записи.");
            System.out.println("4.Сортировать по дате последнего редактирования");
            System.out.println("5.Выход");
            if (scan.hasNextInt()) {
                input = scan.nextInt();
                scan.nextLine();
                switch (input) {
                    case 1:
                        //создание новой записной книжки
                        newNote(noteStorage, scan);
                        break;
                    case 2:
                        //манипуляции с книжкой и ее содержимым
                      noteEdit(noteStorage, scan);
                        break;
                    case 3:
                        //вывод всех книжек
                        noteStorage.printAll();
                        break;
                    case 4:
                        //сортировка по дате изменения
                        noteStorage.sortDate();
                        System.out.println("Записи отсортированы. Номера изменились.");
                        System.out.println("===================");
                        break;
                    default:
                        System.out.println("До свидания!");
                      return;
                }
            }else {
                //вывод сообщения в случае неверного ввода
                System.out.println("Введите число от 1 до 5.");
                scan.nextLine();
            }
        }
    }
}
