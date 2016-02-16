Sudoku Solver
=============

This is a <b>Java program that solves sudokus in a logical way</b>, like a human would - try to find a cell to fill or tackle out candidates for a cell. Not all sudoku solving methods are implemented though.

It <b>can also do the brute-force approach</b> (use computer power to try a lot of possibilities until it finds a possible completed grid), which is useful if it can't do it logically, or if you don't care about the process and just want the finished sudoku. 

## How to run the program
Download the .zip, extract all the files and run the .jar

	http://www.mediafire.com/download/lt6lsrjrk1apa0r/Sudoku_Solver.zip

## More information

### src
Contains the Java classes of the main program:

- <b>Afficher2</b> contains the main class, the view and the controller.
- <b>Main3</b> contains the model and solves the sudoku (contains the methods to solve the sudoku logically).
- <b>Utils</b> quickly checks if the sudoku has a solution or not and contains useful stuff.
- <b>Help</b> for help (oh really ?).

<b>Main2</b> is a light version of the program (see below).

### customGame.txt
Is a file used and modified during runtime, don't change it except if you know what you are doing and want to go full YOLO mode !

### Sudoku Database
Is a database containing ready-to-be-loaded sudokus if you just want to play sudoku and not solve a specific sudoku.

## Light version

<b>Main2</b> is a light version of the program which brute-forces the sudoku and simply shows the final solution. This class works alone, but you need to change the code to enter the starting numbers of the sudoku, also it prints the solution in console (Eclipse console for example).

You can go the the <b>branch Light-version</b> to find the Main2 class.