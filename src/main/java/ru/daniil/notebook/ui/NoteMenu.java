package ru.daniil.notebook.ui;

import ru.daniil.notebook.model.Contact;
import ru.daniil.notebook.service.FileUtils;
import ru.daniil.notebook.service.NoteStorage;
import ru.daniil.notebook.service.Notebook;

import java.nio.file.Path;
import java.util.Scanner;

public class NoteMenu {

    public static void handleActions(Contact c, Scanner scan, Notebook note, NoteStorage noteStorage, int noteNum){
        if (c != null) {
            //вывод меню
            System.out.println("Обнаружен контакт: " + c);
            System.out.println("Какие действия произвести? ");
            System.out.println("1.Поменять имя");
            System.out.println("2.Поменять номер");
            System.out.println("3.Удалить контакт");
            System.out.println("4.Отмена");
            if (scan.hasNextInt()) {
                int act = scan.nextInt();
                scan.nextLine();
                switch (act) {
                    case 1:
                        //изменение имени контакта
                        note.editContactName(c, scan);
                        if (note.isDirty()) {
                            //сохранение книжки и обновление даты изменения
                            note.save();
                            noteStorage.touch(noteNum);
                        }
                        break;
                    case 2:
                        //изменение номера телефона контакта
                        note.editContactPhone(c, scan);
                        if (note.isDirty()) {
                            //сохранение книжки и обновление даты изменения
                            note.save();
                            noteStorage.touch(noteNum);
                        }
                        break;
                    case 3:
                        //удаление контакта
                        note.deleteContact(c);
                        if (note.isDirty()) {
                            //сохранение книжки и обновление даты изменения
                            note.save();
                            noteStorage.touch(noteNum);
                        }
                        return;
                    default:
                        //отмена работы
                        System.out.println("Отмена работы с контактом..");
                        break;
                }
            } else {
                System.out.println("Неверный ввод.");
                scan.nextLine();
            }
        } else System.out.println("Совпадений нет.");

    }

    public void noteMenu(NoteStorage noteStorage, Scanner scan, StorageInfo storage){
        String bookName; //инициализация имени книжки
        Notebook note = null;//инициализация книжки
        int noteNum; //инициализация индекса книжки
        if(storage != null){
            //получение имени, индекса книжки и самой книжки
            bookName = storage.bookname();
            note = storage.note();
            noteNum = storage.noteNum();
        }else {
            //сообщения о возврате реализованы в интерфейсе Хранилища
            return;
        }
        note.load(); //подгрузка списка контактов
        boolean ex = false;
        int input;
        while (!ex) {
            //вывод меню действий с книжкой
            System.out.println("==Открыта записная книга \"" +  bookName + "\" ==");
            System.out.println("1.Добавить контакт");
            System.out.println("2.Найти контакт");
            System.out.println("3.Показать все");
            System.out.println("4.Копировать содержимое в новую книжку.");
            System.out.println("5.Выход");
            if (scan.hasNextInt()) {
                input = scan.nextInt();
                scan.nextLine();
                switch (input) {
                    case 1:
                        //добавление нового контакта
                        note.addContact(scan);
                        if (note.isDirty()) {
                            note.save();
                            noteStorage.touch(noteNum);
                        }
                        break;
                    case 2:
                        //поиск контакта по имени
                        System.out.println("==Поиск контакта по имени==");
                        System.out.print("Введите имя контакта: ");
                        String name = scan.nextLine().trim(); //ввод имени
                        Contact c = note.findByName(name); //поиск
                        handleActions (c, scan, note, noteStorage, noteNum);//действия с контактом
                        System.out.println("\n====================");
                        break;
                    case 3:
                        //вывод всех контактов
                        note.printAll();
                        System.out.println("\n====================");
                        break;
                    case 4:
                        //копирование в новую книжку
                        System.out.println("==Копирование в новую книжку==");
                        Path address = FileUtils.newPath(scan); //получение пути новой книжки
                        if (address != null) {
                            //копирование
                            if (note.copyNote(address, noteStorage, scan)) {
                                System.out.println("==Копирование завершено.==");
                            }else System.out.println("==Копирование не выполнено.==");
                        }
                        System.out.println("\n====================");
                        break;
                    case 5:
                        ex = true;
                        System.out.println("До свидания!");
                        break;
                    default:
                        System.out.println("Неверный пункт меню.");
                }

            } else {
                //сообщение в случае неверного ввода
                System.out.println("Введите число от 1 до 5.");
                scan.nextLine();
            }
        }
    }
}
