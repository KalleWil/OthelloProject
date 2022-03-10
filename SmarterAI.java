import java.util.ArrayList;

/**
 * 
 */
public class SmarterAI implements IOthelloAI {

    private final int CUTOFF = 5;

	public Position decideMove(GameState s) {   // MiniMax search
        Position newMove = maxValue(0, s, new Position(-1, -1)).getMove();
        return newMove;
	}
    
    private UtilityMove eval(GameState s, Position p) {
        int acquiredTokens = s.getPlayerInTurn() == 1 ? s.countTokens()[0] : -s.countTokens()[1];
        return new UtilityMove(acquiredTokens, p);
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
        return this.move;
    }

    public void setMove(Position move){
        this.move = move;
    }

    public void setUtility(int utility){
        this.utility = utility;
    }
}