import java.util.HashMap;
import java.util.Map;
/**
 * This Class creates an ai opponent for a human to play against. 
 * The AI goes second and plays according to a minimax algorithm
 * set to a specific depth.
 * @author MUMAWBM1 & STAHLLR1
 *
 */
public class AI {

	private int depth;
	/**
	 * Default constructor for an AI object.
	 * It sets the depth to a default value of 8.
	 */
	public AI(){
		depth=8;
	}
	
	
	/**
	 * This is a constructor for an AI object
	 * that sets a specific depth.
	 * @param depth	How many moves into the future the AI should look.
	 */
	public AI(int depth){
		this.depth=depth;
	}
	
	/**
	 * A getter for depth.
	 * @return depth, how many moves into the future the ai is calculating.
	 */
	public int getDepth(){
		return depth;
	}
	
	/**
	 * A setter for depth.
	 * @param d, How many moves into the future the AI calculates
	 */
	public void setDepth(int d){
		depth=d;
	}
	
	/**
	 * Starts the minimax process, and then returns the final selected move to main
	 * @param current Takes in the current state of the board to figure out how many
	 * @return Returns the most optimal move to be played by the AI
	 */
	public Move aiMove(Board current){
		Move m= new Move();
		HashMap<Move, Integer> move = new HashMap<Move, Integer>();
		move.putAll(max(current,0,m));		
		for(int r=0; r<current.BOARD_SIZE; r++){
			for(int c=0; c<current.BOARD_SIZE;c++){
				Move query = new Move(r, c, SquareStatus.WHITE);
				if(move.containsKey(query)){
					m=new Move(query);
				}
			}
		}
		return m;
		
	}

	/**
	 * This method is the max portion of minimax, and as such finds the best possible
	 * move the ai can make
	 * @param iterated	Board Object passed from max. It is a projected move possibility
	 * @param n		Iteration counter for recursion
	 * @param m		Move Object that allows the min/max chain to keep track of move to pass up chain
	 * @return		Returns a Map that uses a Move object as the key and an 
	 * 				Integer value as the value. This is how all pertinent information is relayed
	 * 				back up to aiMove.
	 */
	public Map<Move, Integer> max(Board iterated, int n , Move m){
		if(n==depth){
			HashMap<Move, Integer> score = new HashMap<Move,Integer>();
			score.put(m, iterated.getNumTiles(SquareStatus.WHITE));
			return score;
		}
		Board copy = new Board(iterated);
		Map<Move, Integer> currentMax = new HashMap<Move, Integer>();
		if(n==0){
			currentMax.put(m, -9999999);
		}
		Move currentMaxMove;
		if(m.getColor()==SquareStatus.EMPTY){
			currentMaxMove=null;
		}
		else{
			currentMaxMove= new Move(m);
		}
		for(int r=0;r<copy.BOARD_SIZE;r++){
			for(int c=0;c<copy.BOARD_SIZE;c++){
				if (copy.getSquareStatus(r,c)==SquareStatus.EMPTY){
					Move move = new Move(r,c,SquareStatus.WHITE);
					if(copy.isLegalMove(move)){
						try{
							copy.makeMove(move);
						}
						catch(Exception e){
							e.getMessage();
							break;
						}
						if(n==0){
							currentMax.clear();
							currentMaxMove = new Move(move);
						}
						if(currentMax.isEmpty()){
							Map<Move, Integer> tempMax = new HashMap<Move, Integer>();
							tempMax.putAll(min(copy, n+1, move)); 
							currentMax.replace(currentMaxMove, tempMax.get(move));
						}
						else{
							Map<Move, Integer> tempMax = new HashMap<Move, Integer>();
							tempMax.putAll(min(copy, n+1, m));
							if(currentMax.get(currentMaxMove)<tempMax.get(move)){
								currentMax.replace(currentMaxMove, tempMax.get(move));
							}
						}
					}
				}
			}
		}
		return currentMax;
	}

	
	/**
	 * This method is the min portion of minimax, and as such finds the worst possible
	 * move(for the ai) that the human can make
	 * @param iterated 	Board Object passed from max. It is a projected move possibility
	 * @param n			Keeps track of recursion layer
	 * @param m			Move Object that allows the min/max chain to keep track of move to pass up chain
	 * @return			Returns a Map that uses a Move object as the key and an 
	 * 				Integer value as the value. This is how all pertinent information is relayed
	 * 				back up to aiMove.
	 */
	public Map<Move, Integer> min(Board iterated, int n, Move m){
		if(n==depth){
			HashMap<Move, Integer> score = new HashMap<Move, Integer>();
			score.put(m, iterated.getNumTiles(SquareStatus.WHITE));
			return score;
		}
		Board copy = new Board(iterated);
		Map<Move, Integer> currentMin = new HashMap<Move, Integer>();
		Move currentMinMove=new Move(m);
		for(int r=0;r<copy.BOARD_SIZE;r++){
			for(int c=0;c<copy.BOARD_SIZE;c++){
				if (copy.getSquareStatus(r,c)==SquareStatus.EMPTY){
					Move move = new Move(r,c,SquareStatus.BLACK);
					if(copy.isLegalMove(move)){
						try{
							copy.makeMove(move);
						}
						catch(Exception e){
							e.getMessage();
							break;
						}
						if(currentMin.isEmpty()){
							Map<Move, Integer> tempMax = new HashMap<Move, Integer>();
							tempMax.putAll(min(copy, n+1, move)); 
							currentMin.replace(currentMinMove, tempMax.get(move));
						}
						else{
							Map<Move, Integer> tempMax = new HashMap<Move, Integer>();
							tempMax.putAll(min(copy, n+1, m));
							if(currentMin.get(currentMinMove)>tempMax.get(m)){
								currentMin.replace(currentMinMove, tempMax.get(move));
							}
						}
					}
				}
			}
		}
		return currentMin;
	}
}
