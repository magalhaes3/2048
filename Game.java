import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game{

    private static Tile[][] game_board = new Tile[4][4];
    private static GUI gui;
    private boolean movementPreviousRound;
    private boolean gameRunning;
    private int flag;

    private Random random = new Random();

    private Game(){
        gameRunning = false;
        flag = 0;
        initGameboard();
        spawn();
        gui = new GUI(this);
    }

    Tile[][] getGameBoard(){
        return game_board;
    }

    private void initGameboard(){
        this.movementPreviousRound = true;
        for (int i = 0; i != game_board.length; i++){
            for (int j = 0; j != game_board[i].length; j++){
                game_board[i][j] = new Tile();
            }
        }
    }

    private boolean movesLeft(){
        //from this point it will try to make every move to see if there's any available
        for (int j = 0; j != game_board[0].length; j++){
            for (int i = 1; i != game_board.length ; i++){
                boolean toReturn = tryMerge(j, i-1, j, i);
                if (toReturn){
                    return true;
                }
            }
        }
        for (int j = 0; j != game_board[0].length; j++){
            for (int i = game_board.length-2; i >= 0 ; i--){
                boolean toReturn = tryMerge(j, i+1, j, i);
                if (toReturn){
                    return true;
                }
            }
        }
        for (int i = 0; i != game_board.length; i++){
            for(int j = 1; j != game_board[i].length; j++){
                boolean toReturn = tryMerge(j-1, i, j, i);
                if (toReturn){
                    return true;
                }
            }
        }
        for (int i = 0; i != game_board.length; i++){
            for (int j = game_board[i].length - 2; j >= 0; j--){
                boolean toReturn = tryMerge(j+1, i, j, i);
                if (toReturn){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean tryMerge(int j, int i, int j1, int i1) {
        int referenceValue = game_board[j][i].getValue();
        int valueToMerge = game_board[j1][i1].getValue();

        if (referenceValue == valueToMerge){
            System.out.println("TRY MERGE RETURNED TRUE");
            return true;
        } else {
            System.out.println("TRY MERGE RETURNED FALSE");
            return false;
        }
    }

    private boolean spaceAvailable(){
        for (int i = 0; i != game_board.length; i++){
            for (int j = 0; j != game_board[i].length; j++){
                if (game_board[i][j].getValue() == 0){
                    return true;
                }
            }
        }
        return false;
    }

    private void spawn(){
        if (spaceAvailable() && movementPreviousRound){
            movementPreviousRound = false;
            AtomicBoolean set = new AtomicBoolean(false);
            while (!set.get()){
                int a = random.nextInt(4);
                int b = random.nextInt(4);
                if(game_board[a][b].getValue() == 0){
                    game_board[a][b].setValue(2);
                    set.set(true);
                }
            }
        } else if (!movesLeft()){
            System.out.println("GAME FINISHED: NO MOVES LEFT");
            stopRunning();
            //System.exit(1);
        }
    }

    private void showBoard(){
        spawn();
        System.out.println("====== BOARD =======");
        for(int y = 0; y != game_board.length; y++){
            for(int x = 0; x != game_board[y].length; x++){
                System.out.print(game_board[y][x].getValue() + " ");
            }
            System.out.println();
        }

    }

    private void merge(int x1, int y1, int x2, int y2, Direction direction){
        int referenceValue = game_board[y1][x1].getValue();
        int valueToMerge = game_board[y2][x2].getValue();

        if (referenceValue == valueToMerge && valueToMerge != 0 && !game_board[y1][x1].isMerged()){
            game_board[y1][x1].setValue(referenceValue + valueToMerge);
            game_board[y1][x1].setMerged(true);
            game_board[y2][x2] = new Tile();
            movementPreviousRound = true;
        }

        if (referenceValue != valueToMerge && referenceValue == 0){
            game_board[y1][x1].setValue(valueToMerge);
            game_board[y2][x2].setValue(referenceValue);
            movementPreviousRound = true;
            switch (direction){
                case UP:
                    if(y1 - 1 >= 0)
                        merge(x1, y1-1, x1, y1, Direction.UP);
                    break;
                case DOWN:
                    if(y1 + 1 < game_board.length)
                        merge(x1, y1+1, x1, y1, Direction.DOWN);
                    break;
                case LEFT:
                    if(x1 - 1 >= 0)
                        merge(x1-1, y1, x1, y1, Direction.LEFT);
                    break;
                case RIGHT:
                    if(x1 + 1 < game_board.length)
                        merge(x1+1, y1, x1, y1, Direction.RIGHT);
                    break;
            }
        }
    }

    private void clearMerges(){
        for (int i = 0; i != game_board.length; i++){
            for (int j = 0; j != game_board[i].length; j++){
                game_board[i][j].setMerged(false);
            }
        }
    }

    void moveUp(){
        for (int j = 0; j != game_board[0].length; j++){
            for (int i = 1; i != game_board.length ; i++){
                merge(j, i-1, j, i, Direction.UP);
            }
        }
        showBoard();
        clearMerges();
    }

    void moveDown(){
        for (int j = 0; j != game_board[0].length; j++){
            for (int i = game_board.length-2; i >= 0 ; i--){
                merge(j, i+1, j, i, Direction.DOWN);
            }
        }
        showBoard();
        clearMerges();
    }

    void moveLeft(){
        for (int i = 0; i != game_board.length; i++){
            for(int j = 1; j != game_board[i].length; j++){
                merge(j-1, i, j, i, Direction.LEFT);
            }
        }
        showBoard();
        clearMerges();
    }

    void moveRight(){
        for (int i = 0; i != game_board.length; i++){
            for (int j = game_board[i].length - 2; j >= 0; j--){
                merge(j+1, i, j, i, Direction.RIGHT);
            }
        }
        showBoard();
        clearMerges();
    }

    void stopRunning(){
        flag = 1;
        gameRunning = false;
    }

    void startRunning(){
        flag = 0;
        initGameboard();
        spawn();
        gameRunning = true;
    }

    boolean isRunning(){
        return gameRunning;
    }

    int getFlag(){
        return flag;
    }

    public static void main(String[] args){
        Game test = new Game();
        gui.show_gui();
        test.showBoard();
    }

}
