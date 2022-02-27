import java.util.ArrayList;

/**
 * 
 */
public class SmarterAI implements IOthelloAI {

	public Position decideMove(GameState s) {
        ArrayList<Position> moves = s.legalMoves();

        Position bestPos = null;
        int bestEval = 0;
        for (Position position : moves) {
            int curEval = eval(position, s);
            if (curEval >= bestEval) { 
                bestEval = curEval;
                bestPos = position;
            }
        }

        if(bestPos != null){
            return bestPos;
        }

		return null;
	}
    
    private int eval(Position p, GameState s) {
		GameState tmpState = new GameState(s.getBoard(), s.getPlayerInTurn());
		tmpState.insertToken(p);
        return tmpState.countTokens()[tmpState.getPlayerInTurn()-1];
	}

	private boolean isCutoff() {
		return false;
	}
}