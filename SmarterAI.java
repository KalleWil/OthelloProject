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
            System.out.println("Best move: " + bestEval);
            return bestPos;
        }

		return null;
	}
    
    private int eval(Position p, GameState s) {
		GameState tmpState = new GameState(s.getBoard(), s.getPlayerInTurn());
		tmpState.insertToken(p);
        tmpState.changePlayer();
        return tmpState.countTokens()[tmpState.getPlayerInTurn()-1];
	}


    private int recEval(int depth, Position p, GameState s) {
        if (isCutoff(depth)) return eval(p, s);
        // Recursively go through all possible moves from position p, then recursively go through all moves from 'p... Until cutoff is met
        // Corners > sides > normal positions
        return 0;
    }

	private boolean isCutoff(int depth) {
		return depth > 4;
	}
}