package ru.daniil.notebook.service;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Scanner;

public class FileUtils {
    public static Path newPath(Scanner scan){
        Path path;
        while(true){
            System.out.print("Введите адрес новой книжки (или 'отмена'): ");
            String address = scan.nextLine().trim(); //ввод адреса

            if (address.isEmpty() || address.equalsIgnoreCase("отмена")) {
              //проверка на отмену операции
                System.out.println("Процесс отменен.");
                return null;
            }
            try {
                //инициализация пути и проверка правильности написания
                path = Path.of(address).toAbsolutePath();
            } catch (InvalidPathException e) {
                System.out.println("Неверный путь: " + e.getMessage()); //возврат в начало цикла при ошибке
                continue;
            }


            if (Files.exists(path)) {
                //проверка на то, что это файл
                if (!Files.isRegularFile(path)) {
                    System.out.println("Это не файл: " + path);
                    continue;
                }
                //проверка доступа на чтение и запись
                if (!Files.isReadable(path) || !Files.isWritable(path)) {
                    System.out.println("Файл существует, но нет прав на чтение/запись файла.");
                    continue;
                }
                //механизм на случай наличия файла по указанному пути
                System.out.print("Файл существует. Перезаписать? (y/n): ");
                String answer = scan.nextLine().trim().toLowerCase();
                if (!answer.equals("y")) {
                    continue;
                }
            }
//проверка наличия родительской папки и доступа на запись к ней
            Path parent = path.getParent();
            if (parent != null && !Files.isDirectory(parent)) {
                System.out.println("Папка не существует: " + parent);
                continue;
            }
            if (parent != null && !Files.isWritable(parent)) {
                System.out.println("Нет прав на запись в папку: " + parent);
                continue;
            }
            break;
        }
        return path;
    }
}
