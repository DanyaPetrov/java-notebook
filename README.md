# Notebook

[![Java CI with Maven](https://github.com/DanyaPetrov/java-notebook/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/DanyaPetrov/java-notebook/actions/workflows/build.yml)

A console-based notebook application in Java that manages multiple contact books. Each book is stored separately on disk, while a central registry tracks them all.

## Features

- Create, rename, and delete multiple notebooks
- Store each notebook as a separate file
- Central registry with last-modified dates and sorting
- Full CRUD operations on contacts (add, find, edit, delete)
- Input validation: phone format, duplicate names/phones, empty input
- Copy an entire notebook to a new file
- Auto-save on every change (dirty flag tracking)
- Persistent storage via the local file system

## Tech Stack

- **Java 21**
- **Maven** for build automation
- **JUnit 5** for unit tests
- Standard library only — no external runtime dependencies

## Getting Started

### Requirements

- JDK 21 or higher
- Maven 3.8+

### Build

Clone the repository and run:

mvn clean package

### Run
java -jar target/notebook-1.0-SNAPSHOT.jar

Or use the helper scripts (Windows):

build.bat
run.bat


### Project Structure
src/
├── main/java/ru/daniil/notebook/
│   ├── model/       — domain entities (Contact, BookRef)
│   ├── service/     — business logic (Notebook, NoteStorage, FileUtils)
│   └── ui/          — console menus and user interaction
└── test/java/ru/daniil/notebook/
    └── service/     — unit tests for service layer

### Tests
mvn test
The tests cover operations related to working with the NoteBook file.

### Roadmap
□ Add JUnit tests for NoteStorage (registry)
□ Switch registry format to JSON for readability
□ Add contact groups / tags
□ Migrate CLI to a simple GUI (JavaFX or Swing)

### Author
Даниил — DanialP_Dev

### License
This project is licensed under the MIT License.
