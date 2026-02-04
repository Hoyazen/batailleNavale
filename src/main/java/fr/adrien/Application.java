package fr.adrien;

import java.util.Random;
import java.util.Scanner;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

public class Application {

    // grille affichée au joueur (radar)
    char[][] gridUser = new char[10][10];

    // (optionnel debug) grille admin avec des 'B' pour voir les bateaux
    char[][] gridAdmin = new char[10][10];

    char gridWater = '~';

    // données cachées des bateaux
    int[][] shipId = new int[10][10]; // 0 = vide, 1..5 = id bateau
    boolean[][] hit = new boolean[10][10]; // true si case touchée

    Random random = new Random();

    // taille des bateaux
    int[] sizes = { 5, 4, 3, 3, 2 };

    public static void main(String[] args) {

        System.out.println(java.nio.charset.Charset.defaultCharset());

        AnsiConsole.systemInstall();

        Scanner scanner = new Scanner(System.in);
        Application app = new Application();

        app.initGrid();
        app.placeFleet(); // placement des bateaux

        while (true) {
            app.displayGrid();
            app.displayLegend();

            int[] pos = app.userLogic(scanner);

            app.fire(pos[0], pos[1]);
        }
    }

    // initialisation des grilles
    public void initGrid() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gridUser[i][j] = gridWater; // initialiser l'eau
                gridAdmin[i][j] = gridWater; //

                shipId[i][j] = 0; // pas de bateau
                hit[i][j] = false; // rien touché
            }
        }
    }

    // pour les couleurs
    public void printColored(char cell) {
        switch (cell) {
            case '~':
                System.out.print(Ansi.ansi().fg(Ansi.Color.BLUE).a("~ ").reset());
                break;
            case 'X':
                System.out.print(Ansi.ansi().fg(Ansi.Color.RED).a("X ").reset());
                break;
            case 'O':
                System.out.print(Ansi.ansi().fg(Ansi.Color.CYAN).a("O ").reset());
                break;
            case '#':
                System.out.print(Ansi.ansi().fg(Ansi.Color.YELLOW).a("# ").reset());
                break;
            default:
                System.out.print(cell + " ");
        }
    }

    // afficher la légende de couleurs pour le joueur
    public void displayLegend() {

        System.out.println();

        System.out.print("Légende : ");

        System.out.print(Ansi.ansi().fg(Ansi.Color.BLUE).a("~").reset());
        System.out.print(" = Eau   ");

        System.out.print(Ansi.ansi().fg(Ansi.Color.CYAN).a("O").reset());
        System.out.print(" = Tir manqué   ");

        System.out.print(Ansi.ansi().fg(Ansi.Color.RED).a("X").reset());
        System.out.print(" = Touché   ");

        System.out.print(Ansi.ansi().fg(Ansi.Color.YELLOW).a("#").reset());
        System.out.println(" = Coulé");

        System.out.println();
    }

    // affichage de la grille du joueur
    public void displayGrid() {
        char indexTop = 'A' - 1;
        int indexLeft = 1 - 1;

        System.out.print("   ");
        for (int i = 0; i < gridUser.length; i++) {
            indexTop++;
            System.out.print(indexTop + " ");
        }
        System.out.println();

        for (int i = 0; i < gridUser.length; i++) {
            indexLeft++;
            System.out.printf("%2d ", indexLeft);
            for (int j = 0; j < gridUser[i].length; j++) {
                printColored(gridUser[i][j]);
            }
            System.out.println();
        }
    }

    // grille admin pour debug (à enlever ensuite)
    public void displayAdminGrid() {
        char indexTop = 'A' - 1;
        int indexLeft = 1 - 1;

        System.out.print("   ");
        for (int i = 0; i < gridAdmin.length; i++) {
            indexTop++;
            System.out.print(indexTop + " ");
        }
        System.out.println();

        for (int i = 0; i < gridAdmin.length; i++) {
            indexLeft++;
            System.out.printf("%2d ", indexLeft);
            for (int j = 0; j < gridAdmin[i].length; j++) {
                System.out.print(gridAdmin[i][j] + " ");
            }
            System.out.println();
        }
    }

    // lecture et validation des coordonnées utilisateur
    public int[] userLogic(Scanner scanner) {

        int row;
        int col;

        while (true) {
            System.out.println("Il faut choisir une case");
            System.out.println("Exemple : C5");

            String userShoot = scanner.nextLine().trim().toUpperCase();

            // longueur
            if (userShoot.length() < 2 || userShoot.length() > 3) {
                System.out.println("Format invalide (ex: C5 ou J10).");
                continue;
            }

            char userColumn = userShoot.charAt(0);
            String userRowStr = userShoot.substring(1);

            // colonne A..J
            if (userColumn < 'A' || userColumn > 'J') {
                System.out.println("Colonne invalide (A à J).");
                continue;
            }

            int userRowInt;

            // ligne numérique
            try {
                userRowInt = Integer.parseInt(userRowStr);
            } catch (NumberFormatException e) {
                System.out.println("La ligne doit être un nombre (1 à 10).");
                continue;
            }

            // ligne 1..10
            if (userRowInt < 1 || userRowInt > 10) {
                System.out.println("Ligne invalide (1 à 10).");
                continue;
            }

            col = userColumn - 'A';
            row = userRowInt - 1;

            break;
        }

        return new int[] { row, col };
    }

    // placement de toute la flotte
    public void placeFleet() {
        for (int id = 1; id <= sizes.length; id++) {
            placeOneShip(id, sizes[id - 1]); // boucle sur la taille des bateaux
        }

        // vérifier que ça place bien
        // System.out.println("DEBUG - Grille admin (B = bateau) :");
        // displayAdminGrid();
    }

    // placement d’un bateau (id) de taille (size)
    public void placeOneShip(int id, int size) {

        while (true) {
            boolean horizontal = random.nextBoolean();
            int row = random.nextInt(10);
            int col = random.nextInt(10);

            // vérifier si ça rentre
            if (horizontal) {
                if (col + size > 10)
                    continue;
            } else {
                if (row + size > 10)
                    continue;
            }

            // vérifier chevauchement
            boolean ok = true;
            for (int k = 0; k < size; k++) {
                int r = horizontal ? row : row + k;
                int c = horizontal ? col + k : col;

                if (shipId[r][c] != 0) {
                    ok = false;
                    break;
                }
            }

            if (!ok)
                continue;

            // placer
            for (int k = 0; k < size; k++) {
                int r = horizontal ? row : row + k;
                int c = horizontal ? col + k : col;

                shipId[r][c] = id;

                // debug
                gridAdmin[r][c] = 'B';
            }

            break;
        }
    }

    // tirer sur une case
    public void fire(int row, int col) {

        // déjà tiré ?
        if (gridUser[row][col] != '~') {
            System.out.println("Vous avez déjà tiré ici !");
            return;
        }

        // à l’eau
        if (shipId[row][col] == 0) {
            gridUser[row][col] = 'O';
            System.out.println("À l’eau");
            return;
        }

        // touché
        gridUser[row][col] = 'X';
        hit[row][col] = true;
        int id = shipId[row][col];
        System.out.println("Touché");

        // coulé ?
        if (isSunk(id)) {
            markSunkOnRadar(id);
            System.out.println("Coulé");
        }
    }

    // vérifie si un bateau (id) est entièrement touché
    public boolean isSunk(int id) {
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                if (shipId[r][c] == id && !hit[r][c]) {
                    return false;
                }
            }
        }
        return true;
    }

    // remplace les X du bateau coulé par # sur le radar
    public void markSunkOnRadar(int id) {
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                if (shipId[r][c] == id) {
                    if (gridUser[r][c] == 'X') {
                        gridUser[r][c] = '#';
                    }
                }
            }
        }
    }
}
