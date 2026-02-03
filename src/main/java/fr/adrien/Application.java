package fr.adrien;

import java.util.Scanner;

public class Application {
    char[][] gridUser = new char[10][10];
    char[][] gridAdmin = new char[10][10];
    char gridWater = '~';

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Application app = new Application();
        app.initGrid();
        app.displayGrid();
        scanner.close();
    }

    // seulement l'initialisation de l'eau dans la grille
    public void initGrid() {
        for(int i = 0; i < gridUser.length; i++) {
            for(int j = 0; j < gridUser[i].length; j ++) {
                gridUser[i][j] = gridWater;
            }
        }
    }

    // seulement l'affichage de la grille
    public void displayGrid() {
        for(int i = 0; i < gridUser.length; i++) {
            for(int j = 0; j < gridUser[i].length; j ++) {
                System.out.print(gridUser[i][j] + " ");
            }
            System.out.println();
        }
    }
}
