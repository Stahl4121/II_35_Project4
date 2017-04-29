import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * This Class creates an ai opponent for a human to play against. 
 * The AI goes second and plays according to a minimax algorithm
 * set to a specific depth.
 * @author MUMAWBM1 & STAHLLR1
 *
 */
public class AIPlayer extends Player {
	//Proper Practice here?
	public int numRecursions=0;
	private final int DEFAULT_DEPTH = 8;
	private int depth;
	
	/**
	 * Default constructor for an AI object.
	 * It sets the depth to a default value of 8.
	 */
	public AIPlayer(){
		color = SquareStatus.WHITE;
		depth = DEFAULT_DEPTH;
	}
	
	
	/**
	 * This is a constructor for an AI object
	 * that sets a specific depth.
	 * @param depth	How many moves into the future the AI should look.
	 */
	public AIPlayer(int depth){
		color = SquareStatus.WHITE;
		this.depth = depth;
	}
	
	/**
	 * This is a constructor for an AI object
	 * that sets a specific color.
	 * @param color The color of the AIPlayer
	 */
	public AIPlayer(SquareStatus color){
		this.color = color;
		depth = DEFAULT_DEPTH;
	}
	
	/**
	 * This is a constructor for an AI object
	 * that sets a specific depth and color.
	 * @param depth	How many moves into the future the AI should look.
	 * @param color 
	 */
	public AIPlayer(int depth, SquareStatus color){
		this.color = color;
		this.depth = depth;
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
	@Override
	public Move getMove(Board current){
		
		ArrayList<Move> list = new ArrayList<Move>();
		for(int r=0 ; r < current.BOARD_SIZE ; r++){
			for(int c = 0; c < current.BOARD_SIZE; c++){
				Move t = new Move(r,c,color);
				if(current.isLegalMove(t)){
					list.add(t);
				}
			}
		}
		Move result = new Move(list.get(max(current,0,list)));
		System.out.println(numRecursions);
		numRecursions = 0;
		return(result);
		/*HashMap<Move, Integer> move = new HashMap<Move, Integer>();
		move.putAll(max(current,0,m));		
		for(int r=0; r<current.BOARD_SIZE; r++){
			for(int c=0; c<current.BOARD_SIZE;c++){
				Move query = new Move(r, c, color);
				if(move.containsKey(query)){
					m=new Move(query);
				}
			}
		}*/		
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
	public /*Map<Move, Integer>*/int max(Board iterated, int n , ArrayList<Move> moves){
		
		if(n==depth || iterated.isBoardFull()){
			numRecursions++;
			return iterated.getNumTiles(color);
		}
		
		SquareStatus opposingColor;
		if(color == SquareStatus.BLACK){
			opposingColor = SquareStatus.WHITE;
		}
		else{
			opposingColor = SquareStatus.BLACK;
		}

		Board copy = new Board(iterated);
		int maxScore=0;
		ArrayList<Integer> moveIndex = new ArrayList<>();
		for(int i=0;i<moves.size();i++){
			try{
				copy.makeMove(moves.get(i));
				ArrayList<Move> list = new ArrayList<>();
				for(int r=0;r<copy.BOARD_SIZE;r++){
					for(int c=0;c<copy.BOARD_SIZE;c++){
						Move layer = new Move(r,c,opposingColor);
						if (copy.isLegalMove(layer)){
							list.add(layer);
						}
					}
				}
				int temp =min(copy, n+1, list);
				if(maxScore<=temp){
					maxScore=temp;
					if(n==0){
						moveIndex.add(i);
					}
				}

			}
			catch(Exception e){
				//System.out.println("NO!!!!!!!!!");
			}
		}
		Random rnd = new Random();
		int aryLstNum = (rnd.nextInt(119)%moveIndex.size());
		if(n==0){
			return aryLstNum;//element num of best move
		}
		return maxScore;
		
		/*if(n==depth){
			HashMap<Move, Integer> score = new HashMap<Move,Integer>();
			score.put(m, iterated.getNumTiles(color));
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
					Move move = new Move(r,c,color);
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
		return currentMax;*/
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
	public int/*Map<Move, Integer>*/ min(Board iterated, int n, ArrayList<Move> moves){
		
		if(n==depth || iterated.isBoardFull()){
			numRecursions++;
			return iterated.getNumTiles(color);
		}
		
		Board copy = new Board(iterated);
		int minScore=0;
		for(int i=0;i<moves.size();i++){
			try{
				copy.makeMove(moves.get(i));
				ArrayList<Move> list = new ArrayList<>();
				for(int r=0;r<copy.BOARD_SIZE;r++){
					for(int c=0;c<copy.BOARD_SIZE;c++){
						Move layer = new Move(r,c,color);
						if (copy.isLegalMove(layer)){
							list.add(layer);
						}
					}
				}
				int temp = max(copy, n+1, list);
				if(minScore>temp){
					minScore=temp;
				}
				
			}
			catch(Exception e){
				//System.out.println("NO!!!!!!!!!");
			}
		}
		return minScore;
		/*if(n==depth){
			HashMap<Move, Integer> score = new HashMap<Move, Integer>();
			score.put(m, iterated.getNumTiles(color));
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
		return currentMin;*/
	}

}
