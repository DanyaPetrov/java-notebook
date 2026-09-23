package ru.daniil.notebook.service;

import ru.daniil.notebook.model.Contact;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Notebook {
    //класс Записной книжки
    private List<Contact> contacts = new ArrayList<>(); //список контактов
    private  Path fPath; //путь к книжке
private  boolean dirty = false; //метка изменения
    // Конструктор по умолчанию - для реальной работы
    public Notebook() {
        this.fPath = Path.of(System.getProperty("user.home"), "notebook.txt");
    }

    // Конструктор с параметром - для тестов
    public Notebook(Path filePath) {
        this.fPath = filePath;
    }


    public void addContact(Scanner scan) {
        //метод добавления контакта
        System.out.println("==Добавление контакта==");
        String name; //инициализация имени
        while (true) {
            System.out.print("Введите имя контакта: ");
            name = scan.nextLine().trim(); //ввод имени

            if (name.isEmpty()) { //проверка на отмену
                System.out.println("Добавление отменено.");
                return;
            }
            if (name.contains(";")) { //проверка на наличие разделителя
                System.out.println("Имя не может содержать ';'.");
                continue;
            }
            if (duplicateNameSearch(name)) { //проверка наличия дубликата по имени
                System.out.println("Такое имя уже есть. Введите другое:");
                continue;
            }
            break;  //все проверки пройдены - выходим
        }

        String phone; //инициализация номера телефона
        while (true) {
            System.out.print("Введите номер контакта: ");
            phone = scan.nextLine();

            if (phone.isEmpty()) {
                //проверка на отмену операции
                System.out.println("Добавление отменено.");
                return;
            }
            if (duplicatePhoneSearch(phone)) {
                //проверка наличия дубликата по номеру
                System.out.println("Такой номер уже есть. Введите другой:");
                continue;
            }
            if (!phone.matches("\\+\\d{11}")) {
                //проверка соответствия формату +XXXXXXXXXXX
                System.out.println("Неверный номер. Формат: + и 11 цифр.");
                continue;
            }
            break;
        }

        contacts.add(new Contact(name, phone)); //добавление контакта в список
        System.out.println("Контакт \"" + name + "\" добавлен.");
       this.setDirty(); //установка метки изменения
    }

    public void addAll(List<Contact> newContacts) {
        //добавление списка контактов
        contacts.addAll(newContacts);
        this.setDirty(); //установка метки изменения
    }

        public boolean duplicateNameSearch(String name){
       //поиск дубликатов по имени
        int count = 0;
        for (Contact contact : contacts) {
            //перебор контактов и поиск соответствий
            if (contact.getName().equalsIgnoreCase(name)) count++;
        }
        if(count > 0) {
            return true;
        }
        else {
            return false;
        }

    }

    public boolean duplicatePhoneSearch(String phone){
        int count = 0;
        //поиск дубликатов по номеру телефона
        for (Contact contact : contacts) {
            //перебор контактов и поиск соответствий
            if (contact.getPhone().equalsIgnoreCase(phone)) count++;
        }
        if(count > 0) {//написано, потому что при добавлении в основную программу потребуется учесть, что данный код посчитает исходный контакт за дубликат
            return true;
        }
        else {
            return false;
        }
    }

    public String getPath() {
        return fPath.toString();
    }

    // TODO
    public boolean fullDuplicateSearch(Contact c){
        int count = 0;
        for (Contact contact : contacts) {
            if (contact.getName().equalsIgnoreCase(c.getName()) && contact.getPhone().equalsIgnoreCase(c.getPhone())) count++;
        }
        if(count > 1) { //написано, потому что при добавлении в основную программу потребуется учесть, что данный код посчитает исходный контакт за дубликат
            return true;
        }
        else {
            return false;
        }
    }

    public Contact findByName(String name) {
//поиск по имени
        for (Contact contact : contacts) {
            if (contact.getName().equalsIgnoreCase(name)) return contact;
        }
        return null;
    }

    // TODO
    public Contact findByFullInfo(String name, String phone) {
      //поиск по полному соответствию запросу
        if (name == null || name.isEmpty()) return null;
        if (phone == null || phone.isEmpty()) return null;
        for (Contact contact : contacts) {
            if (contact.getName().equalsIgnoreCase(name)
                    && contact.getPhone().equalsIgnoreCase(phone)) return contact;
        }
        return null;
    }

    public void editContactName(Contact c, Scanner scan) {
      //изменение имени контакта
        while (true) {
            System.out.print("Введите новое имя контакта: ");
            String name = scan.nextLine().trim(); //ввод имени

            if (name.isEmpty()) {
                //проверка на отмену
                System.out.println("Изменение отменено.");
                return;
            }
            if (name.contains(";")) {
                //проверка на наличие разделителя
                System.out.println("Имя не может содержать ';'.");
                continue;
            }
            if (name.equalsIgnoreCase(c.getName())) {
                //проверка на соответствие нового имени старому
                System.out.println("Это то же самое имя. Изменение не требуется.");
                return;
            }
            if (duplicateNameSearch(name)) {
                //проверка на дубликат
                System.out.println("Такое имя уже есть. Введите другое:");
                continue;
            }
            c.setName(name); //установка имени контакта
            System.out.println("Имя контакта изменено.");
            this.setDirty();   //установка метки изменения
            return;
        }
    }

    public void editContactPhone(Contact c, Scanner scan) {
        //изменение номера контакта

        String phone;
        while (true) {
            System.out.print("Введите новый номер контакта: ");
            phone = scan.nextLine();

            if (phone.isEmpty()) {
                //проверка на отмену
                System.out.println("Добавление отменено.");
                return;
            }
            if (phone.equalsIgnoreCase(c.getPhone())) {
                //проверка на соответствие нового номера старому
                System.out.println("Это тот же самый номер. Изменение не требуется.");
                return;
            }
            if (duplicatePhoneSearch(phone)) {
                //проверка на дубликат
                System.out.println("Такой номер уже есть. Введите другой:");
                continue;
            }
            if (!phone.matches("\\+\\d{11}")) {
                //проверка на соответствие формату
                System.out.println("Неверный номер. Формат: + и 11 цифр.");
                continue;
            }

            c.setPhone(phone);//установка номера контакта
            System.out.println("Номер контакта изменен.");
            this.setDirty();  //установка метки изменения
            break;
        }
    }

    public void deleteContact(Contact c) {
        contacts.remove(c);
        System.out.println("Контакт \"" + c.getName() + "\" удалён.");
        this.setDirty();
    }

    public void printAll() {
        System.out.println("=== Записная книжка ===");
        System.out.println("Всего контактов: " + contacts.size());

        if (contacts.isEmpty()) {
            System.out.println("Записная книжка пуста.");
            return;
        }

        int i = 1;
        for (Contact c : contacts) {
            System.out.println(i + "." + c);
            i++;
        }
    }

    public void cleanFile(){
        Path file = fPath;

        if (!Files.exists(file)) {
            return;
        }

        try {
            Files.write(file, List.of());
            System.out.println("Записная книжка очищена.");
        } catch (IOException e) {
            System.out.println("Ошибка очистки файла: " + e.getMessage());
        }
    }

    public void clearContacts() {
        contacts.clear();
        this.setDirty();
    }

    public void clearAll() {
        contacts.clear();
        try {
            Files.write(fPath, List.of());
        } catch (IOException e) {
            System.out.println("Ошибка очистки файла: " + e.getMessage());
        }
        this.setDirty();
    }

    public void load()  {

        //загрузка контактов
        Path file = fPath;
        try {
        if (!Files.exists(file)) {
            Files.write(file,  List.of());
            return;
        }
            List<String> f_text = Files.readAllLines(file);
            contacts.clear();
            for(String s: f_text){
                if (s.isBlank()) continue;
                String[] parts = s.split(";");
                if (parts.length != 2) {
                    System.out.println("Пропущена некорректная строка: " + s);
                    continue;
                }
                String name = parts[0];
                String phone = parts[1];
                Contact c = new Contact(name, phone);
                contacts.add(c);
            }
            System.out.println("Загружено контактов: " + contacts.size());
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }
    }

    public void save(){

        Path file = fPath;

        try {
            List<String> f_text = new ArrayList<>();
            for(Contact c: contacts){
                String str = c.getName() + ";" + c.getPhone();
                f_text.add(str);
            }
            Files.write(file, f_text);

            System.out.println("Сохранено контактов: " + contacts.size());
        } catch (IOException e) {
            System.out.println("Ошибка записи файла: " + e.getMessage());
        }
        this.clearDirty();
    }

    public boolean copyNote(Path p, NoteStorage noteStorage, Scanner scan){
        Notebook note = new Notebook(p);
        String noteName = noteStorage.setNoteName(scan);
        if(noteName == null){
            return false;
        }
        List<Contact> contacts2 =this.getAllContacts();
        note.addAll(contacts2);
           noteStorage.addAddress(note, noteName);
           note.save();
        return true;
    }

    public List<Contact> getAllContacts() {
        return new ArrayList<>(contacts);
    }

    public void setDirty(){
        this.dirty = true;
    }

    public void clearDirty(){
        this.dirty = false;
    }

    public boolean isDirty(){
        if(dirty) return true;
        else return false;
    }

}