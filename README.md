# Bifrost compiler <img src="https://user-images.githubusercontent.com/38227712/162048400-b684988d-6d94-4dee-97c4-4530526d6295.png" width="80px" align="right">

## Description
This is a semester project, developed at AAU CPH by a team of 6 SW4 students.

## Building
1. Open the project in IntelliJ or Java IDE of choice. 
2. Build project with src/ASTVisitor/Main.java as the main class.

## Compiling
**Prerequisites**
- Install GCC
- Install libcurl
- Install cJSON
- Bifrost.jar (created through building the project)
- Lib folder (downloaded with the project)

---

1. Open a terminal in a directory containing ```Bifrost.jar```, some ```program.iot``` and the Lib directory.
2. Run ```java -jar Bifrost.jar program.iot``` + additional commandline arguments, e.g. ```--preserve```

This generates and runs an ```a.out``` or ```a.exe``` file, depending on your OS, and executes it.

---

Command line arguments
- --preserve (-p): preserves the C-file.
- --debug (-d): prints debug information such as symbol table.
- --ast (-a): draws the AST for the program.
- --out (-o) *file name*: changes the name of the output file.
