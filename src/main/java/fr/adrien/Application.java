package fr.adrien;

import java.util.Scanner;

public class Application {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        displayGame();

        scanner.close();
    }

    public static void displayGame() {
        char[][] display = new char[10][10];
        char top = 'A';
        int left = 1;
        
        // aligne la ligne du haut avec le tableau (due au index de gauche (décalage de 1))
        System.out.print("    ");

        // affichage ligne du haut à part du tableau
        for (int i = 0; i < display.length; i++) {
            System.out.print(top + "  ");
            top = (char)(top + 1);
        }

        // saut le ligne pour séparer les index top du tableau
        System.out.println();

        // affichage intérieur du tableau
        for(int i = 0; i < display.length; i++) {
            System.out.printf("%2d  ", left); // le %2d me permet d'afficher un entier (d) sur une largeur de 2 caractères aligné à droite. pour éviter que le 10 ne décale tout le tableau
            left = left + 1;
            for(int j = 0; j < display[i].length; j++) {
                display[i][j] = '~';
                System.out.print(display[i][j] + "  ");
            }
            System.out.println();
        }
        
    }
}
