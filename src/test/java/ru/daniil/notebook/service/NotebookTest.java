package ru.daniil.notebook.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.daniil.notebook.model.Contact;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class NotebookTest {

    private Path tempFile;
    private Notebook notebook;

    @BeforeEach
    void setUp() throws IOException {
        // Создаём свежий временный файл и чистый Notebook перед каждым тестом
        tempFile = Files.createTempFile("notebook-test", ".txt");
        notebook = new Notebook(tempFile);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Убираем файл после каждого теста
        Files.deleteIfExists(tempFile);
    }


    @Test
    void testAddContact() throws IOException {
        Scanner scanner = new Scanner("Иван\n+79991234567\n");

        notebook.addContact(scanner);

        Contact found = notebook.findByName("Иван");
        assertNotNull(found, "Контакт должен быть найден");
        assertEquals("+79991234567", found.getPhone(), "Телефон должен совпадать");
    }

    @Test
    void testAddContactEmptyName() throws IOException {
        Scanner scanner = new Scanner("\nИван\n+79991234567\n");

        notebook.addContact(scanner);

        Contact found = notebook.findByName("Иван");
        assertNull(found, "Контакт не должен быть найден");
        assertEquals(0, notebook.getAllContacts().size(), "Книжка должна быть пуста.");
    }

    @Test
    void testAddContactNameWithSemicolon() throws IOException {
        Scanner scanner = new Scanner("Иван;Петр\nИван\n+79991234567\n");

        notebook.addContact(scanner);

        Contact found = notebook.findByName("Иван");
        assertNotNull(found, "Контакт должен быть найден");
        assertEquals("Иван", found.getName(), "Имя должно совпадать.");
    }

    @Test
    void testAddContactEmptyPhone() throws IOException {
        //Arrange (подготовка)
        Scanner scanner = new Scanner("\nИван\n\n");

        //Act (действие)
        notebook.addContact(scanner);

        //Assert (проверка)
        Contact found = notebook.findByName("Иван");
        assertNull(found, "Контакт не должен быть найден");
        assertEquals(0, notebook.getAllContacts().size(), "Книжка должна быть пуста.");
    }

    @Test
    void testAddContactInvalidPhoneFormat() throws IOException {
        Scanner scanner = new Scanner("Иван\n79991234567\n+79991234567\n");

        notebook.addContact(scanner);

        Contact found = notebook.findByName("Иван");
        assertNotNull(found, "Контакт должен быть найден");
        assertEquals("+79991234567", found.getPhone(), "Телефон должен совпадать");
    }

    @Test
    void testFindByNameNotFound() throws IOException {
        Contact found = notebook.findByName("Пётр");
        assertNull(found, "Контакт не должен быть найден");
    }

    @Test
    void testFindByNameCaseInsensitive() throws IOException {
        Scanner scanner = new Scanner("Иван\n+79991234567\n");
        notebook.addContact(scanner);

        Contact found = notebook.findByName("иван");
        assertNotNull(found, "Поиск должен быть регистронезависимым");
    }

    @Test
void testEditContactName() throws IOException {
        Scanner scanner = new Scanner("Иван\n+79991234567\n");
        notebook.addContact(scanner);
       Scanner s = new Scanner("Дима");
        Contact c = notebook.findByName("Иван");
        notebook.editContactName(c, s);
        assertEquals("Дима", c.getName(), "Имя контакта должно быть \"Дима\"");
    }

    @Test
    void testEditContactPhone() throws IOException {
        Scanner scanner = new Scanner("Иван\n+79991234567\n");
        notebook.addContact(scanner);
        Scanner s = new Scanner("+78989876769");
        Contact c = notebook.findByName("Иван");
        notebook.editContactPhone(c, s);
        assertEquals("+78989876769", c.getPhone(), "Номер должен быть \"+78989876769\"");
    }

    @Test
    void testDeleteContact() throws IOException {
        Scanner scanner = new Scanner("Иван\n+79991234567\n");
        notebook.addContact(scanner);
        Contact c = notebook.findByName("Иван");
        notebook.deleteContact(c);
        Contact found = notebook.findByName("Иван");
        assertNull(found, "Контакт не должен быть найден.");
    }

    @Test
    void testAddDuplicateName() throws IOException  {

        Scanner scanner1 = new Scanner("Иван\n+79991234567\n");
        notebook.addContact(scanner1);

        //Пытаемся добавить контакт с тем же именем, но другим номером
        Scanner scanner2 = new Scanner("Иван\nПётр\n+79991111111\n");
        notebook.addContact(scanner2);

        //Первый "Иван" должен остаться, второй "Иван" переименован в "Пётр"
        assertNotNull(notebook.findByName("Иван"));
        assertNotNull(notebook.findByName("Пётр"));
        assertEquals(2, notebook.getAllContacts().size());
    }

@Test
    void testAddDuplicatePhone() throws IOException  {

        Scanner scanner1 = new Scanner("Иван\n+79991234567\n");
        notebook.addContact(scanner1);

        Scanner scanner2 = new Scanner("Пётр\n+79991234567\n+79991111111\n");
        notebook.addContact(scanner2);

        assertNotNull(notebook.findByName("Иван"));
        assertNotNull(notebook.findByName("Пётр"));
        assertEquals(2, notebook.getAllContacts().size());
        assertEquals("+79991111111", notebook.findByName("Пётр").getPhone(), "Номер должен быть \"+79991111111\"");
    }


    @Test
    void testAddDuplicateNameSearchCounts() throws IOException  {
        List<Contact> newContacts = new ArrayList<>();
        Contact ivan = new Contact("Иван", "+79991234567");
        Contact petr = new Contact("Петр", "+79991111111");
        newContacts.add(ivan);
        newContacts.add(petr);
        notebook.addAll(newContacts);
        assertEquals(true, notebook.duplicateNameSearch("Иван"), "Дубликат должен быть найден");
    }
}
