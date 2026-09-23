package ru.daniil.notebook.service;

import ru.daniil.notebook.model.BookRef;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class NoteStorage {
        private List<BookRef> noteAddress = new ArrayList<>(); //список книг
        private Path regPath; //путь к файлу регистра книг

        public NoteStorage() {
            this.regPath = Path.of(System.getProperty("user.home"), "notebook-registry.txt");
        } //конструктор для основного пользования

        public NoteStorage(Path registryPath) { //конструктор для тестов
            this.regPath = registryPath;
        }

        public void addAddress(Notebook notebook, String name){
          //добавление адреса книжки в реестр
            if (name == null || name.isBlank()) {
                //проверка на пустоту объекта книжки
                System.out.println("Имя не может быть пустым.");
                return;
            }
           if(existsByPath(notebook.getPath())){
               //проверка на наличие книги по пути
               System.out.println("Такая книжка уже есть.");
         return;
           }

            LocalDate lastOpened = LocalDate.now(); //установка текущего времени
            BookRef ref = new BookRef(notebook.getPath(), name, lastOpened); //создание объекта книжки
            noteAddress.add(ref); //добавление книжки в реестр
            this.save(); //сохранение реестра в файл
        }


    public void load()  {

            //Загрузчик реестра
        try {
            //проверка на существование реестра
        if (!Files.exists(regPath)) {
            //создание файла реестра, если его нет
            Files.write(regPath,  List.of());
            System.out.println("Загружено Записных книжек: 0" );
            return;
        }
            List<String> f_text = Files.readAllLines(regPath); //чтение файла реестра
            noteAddress.clear();//очистка списка книжек
            for(String s: f_text){
                if (s.isBlank()) continue;
                //разделение строки на параметры книжки
                String[] parts = s.split(";");
                if (parts.length != 3) {
                    //проверка целостности полученных данных о книжке
                    System.out.println("Пропущена некорректная строка: " + s);
                    continue;
                }
                String address = parts[0]; //считывание пути к книжке
                String name = parts[1]; //считывание пути имени книжки
                try {
                    LocalDate date = LocalDate.parse(parts[2]); //считывание даты последнего изменения
                    noteAddress.add(new BookRef(address, name, date)); //добавление книжки в реестр
                } catch (DateTimeParseException e) {
                    System.out.println("Некорректная дата в строке: " + s); //вывод сообщения об ошибке
                }

            }
            System.out.println("Загружено Записных книжек: " + noteAddress.size());
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }
    }

    public void save(){
            //сохранение реестра в файл
        try {
            List<String> f_text = new ArrayList<>();
            for(BookRef ref: noteAddress){
                String str =  ref.getPath()+ ";"+ ref.getName() + ";" + ref.getLastEdit(); //создание строки из данных объекта книжки
                f_text.add(str); //создание списка строк
            }
            Files.write(regPath, f_text);  //запись в файл

            System.out.println("Сохранено книжек: " + noteAddress.size());
        } catch (IOException e) {
            System.out.println("Ошибка записи файла: " + e.getMessage()); //вывод сообщения об ошибке
        }
    }

    public void printAll() {
            //вывод списка данных о книжках в реестре
        System.out.println("Всего книжек: " + noteAddress.size());

        if (noteAddress.isEmpty()) {
            //проверка на наличие книжек в реестре
            System.out.println("Реестр книжек пуста.");
            return;
        }

        int i = 1;
        for (BookRef c : noteAddress) {
            //вывод данных
            System.out.println(i + "." + c);
            i++;
        }
        System.out.println("===================");
    }

    public Path getBookPath(int i) { return Path.of(noteAddress.get(i - 1).getPath()); } //возврат пути указанной книги

    public String getBookName(int i) {
        return noteAddress.get(i - 1).getName();
    } //возврат имени указанной книги

    public LocalDate getBookLocalDate(int i) {
        return noteAddress.get(i - 1).getLastEdit();
    } //возврат даты изменения указанной книги

    public int getSize() {
        return noteAddress.size();
    } //возврат размерности регистра

    public boolean existsByPath(String path) {
       //метод определения существования книжки по ее пути
        for (BookRef ref : noteAddress) {
            if (ref.getPath().equalsIgnoreCase(path)) return true;
        }
        return false;
    }

   public BookRef findByIndex(int i){
 return noteAddress.get(i-1); //возврат книжки по ее индексу в реестре
   }

   public void touch(int noteNum){
       BookRef ref = this.findByIndex(noteNum); //поиск книжки по индексу
       ref.setLastEdit(LocalDate.now()); //изменение даты изменения
       this.save(); //сохранение реестра
   }

    public boolean duplicateNoteNameSearch(String name){
       //поиск дубликатов по имени книжки в реестре
        int count = 0;
        for (BookRef noteAdr : noteAddress) {
            if (noteAdr.getName().equalsIgnoreCase(name)) count++;
        }
        if(count > 0) {
            return true;
        }
        else {
            return false;
        }
    }

   public  String setNoteName(Scanner scan){
       //установка имени книжки в реестре
       String name;
       while (true) {
           //создание имени для книжки
           System.out.print("Введите имя записной книги (или \"отмена\" для отмены): ");
            name = scan.nextLine().trim();
           if (name.contains(";")) {
               //проверка на наличие разделителя
               System.out.println("Имя не может содержать ';'.");
               continue;
           }
           if (name.isBlank()) {
               //проверка на пустоту
               System.out.println("Имя не может быть пустым.");
               continue;
           }
           if (name.equalsIgnoreCase("отмена")) { //проверка на отмену операции
               return null;
           }
          if(this.duplicateNoteNameSearch(name)) {
              //проверка на дубликатность имени
              System.out.print("Такое имя уже есть.");
              continue;
          }
          break;
       }
       return name;
   }


   public void NoteRename(int i, String name){
      //переименовка книжки
       if (name == null || name.isBlank()) {
           System.out.println("Имя не задано, переименование отменено.");
           return;
       }
       BookRef ref = findByIndex(i); //поиск книжки по индексу
       ref.setName(name); //установка имени
       System.out.println("Имя книги изменено на \"" + name + "\"");
       System.out.println("==============");
       this.save(); //сохранение реестра
   }

   public void sortDate(){
            noteAddress.sort(Comparator.comparing(BookRef::getLastEdit).reversed()); //сортировка по времени изменения
       this.save(); //сохранение реестра
   }

   public void deleteNote(int i, Scanner scan) {
     //метод удаления книжки
       BookRef ref = findByIndex(i);//поиск книжки по индексу
       if(Files.exists(Path.of(ref.getPath()))){ //проверка наличие файла книжки
           System.out.println("Удалить файл книжки тоже? (y/n).");
           String answer = scan.nextLine().trim().toLowerCase();
           if (answer.equals("y")) {
               try{
                   boolean deleted = Files.deleteIfExists(Path.of(ref.getPath())); //удаление файла, если он существует
                   if (deleted) {
                       System.out.println("Файл книжки удалён: " + ref.getPath());
                   } else {
                       System.out.println("Файл не найден, удалена только запись из реестра.");
                   }
               } catch (Exception e) {
                   // не убираем из реестра, если файл не удалился
                   System.out.println("Не удалось удалить файл: " + e.getMessage());
                   System.out.println("Запись в реестре останется — попробуйте позже.");
                   return;
               }
               System.out.println("Файл книжки удален.");
           } else{
               System.out.println("Файл оставлен на диске, удалена только запись из реестра.");
           }
       }
       noteAddress.remove(i-1); //удаление из реестра
       this.save(); //сохранение реестра
       System.out.println("Книжка удалена из реестра.");
   }
}
