package main;

import java.util.Scanner;

public class Othello {
    final static int PLAYER_COLOR = Stone.WHITE;
    final static int ENEMY_COLOR = PLAYER_COLOR * -1;
    static int turn = PLAYER_COLOR; // プレイヤーが先攻とした
    static boolean didPass = false; // 前回のプレイヤーがパスしたか。2回パスしたときはゲーム終了

    final static boolean COMPUTER = true; // コンピュータと対戦するか
    final static boolean AUTO_PLAY = false; // オートプレイにするか（みてるだけ）

    public static void main(String args[]) {
        // scannerの設定
        Scanner scanner = new Scanner(System.in);
        scanner.reset();

        // 初期化
        int x = 0, y = 0, winner = 0;
        Board board = new Board();
        Stone[][] stones = new Stone[board.HEIGHT][board.WIDTH]; // 配列用の領域を確保しただけに過ぎないので注意！！
        boolean[][] canPut = new boolean[board.HEIGHT][board.WIDTH];
        int[] placeCanPut = {-1, -1};

        for (int i = 0; i < board.HEIGHT; i++) {
            for (int j = 0; j < board.WIDTH; j++) {
                // ここで石の実体を作成している
                stones[i][j] = new Stone();
                // 最初はどこも置けないのでfalse
                canPut[i][j] = false;
            }
        }

        // 4つの石を置く 多分HEIGHTとWIDTHが偶数でなくてもエラーは出ない
        stones[board.HEIGHT / 2 - 1][board.WIDTH / 2 - 1].put(Stone.WHITE);
        stones[board.HEIGHT / 2 - 1][board.WIDTH / 2    ].put(Stone.BLACK);
        stones[board.HEIGHT / 2    ][board.WIDTH / 2 - 1].put(Stone.BLACK);
        stones[board.HEIGHT / 2    ][board.WIDTH / 2    ].put(Stone.WHITE);
        board.printBoard(stones);

        // ゲーム開始
        for (int allTurn = 0; allTurn < board.HEIGHT * board.WIDTH - 4; allTurn++) { // -4 は、最初置いてある石の分
            canPut = board.checkCanPut(stones, turn); // 置ける場所を更新
            if (board.checkCanNotPut(canPut)) { // 置ける場所がなければ
                System.out.println("パス！");
                turn *= -1;
                allTurn--;
                continue;
            }

            if ((COMPUTER && turn == ENEMY_COLOR) || AUTO_PLAY) {
                placeCanPut = board.searchPlaceCanPut(canPut);
                x = placeCanPut[0];
                y = placeCanPut[1];
                System.out.println("ENEMY: " + x + " " + y);
            } else {
                x = scanner.nextInt(); // scannerで読み込み
                y = scanner.nextInt();
            }

            if (board.checkInputValue(x, y) && canPut[y][x]) { //指定された位置に置けるなら
                stones[y][x].put(turn);
            } else {
                System.out.println("置けません！"); //パスではない、変なとこに置こうとしたとき
                allTurn--; // ターンはまだ終わっていない
                continue;
            }

            board.allReverse(stones, turn, x, y);; // ひっくり返すよ
            board.printBoard(stones); // 盤面を出力
            winner = board.displayCountOfStones(stones);
            turn *= -1; // ターン交代
        }

        // 終了時処理
        // スコア表示したりいろいろする予定。。。
        System.out.println("ゲーム終了");
        winner = board.displayCountOfStones(stones);
        if (winner > 0) {
            System.out.println("Winner is White!");
        } else if (winner < 0) {
            System.out.println("Winner is Black!");
        } else {
            System.out.println("DRAW...");
        }
        scanner.close();
    }

}



