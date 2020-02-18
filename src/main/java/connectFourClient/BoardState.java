package connectFourClient;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the state of the board at the point in time that the 
 * server sends back for most of its responses. It is used by the client to 
 * figure out at what point the game is at
 *
 * @author Richie Duggan
 */
public class BoardState {

	private int playerGo;
	private String board;
	private int numPlayers;
	private boolean gameOver;
	private String winningName;
	private int winningColour;
	
    public static final int NUM_ROWS = 6;
    public static final int NUM_COLS = 9;
	
    public static final int YELLOW = 1;
    public static final int RED = 2;
	
	private final static Map < Integer, String > colourTextMapping = new HashMap < > () {
        {
            put(YELLOW, "yellow");
            put(RED, "red");
        }
    };

    private final static Map < Integer, Character > numberMapping = new HashMap < > () {
        {
            put(YELLOW, 'x');
            put(RED, 'o');
        }
    };
	
	public BoardState() {
	}
	
	public int getPlayerGo() {
		return playerGo;
	}
	
	public void setPlayerGo(int go) {
		playerGo = go;
	}
	
	public String getBoardState() {
		return board; 
	}
	
	public void setBoardState(String newState) {
		board = newState;
	}
	
	public int getNumPlayers() {
		return numPlayers;
	}
	
	public void setNumPlayer(int go) {
		playerGo = numPlayers;
	}
	
	public boolean getGameOver() {
		return gameOver;
	}
	
	public int getWinningColour() {
		return winningColour;
	}
	
	public String getWinningName() {
		return winningName;
	}
	
	public static String welcomeMessage2() {
	    String msg = "Please choose your preferred colour. Use " + YELLOW + " for " + 
	    		colourTextMapping.get(YELLOW) + " and " + RED + " for " + 
	    		colourTextMapping.get(RED) + " : ";
	    return msg;
	}
	
	public String boardMessage(Player player) {
    	String msg = this.board + player.getName() + ",here is board above. You" +
    			" are colour " + colourTextMapping.get(player.getColour()) +
    			" represented as '" + numberMapping.get(player.getColour()) + 
    			"' on the board. Pick a column (1-" + NUM_COLS + ") that has free space : ";
    	return msg;
    }
	
	public String winningMessage(Player player,String winningName) {

        StringBuilder resultText = new StringBuilder();
        
        if (player.getWinner()) {
            resultText.append("You are the winner " + player.getName() + "!!");
        } else {
            resultText.append("The game is over, " + winningName + " won. " +
                "Thank you for playing.");
        }

        return(resultText.toString() + " Final board is : \n" + this.toString());
    }
 }
