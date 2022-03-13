import java.util.ArrayList;
import java.util.Date;

/**
 * 
 */
public class SmarterAI implements IOthelloAI {

    private final int CUTOFF = 5;
    private final int secondsCutoff = 10;
    private long startTime;
    private long totalMoveTime = 0;
    private double moves = 0.0;

	public Position decideMove(GameState s) {   // MiniMax search
        startTime = new Date().getTime()/1000;
        Position newMove = maxValue(0, s, new Position(-1, -1)).getMove();
        // TODO: this is not a good solution
        // This occources when the algorithm can't find any legal moves. 
        // Though, it should still be able find a move either horizontal, vertical or diagonally
        if((newMove.col == -1 || newMove.row == -1) && s.legalMoves().size() != 0){
            System.out.println("The Laurits fix🔥");
            for(Position p : s.legalMoves()){
                System.out.println("Possible move: r:" + p.row + " - c:" + p.col);
            }
            return s.legalMoves().get(0);
        }
        long endtime = new Date().getTime()/1000;
        long timeElapsed = endtime - startTime;

        totalMoveTime += timeElapsed;
        moves++;

        System.out.println("Seconds elapsed: " + timeElapsed + "\t Average move time: " + totalMoveTime/moves);
        return newMove;
	}
    
    private UtilityMove eval(GameState s, Position p) {
        //int acquiredTokens = s.getPlayerInTurn() == 1 ? s.countTokens()[0] : -s.countTokens()[1];
        int[][] board = s.getBoard();
        int utility = 0;
        int size = board.length - 1;
        for(int i = 0; i <= size; i++){
            for(int j = 0; j <= size; j++){
                if(s.getPlayerInTurn() == board[i][j]){
                    if((i == 0 && j == 0) 
                        || (i == size && j == size)
                        || (i == size && j == 0) 
                        || (i == 0 && j == size)) {   // Corners
                        utility += 5;
                    } else if (i == 0 || j == 0 || j == size || i == size) {    // Edges
                        utility += 3;
                    } else {
                        utility += 1;
                    }
                }
            }
        }
        return new UtilityMove(utility, p);
	}

    private UtilityMove maxValue(int depth, GameState s, Position p) {
        if (isCutoff(depth,s)) return eval(s, p);
        
        UtilityMove bestMove = new UtilityMove(Integer.MIN_VALUE, new Position(-1, -1));
        for (Position p2 : s.legalMoves()) {
            UtilityMove currentMove = minValue(depth+1, futureState(p2, s), p2);
            if(currentMove.getUtility() > bestMove.getUtility()){
                bestMove.setUtility(currentMove.getUtility());
                bestMove.setMove(p2);
            }
        }
        return bestMove;
    }

    private UtilityMove minValue(int depth, GameState s, Position p) {
        if (isCutoff(depth,s)) return eval(s, p);
        
        UtilityMove bestMove = new UtilityMove(Integer.MAX_VALUE, new Position(-1, -1));
        for (Position p2 : s.legalMoves()) {
            UtilityMove currentMove = maxValue(depth+1, futureState(p2, s), p2);
            if(currentMove.getUtility() < bestMove.getUtility()){
                bestMove.setUtility(currentMove.getUtility());
                bestMove.setMove(p2);
            }
        }
        return bestMove;
    }

	private boolean isCutoff(int depth, GameState s) {
        return s.isFinished() || depth > CUTOFF || (new Date().getTime()/1000 - startTime) >= secondsCutoff; // Returns if we have reached a terminalstate or our cutoff constant.
	}

    private GameState futureState(Position p, GameState s){
        GameState tmpState = new GameState(s.getBoard(), s.getPlayerInTurn());
		tmpState.insertToken(p);
        return tmpState;
    }
}

class UtilityMove {
    int utility;
    Position move;

    public UtilityMove(int utility, Position move){
        this.utility = utility;
        this.move = move;
    }
    
    public int getUtility(){
        return utility;
    }

    public Position getMove(){
        return this.move;
    }

    public void setMove(Position move){
        this.move = move;
    }

    public void setUtility(int utility){
        this.utility = utility;
    }
}