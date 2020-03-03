package connectFourClient;

import java.io.*;

import org.springframework.web.client.RestTemplate;

/**
 * This application connects to a server which provides an implementation
 * of the Connect 4 game allowing a client to play against another client
 *
 * @author Richie Duggan
 */
public class ConnectClient {
    
    private static final String restService = "http://localhost:8080";
    private static final String acceptingPlayers = restService + "/isAcceptingClients";
    private static final String colourChosen = restService + "/colourChosen";
    private static final String chooseColour = restService + "/chooseColour";
    private static final String addPlayer = restService + "/addPlayer";
    private static final String gameState = restService + "/gameState";
    private static final String takeTurn = restService + "/takeTurn";
      
    public static void main(String[] args) {

    RestTemplate restTemplate = new RestTemplate();    	
    	
    String colourChosenUser = "";
    int colourChosenInt = 0;
    int colourIndex = 0;
    	
     // see if the server is accepting players
     ServerAnswer servAns = restTemplate.getForObject(acceptingPlayers, ServerAnswer.class);   
    	
     if (!servAns.getAnswer()) {
        System.out.println("The server is currently not accepting any players.");
        return;
     }
	
     System.out.print("Welcome to the game! Please enter your name : ");
		
     // get player name and register the player to the game
     String username = "";
       
     InputStreamReader streamReader = new InputStreamReader(System.in);
     BufferedReader bufferedReader = new BufferedReader(streamReader);
        
     try {
        username = bufferedReader.readLine();
     } catch (IOException e) {
        e.printStackTrace();
        return;
     }
        
     System.out.println("Thank you " + username);
    	
     Player player = restTemplate.getForObject(addPlayer + "/" + username, Player.class);
        
     if (player == null) {
        System.out.println("There was a problem registering you with the server");
	return;
     }
		
    // see if colour has been chosen. If not ask the player what colour
    // is wanted and register this with the server

    servAns = restTemplate.getForObject(colourChosen, ServerAnswer.class);
    	
    if (!servAns.getAnswer()) {	
	while (colourIndex == 0) {			
	   System.out.print(BoardState.welcomeMessage2());	
	      try {
                 colourChosenUser = bufferedReader.readLine();
	      } catch (IOException e) {
	         e.printStackTrace();
	         return;
              }

	      try {
	         colourChosenInt = Integer.parseInt(colourChosenUser);
                 
	         if (colourChosenInt == 1  || colourChosenInt == 2) {
		    colourIndex = colourChosenInt;
		 }
	      } catch (NumberFormatException exception) {
	      }
	   }
			
	   player = restTemplate.getForObject(chooseColour + "/" + player.getId() + "/" + colourIndex, Player.class);

	   if (player.getColour() == colourIndex) {
	      System.out.println("You have been assigned your preferred colour");
	   } else {
	      System.out.println("Your preferred colour could not be chosen and has been automatically chosen");
	   }		
	}
		
        BoardState currentState = new BoardState();
		
        String colChoiceStr = "";
        int colChoice = 0;
		
	// poll the state of the game until it is this player's turn
	while (!currentState.getGameOver()) {
	   currentState = restTemplate.getForObject(gameState,BoardState.class);	
	      if (currentState.getGameOver()) {
	         break;
	      }
			
	      if (currentState.getNumPlayers() != 2 ||
	            currentState.getPlayerGo() != player.getId()) {			
	         try {
		    // TODO : make configurable
		    Thread.sleep(5000);
	         } catch (InterruptedException e) {
	            throw new RuntimeException("Unexpected interrupt", e);
		 }
		    continue;
		 } else {			
		    colChoice = 0;
		
		    while (colChoice <= 0 || colChoice > BoardState.NUM_COLS ) {
		       System.out.print(currentState.boardMessage(player));
					
		       try {
		          colChoiceStr = bufferedReader.readLine();
		       } catch (IOException e) {
		          e.printStackTrace();
		          return;
		       }

		       try {
		          colChoice = Integer.parseInt(colChoiceStr);
		       } catch (NumberFormatException exception) {
		    }
	         }
				
	         // send user choice to server
		 currentState = restTemplate.getForObject(takeTurn + "/" + player.getId() + "/" + colChoice,BoardState.class);
	      }
	   }
		
	   StringBuilder resultText = new StringBuilder();
		
           if (currentState.getWinningColour() == player.getColour()) {
	      resultText.append("You are the winner " + player.getName() + "!!");
	    } else if (currentState.getWinningName() == "") {
	       System.out.println("The server determined too much time has been taken for goes so the game is over");
	    } else {
	       resultText.append("The game is over, " + currentState.getWinningName() + " won. " +
	             "Thank you for playing.");
	    }
	    System.out.println(resultText.toString() + " Final board is : \n" + currentState.getBoardState());
	 }
      }
