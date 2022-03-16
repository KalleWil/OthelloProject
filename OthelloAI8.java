import java.util.Date;

/**
 * Group 8's Othello AI
 */
public class OthelloAI8 implements IOthelloAI {

    private final int CUTOFF = 7;
    private long startTime;
    private long totalMoveTime = 0;
    private double moves = 0.0;

    // MiniMax search
	public Position decideMove(GameState s) {   
        startTime = new Date().getTime();

        Position newMove = maxValue(0, s, new Position(-1, -1), Integer.MIN_VALUE, Integer.MAX_VALUE).getMove();
        
        long endtime = new Date().getTime();
        long timeElapsed = endtime - startTime;
        totalMoveTime += timeElapsed;
        moves++;
        System.out.println("Milliseconds elapsed: " + timeElapsed + "\t Average move time: " + totalMoveTime/moves);

        return newMove;
	}
    
    private UtilityMove eval(GameState s, Position p, int depth) {
        int[][] board = s.getBoard();
        int utility = 0;
        int size = board.length - 1;
        for(int i = 0; i <= size; i++) {
            for(int j = 0; j <= size; j++) {
                // check if the placement is in the corner
                if(s.getPlayerInTurn() == board[i][j]){
                    if((i == 0 && j == 0)  
                    || (i == size && j == size)
                    || (i == size && j == 0) 
                    || (i == 0 && j == size)) {
                        utility += 5;
                    } else if (i == 0 || j == 0 || j == size || i == size) {    // Edges
                        utility += 3;
                    } else {
                        utility += 1;
                    }
                }
            }
        }
        if (depth%2 == 1) utility = utility * -1; // Check if we are at a min node
        return new UtilityMove(utility, p);
	}

    // Find MAX value for the MiniMax algorithm using a beta-cutoff
    private UtilityMove maxValue(int depth, GameState s, Position p, int alpha, int beta) {
        if (isCutoff(depth,s)) return eval(s, p, depth);
        
        UtilityMove bestMove = new UtilityMove(Integer.MIN_VALUE, new Position(-1, -1));
        for (Position p2 : s.legalMoves()) {
            UtilityMove currentMove = minValue(depth+1, futureState(p2, s), p2, alpha, beta);
            if(currentMove.getUtility() > bestMove.getUtility()) {
                bestMove.setUtility(currentMove.getUtility());
                bestMove.setMove(p2);
                alpha = Math.max(alpha, bestMove.getUtility());
            }
            if(bestMove.getUtility() >= beta) return bestMove;
        }
        return bestMove;
    }

    // Find MIN value for the Minimax algorithm using a alpha-cutoff
    private UtilityMove minValue(int depth, GameState s, Position p, int alpha, int beta) {
        if (isCutoff(depth,s)) return eval(s, p, depth);
        
        UtilityMove bestMove = new UtilityMove(Integer.MAX_VALUE, new Position(-1, -1));
        for (Position p2 : s.legalMoves()) {
            UtilityMove currentMove = maxValue(depth+1, futureState(p2, s), p2, alpha, beta);
            if(currentMove.getUtility() < bestMove.getUtility()) {
                bestMove.setUtility(currentMove.getUtility());
                bestMove.setMove(p2);
                beta = Math.min(beta, bestMove.getUtility());
            }
            if(bestMove.getUtility() <= alpha) return bestMove;
        }
        return bestMove;
    }

    // Returns if we have reached a terminalstate, our cutoff constant or we do not have any legal moves left.
	private boolean isCutoff(int depth, GameState s) {
        if(s.isFinished()){
            return true;
        }
        return depth > CUTOFF || s.legalMoves().size() == 0;
	}

    // Setup of new GameState used for recursive call for each branch
    private GameState futureState(Position p, GameState s) {
        GameState tmpState = new GameState(s.getBoard(), s.getPlayerInTurn());
		tmpState.insertToken(p);
        return tmpState;
    }
}

class UtilityMove {
    int utility;
    Position move;

    public UtilityMove(int utility, Position move) {
        this.utility = utility;
        this.move = move;
    }
    
    public int getUtility() {
        return utility;
    }

    public Position getMove() {
        return this.move;
    }

    public void setMove(Position move) {
        this.move = move;
    }

    public void setUtility(int utility) {
        this.utility = utility;
    }
}