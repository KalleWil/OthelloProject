import java.util.ArrayList;

/**
 * 
 */
public class SmarterAI implements IOthelloAI {

    private final int CUTOFF = 5;

	public Position decideMove(GameState s) {   // MiniMax search
        return maxValue(0, s).getMove();
	}
    
    private UtilityMove eval(GameState s) {
        int acquiredTokens = s.getPlayerInTurn() == 1 ? s.countTokens()[0] : -s.countTokens()[1];
        return new UtilityMove(acquiredTokens, null);
	}

    private UtilityMove maxValue(int depth, GameState s) {
        if (isCutoff(depth,s)) return eval(s);
        
        UtilityMove bestMove = new UtilityMove(Integer.MIN_VALUE, null);
        for (Position p : s.legalMoves()) {
            System.out.println("maxpositions is null: " + (p == null));
            UtilityMove currentMove = minValue(depth+1, futureState(p, s));
            if(currentMove.utility > bestMove.utility){
                bestMove = currentMove;
            }
        }
        return bestMove;
    }

    private UtilityMove minValue(int depth, GameState s) {
        if (isCutoff(depth,s)) return eval(s);
        
        UtilityMove bestMove = new UtilityMove(Integer.MAX_VALUE, null);
        for (Position p : s.legalMoves()) {
            System.out.println("minposition is null: " + (p == null));
            UtilityMove currentMove = maxValue(depth+1, futureState(p, s));
            if(currentMove.utility < bestMove.utility){
                bestMove = currentMove;
            }
        }
        return bestMove;
    }

	private boolean isCutoff(int depth, GameState s) {
        return s.isFinished() || depth > CUTOFF; // Returns if we have reached a terminalstate or our cutoff constant.
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
        return move;
    }
}