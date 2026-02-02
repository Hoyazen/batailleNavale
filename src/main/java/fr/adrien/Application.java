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
        int top = 0;
        char left = 'A';
        
        // aligne la ligne du haut avec le tableau (due au index de gauche (décalage de 1))
        System.out.print("   ");

        // affichage ligne du haut à part du tableau
        for (int i = 0; i < display.length; i++) {
            top = top + 1;
            System.out.print(+ top + "  ");
        }

        // saut le ligne pour séparer les index top du tableau
        System.out.println();

        // affichage intérieur du tableau
        for(int i = 0; i < display.length; i++) {
            System.out.print((char)(left + i) + "  ");
            for(int j = 0; j < display[i].length; j++) {
                display[i][j] = '~';
                System.out.print(display[i][j] + "  ");
            }
            System.out.println();
        }
        
    }
}
