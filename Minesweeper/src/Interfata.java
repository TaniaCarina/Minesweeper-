import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import java.awt.*;
public class Interfata extends JFrame {

    public static int spacing = 5;

    public static int vecini = 0;
    public static int mx = -100;
    public static int my = -100;
    public static boolean smileyface = true;
    public static boolean flag = false;
    public static boolean win = false;
    public static boolean isReset = false;  //verificam daca jocul e resetat sau nu
    public static boolean loser = false;  //inca nu am pierdut
    static Date startDate = new Date();
    public static int sec = 0;


    Random rand = new Random();

    static int[][] bombs = new int[15][10];
    static int[][] neighbours = new int[15][10];
    static boolean[][] revealed = new boolean[15][10];
    static boolean[][] rev = new boolean[15][10];
    static boolean[][] flagged = new boolean[15][10];

    public Interfata() {
        this.setTitle("Minesweeper");
        this.setSize(820, 629);  //86 si 29 sunt borderele
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);  //fereastra e vizibila pt user
        this.setResizable(false);  //nu putem schimba dimensiunea ferestrei

        for (int i = 1; i < 15; i++) {
            for (int j = 1; j < 10; j++) {
                if (rand.nextInt(100) < 20) {  //aprox 20% din patrate sa fie bombe
                    bombs[i][j] = 1;
                } else {
                    bombs[i][j] = 0;
                }
                revealed[i][j] = false;
                rev[i][j] = false;
            }
        }

        for (int i = 1; i < 15; i++) {
            for (int j = 1; j < 10; j++) {
                vecini = 0;
                for (int m = 1; m < 15; m++) {
                    for (int n = 1; n < 10; n++) {
                        if (!(n == j && i == m) && isN(i, j, m, n)) {
                            vecini++;
                        }
                    }
                    neighbours[i][j] = vecini;
                }
            }
        }

//        for (int i = 1; i < 15; i++) {
//            for (int j = 1; j < 10; j++) {
//                for (int m = 1; m < 15; m++) {
//                    for (int n = 1; n < 10; n++) {
//                        if (!(n == j && i == m) && notN(i, j, m, n)) {
//                            revealed[m][n] = true;
//                        }
//                    }
//                }
//            }
//        }

        Board board = new Board();
        this.setContentPane(board);

        Move move = new Move();
        this.addMouseMotionListener(move);

        Click click = new Click();
        this.addMouseListener(click);

    }

    public static class Board extends JPanel {
        public void paintComponent(Graphics g) {
            g.setColor(Color.darkGray);
            g.fillRect(0, 0, 805, 600);
            g.setColor(Color.lightGray);
            g.fill3DRect(50, 30, 705, 50, false);
            g.fillRect(50, 100, 705, 455);

            //flag button
            if (flag) {
                g.setColor(Color.gray);
            } else
                g.setColor(Color.white);
            g.fill3DRect(180, 35, 40, 40, false);
            g.setColor(Color.black);
            g.fill3DRect(190, 40, 3, 30, false);
            g.setColor(Color.red);
            g.fill3DRect(193, 40, 5, 22, false);
            g.fill3DRect(193, 42, 7, 18, false);
            g.fill3DRect(193, 44, 10, 14, false);
            g.fill3DRect(193, 46, 13, 10, false);
            g.fill3DRect(193, 48, 16, 6, false);
            g.fill3DRect(193, 49, 19, 3, false);

            //reset button
            g.setColor(Color.black);
            g.fill3DRect(55, 35, 113, 40, false);
            g.setColor(Color.white);
            g.setFont(new Font("Lucida Handwritting", Font.LAYOUT_LEFT_TO_RIGHT, 40));
            g.drawString("Reset", 57, 70);

            //timer
            g.setColor(Color.black);
            g.fill3DRect(670, 35, 80, 40, false);
            if (!loser && !win)
                sec = (int) ((new Date().getTime() - startDate.getTime()) / 1000);
            if (sec > 999)
                sec = 999;
            g.setColor(Color.white);
            if (win) {
                g.setColor(Color.green);
            } else if (loser) {
                g.setColor(Color.red);
            }
            g.setFont(new Font("Lucida Handwritting", Font.BOLD, 40));
            if (sec < 10)
                g.drawString("00" + Integer.toString(sec), 675, 70);
            else if (sec < 100)
                g.drawString("0" + Integer.toString(sec), 675, 70);
            else
                g.drawString(Integer.toString(sec), 675, 70);

            //smiley face
            g.setColor(Color.black);
            g.fillOval(375, 30, 50, 50);
            g.setColor(Color.yellow);
            g.fillOval(378, 32, 45, 45);
            g.setColor(Color.black);
            g.fillOval(390, 45, 5, 5);
            g.fillOval(405, 45, 5, 5);
            if (smileyface) {
                g.setColor(Color.black);
                g.fillOval(388, 58, 25, 12);
                g.setColor(Color.yellow);
                g.fillOval(388, 58, 25, 9);
            } else {
                g.setColor(Color.black);
                g.fillOval(388, 58, 25, 14);
                g.setColor(Color.yellow);
                g.fillOval(388, 60, 25, 18);
            }

            //butoane
            for (int i = 1; i < 15; i++) {
                for (int j = 1; j < 10; j++) {
                    g.setColor(Color.lightGray);
//                    if(bombs[i][j] == 1){ //facem bombele vizibile
//                        g.setColor(Color.yellow);
//                    }
                    if (revealed[i][j]) {
                        g.setColor(Color.white);
                        if(flagged[i][j])
                            g.setColor(Color.white);
                        if (bombs[i][j] == 1)
                            g.setColor(Color.red);
                    }

                    if (mx >= spacing * 2 + i * 50 &&
                            mx < spacing + i * 50 + 50 &&
                            my >= j * 50 + 50 + 36 &&
                            my < j * 50 + 50 + 36 + 50
                    ) {
                        g.setColor(Color.white);
                    }
                    g.fill3DRect(spacing + i * 50, spacing + j * 50 + 50, 50 - spacing, 50 - spacing, false);
                    if (revealed[i][j]) {
                        //discover(i, j);
                        g.setColor(Color.black);
                        if (bombs[i][j] == 0 && neighbours[i][j] != 0) {
                            if (neighbours[i][j] == 1)
                                g.setColor(Color.blue);
                            else if (neighbours[i][j] == 2)
                                g.setColor(new Color(0, 102, 0));
                            else if (neighbours[i][j] == 3)
                                g.setColor(Color.red);
                            else if (neighbours[i][j] == 4)
                                g.setColor(new Color(0, 0, 102));
                            else if (neighbours[i][j] == 5)
                                g.setColor(new Color(128, 0, 0));
                            else if (neighbours[i][j] == 6)
                                g.setColor(new Color(153, 0, 153));
                            else if (neighbours[i][j] == 7)
                                g.setColor(Color.black);
                            else if (neighbours[i][j] == 8)
                                g.setColor(Color.darkGray);

                            g.setFont(new Font("Lucida Handwritting", Font.BOLD, 40));
                            g.drawString(Integer.toString(neighbours[i][j]), i * 50 + 15, j * 50 + 92);
                        } else if (bombs[i][j] == 1) { //bomba
                            g.fillRect(+i * 50 + 17, +j * 50 + 57, 20, 41);
                            g.fillRect(+i * 50 + 8, +j * 50 + 68, 38, 20);
                            g.fillRect(+i * 50 + 12, +j * 50 + 62, 30, 32);
                            g.fillRect(i * 50 + 23, j * 50 + 55, 8, 45);
                            g.fillRect(i * 50 + 4, j * 50 + 73, 46, 9);
                            g.setColor(Color.white);
                            g.fillRect(i * 50 + 15, j * 50 + 65, 7, 7);
                        }
                    }
                    //flag painting
                    if(flagged[i][j]){
                        g.setColor(Color.black);
                        g.fillRect(+i * 50 + 17, +j * 50 + 62, 3, 30);//57
                        g.setColor(Color.red);
                        g.fillRect(+i * 50 + 20, +j * 50 + 62, 5, 22);
                        g.fillRect(+i * 50 + 20, +j * 50 + 64, 7, 18);
                        g.fillRect(+i * 50 + 20, +j * 50 + 66, 10, 14);
                        g.fillRect(+i * 50 + 20, +j * 50 + 68, 13, 10);
                        g.fillRect(+i * 50 + 20, +j * 50 + 70, 16, 6);
                        g.fillRect(+i * 50 + 20, +j * 50 + 71, 19, 3);

                    }
                }
            }
        }
    }

    public class Move implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mx = e.getX();
            my = e.getY();
        }
    }

    public class Click implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

            if (inBoxX() != -1 && inBoxY() != -1) {
                if (flag && !revealed[inBoxX()][inBoxY()]) {
                    if (!flagged[inBoxX()][inBoxY()]) {
                        flagged[inBoxX()][inBoxY()] = true;
                    } else {
                        flagged[inBoxX()][inBoxY()] = false;
                    }
                } else {
                    if(!flagged[inBoxX()][inBoxY()]) {
                        revealed[inBoxX()][inBoxY()] = true;
                    }
                }
            }

            if (inReset()) {
                reset();
            }
            if (inFlag()) {
                if (!flag) {
                    flag = true;
                }
                else {
                    flag = false;
                }
            }


        }

        @Override
        public void mousePressed (MouseEvent e){

        }

        @Override
        public void mouseReleased (MouseEvent e){

        }

        @Override
        public void mouseEntered (MouseEvent e){

        }

        @Override
        public void mouseExited (MouseEvent e){

        }
    }

    public int inBoxX() {
        for (int i = 1; i < 15; i++) {
            for (int j = 1; j < 10; j++) {
                if (mx >= spacing * 2 + i * 50 &&
                        mx < spacing + i * 50 + 50 &&
                        my >= j * 50 + 50 + 36 &&
                        my < j * 50 + 50 + 36 + 50) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int inBoxY() {
        for (int i = 1; i < 15; i++) {
            for (int j = 1; j < 10; j++) {
                if (mx >= spacing * 2 + i * 50 &&
                        mx < spacing + i * 50 + 50 &&
                        my >= j * 50 + 50 + 36 &&
                        my < j * 50 + 50 + 36 + 50) {
                    return j;
                }
            }
        }
        return -1;
    }

    public boolean isN(int dX, int dY, int cX, int cY) {
        if (dX - cX < 2 && dX - cX > -2
                && dY - cY < 2 && dY - cY > -2 && bombs[cX][cY] == 1) {
            return true;
        }
        return false;
    }

    public boolean notN(int dX, int dY, int cX, int cY) {
        if (dX - cX < 2 && dX - cX > -2
                && dY - cY < 2 && dY - cY > -2 && bombs[cX][cY] == 0 && neighbours[cX][cY]<=8) {
            return true;
        }
        return false;
    }

    public void reset() {
        isReset = true;
        startDate = new Date();
        smileyface = true;
        win = false;
        loser = false;
        flag = false;

        for (int i = 1; i < 15; i++) {
            for (int j = 1; j < 10; j++) {
                if (rand.nextInt(100) < 20) {  //aprox 20% din patrate sa fie bombe
                    bombs[i][j] = 1;
                } else {
                    bombs[i][j] = 0;
                }
                revealed[i][j] = false;
                flagged[i][j] = false;
            }
        }

        for (int i = 1; i < 15; i++) {
            for (int j = 1; j < 10; j++) {
                vecini = 0;
                for (int m = 1; m < 15; m++) {
                    for (int n = 1; n < 10; n++) {
                        if (!(n == j && i == m) && isN(i, j, m, n)) {
                            vecini++;
                        }
                    }
                    neighbours[i][j] = vecini;
                }
            }
        }
        isReset = false;
    }

    public void ifWin() {
        for (int i = 1; i < 15; i++) {
            for (int j = 1; j < 10; j++) {
                if (revealed[i][j] && bombs[i][j] == 1) {
                    loser = true;
                    smileyface = false;
                }
            }
        }
        if (nrRevealed() >= 126 - nrBombs()) {
            win = true;
        }
    }

    public int nrBombs() {
        int total = 0;
        for (int i = 1; i < 15; i++) {
            for (int j = 1; j < 10; j++) {
                if (bombs[i][j] == 1) {
                    total++;
                }
            }
        }
        return total;
    }

    public int nrRevealed() {
        int total = 0;
        for (int i = 1; i < 15; i++) {
            for (int j = 1; j < 10; j++) {
                if (revealed[i][j]) {
                    total++;
                }
            }
        }
        return total;
    }

    public boolean inReset() {
        if (mx >= 60 && mx < 175 && my >= 65 && my < 105)
            return true;
        return false;
    }

    public boolean inFlag() {
        if (mx >= 185 && mx < 225 && my >= 65 && my < 105)
            return true;
        return false;
    }

    public static int discover(int i, int j){
        int k;
        int[] dx = {0, 0, 1, -1, 1, 1, -1, -1};
        int[] dy = {1, -1, 0, 0, 1, -1, 1, -1};
        for (i = 1; i < 15; i++) {
            for ( j = 1; j < 10; j++) {
                if(bombs[i][j] == 1)
                    return -1;
                if(rev[i][j]) {
                    revealed[i][j] = true;
                    return 0;
                }
                if(neighbours[i][j] >= 1 && neighbours[i][j] <= 8){
                    rev[i][j] = true;
                    return 1;
                }
                if(revealed[i][j]){
                    rev[i][j] = true;
                    for(k = 0; true; k++)
                        return discover(i+dx[k], j+dy[k]);
                }
            }
        }
        return 0;
    }

}


