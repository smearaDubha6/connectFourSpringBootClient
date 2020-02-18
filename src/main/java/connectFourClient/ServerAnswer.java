package connectFourClient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is a simple class used to hold the boolean answer from the server
 * to a question asked by the client
 *
 * @author Richie Duggan
 */
public class ServerAnswer {

  private boolean answer;

  public ServerAnswer() {
  }

  public boolean getAnswer() {
	  return this.answer;
  }
  
  public void setAnswer(boolean answer) {
	  this.answer = answer;
  }
}