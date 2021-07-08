package main;

import java.util.Date;
import java.util.Random;

public class Board {
    // must be EVEN number!! and bigger than 4!!
    // official rule is 8x8
    final int WIDTH = 6;
    final int HEIGHT = 6;

    int countFlip(Stone[][] stones, int myColor, int x, int y, int dx, int dy) {
        // 置きたい場所から見て、いくつ石をひっくり返せるかを返す関数
        // アイデアはここからパクりました　https://www3.cuc.ac.jp/~miyata/classes/prg2/09/reversi.html
        int count = 0;
        final int enemyColor = myColor * -1;
        x += dx;
        y += dy;

        while (0 <= x && x < this.WIDTH && 0 <= y && y < this.HEIGHT) {
            if (stones[y][x].check() == myColor) {
                return count;
            } else if (stones[y][x].check() == enemyColor) {
                count++;
            } else {
                return 0;
            }
            x += dx;
            y += dy;
        }

        return 0;
    }

    boolean[][] checkCanPut(Stone[][] stones, int myColor) {
        // アイデアはここからパクりました　https://www3.cuc.ac.jp/~miyata/classes/prg2/09/reversi.html
        Board board = new Board();
        boolean[][] newCanPut = new boolean[board.HEIGHT][board.WIDTH];

        for (int y = 0; y < board.HEIGHT; y++) {
            for (int x = 0; x < board.WIDTH; x++) {
                // そこに石があれば置けない
                if (stones[y][x].check() != 0) {newCanPut[y][x] = false;continue;}

                // 8方向調べる
                if (this.countFlip(stones, myColor, x, y, +1, +1) > 0) {newCanPut[y][x] = true;continue;}
                if (this.countFlip(stones, myColor, x, y,  0, +1) > 0) {newCanPut[y][x] = true;continue;}
                if (this.countFlip(stones, myColor, x, y, -1, +1) > 0) {newCanPut[y][x] = true;continue;}
                if (this.countFlip(stones, myColor, x, y, +1,  0) > 0) {newCanPut[y][x] = true;continue;}
                if (this.countFlip(stones, myColor, x, y, -1,  0) > 0) {newCanPut[y][x] = true;continue;}
                if (this.countFlip(stones, myColor, x, y, +1, -1) > 0) {newCanPut[y][x] = true;continue;}
                if (this.countFlip(stones, myColor, x, y,  0, -1) > 0) {newCanPut[y][x] = true;continue;}
                if (this.countFlip(stones, myColor, x, y, -1, -1) > 0) {newCanPut[y][x] = true;continue;}

                // だめなら置けない
                newCanPut[y][x] = false;
            }
        }
        return newCanPut;
    }

    boolean checkCanNotPut(boolean[][] canPut) {
        for (int i = 0; i < this.HEIGHT; i++) {
            for (int j = 0; j < this.WIDTH; j++) {
                if (canPut[i][j]) return false;
            }
        }
        return true;
    }

    int[] searchPlaceCanPut(boolean[][] canPut) {
        // 適当に置ける場所を探して返す
        int x, y;
        long seed = (new Date()).getTime();
        Random random = new Random(seed);
        int[] placeCanPut = {-1, -1};
        if (checkCanNotPut(canPut)) return placeCanPut;

        while (true) {
            x = random.nextInt(this.WIDTH);
            y = random.nextInt(this.HEIGHT);
            if (canPut[y][x]) {
                placeCanPut[0] = x;
                placeCanPut[1] = y;
                return placeCanPut;
            }
        }
    }

    void allReverse(Stone[][] stones, int myColor, int putx, int puty) {
        // reverse stones
        int x = putx, y = puty;
        int dx, dy, numberOfReverse;
        int[][] delta = {{1,1}, {0,1}, {-1,1}, {1,0}, {-1,0}, {1,-1}, {0,-1}, {-1,-1}};
        for (int direction = 0; direction < delta.length; direction++) {
            x = putx;
            y = puty;
            dx = delta[direction][0];
            dy = delta[direction][1];
            numberOfReverse = this.countFlip(stones, myColor, putx, puty, dx, dy);
            for (int i = 0; i < numberOfReverse; i++) {
                x += dx;
                y += dy;
                stones[y][x].reverse();
            }
        }
    }

    void printBoard(Stone[][] stones) {
        Board board = new Board();
        String message = " ";

        for (int i = 0; i < board.WIDTH; i++) {
            message += " " + Integer.toString(i);
        }
        System.out.println(message);

        for (int i = 0; i < board.HEIGHT; i++) {
            message = Integer.toString(i);
            for (int j = 0; j < board.WIDTH; j++) {
                if (stones[i][j].check() == Stone.WHITE) {
                    message += " o";
                } else if (stones[i][j].check() == Stone.BLACK) {
                    message += " x";
                } else {
                    message += " .";
                }
            }
            System.out.println(message);
        }
    }

    int displayCountOfStones(Stone[][] stones) {
        int white = 0, black = 0, winner = 0;
        for (int i = 0; i < this.HEIGHT; i++) {
            for (int j = 0; j < this.WIDTH; j++) {
                winner += stones[i][j].check();
                if (stones[i][j].check() == Stone.WHITE) {
                    white++;
                } else if (stones[i][j].check() == Stone.BLACK) {
                    black++;
                }
            }
        }

        String message = "White: " + white +"  Black: " + black;
        System.out.println(message);

        return winner;
    }

    boolean checkInputValue(int x, int y) {
        if (x < 0 || x >= this.WIDTH) return false;
        if (y < 0 || y >= this.HEIGHT) return false;
        return true;
    }
}
