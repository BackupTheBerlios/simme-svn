package at.einspiel.simme.server.management;

import at.einspiel.simme.server.messaging.Message;
import at.einspiel.simme.server.messaging.SimpleXmlMessage;

/**
 * <p>Models the user's state. This class has three main state categories for
 * a user. Each of these categories are treated differently by the session
 * manager.</p>
 * 
 * A <code>ManagedUser</code> holds a state and is updated whenever the state
 * itself has changed. 
 * 
 * @author kariem
 */
public class UserState {

   /** cancels the current operation.  */
   public static final String ST_CANCEL = "cancel";
   /** advances to the next state. */
   public static final String ST_NEXT = "next";

   /** first choice (or win) */
   public static final String ST_1 = "1";
   /** second choice (or loss) */
   public static final String ST_2 = "2";

   /** user has just logged in and has not yet started a game */
   public static final int STATE_IDLE = 0;
   /** user is waiting for other players to play with him */
   public static final int STATE_WAITING = 1;
   /** user is playing */
   public static final int STATE_PLAYING = 2;

   private int stateCategory;
   private DetailedState detailedState;
   private ManagedUser user;

   /**
    * Creates a new instance.
    * 
    * @param user the user that is in this state.
    */
   public UserState(ManagedUser user) {
      this.user = user;
      stateCategory = STATE_IDLE;
      detailedState = new DetailedState();
   }

   /**
    * Sets the state category.
    * 
    * @param state the category.  One of {@link #STATE_IDLE},
    *        {@link #STATE_WAITING}, {@link #STATE_PLAYING}.
    */
   public void setStateCategory(int state) {
      if ((state >= 0) && (state <= 2)) {
         this.stateCategory = state;
         user.update();
      }
   }

   /**
    * Returns the state category
    * 
    * @return The current state. One of {@link #STATE_IDLE},
    *         {@link #STATE_WAITING}, {@link #STATE_PLAYING}.
    */
   public int getStateCategory() {
      return stateCategory;
   }

   /**
    * Returns the current state as string.
    * 
    * @return the state in string representation
    */
   public String getStateAsString() {
      String state = "unknown";
      switch (getStateCategory()) {
         case STATE_IDLE :
            state = "idle";
            break;

         case STATE_PLAYING :
            state = "playing";
            break;

         case STATE_WAITING :
            state = "waiting";
            break;

      }
      return state;
   }

   /**
    * Performs an action on this state.
    * 
    * @param actionString the string defining the action.
    * @throws StateException if the action produced an error.
    */
   public void doAction(String actionString) throws StateException {
      detailedState.changeState(actionString);
   }

   private class DetailedState {
      private int id;
      private Message clientMessage;

      private static final int IDLE = 0;
      private static final int WAITING_FOR_OPP = 1;
      private static final int STARTING = 2;
      private static final int TURN = 3;
      private static final int NOTURN = 4;
      private static final int OVER_WIN = 5;
      private static final int OVER_LOSS = 6;

      /**
       * Changes the state from one <code>DetailedState</code> to another
       * <code>DetailedState</code> with a string that initiates the change.
       * 
       * @param changeString the string which changes the state
       * @return a new <code>DetailedState</code>
       * 
       * @throws StateException if the given <code>changeString</code> does
       *         not change the state or has produced an error.
       */
      void changeState(String changeString) throws StateException {
         boolean advance = changeString.equals(ST_NEXT);
         boolean cancel = changeString.equals(ST_CANCEL);
         switch (id) {
            case WAITING_FOR_OPP :
               if (advance) {
                  setState(STARTING);
               } else if (cancel) {
                  // nothing
               } else {
                  throw new StateException();
               }

               break;
            case STARTING :
               if (!cancel) {
                  if (changeString.equals(ST_1)) {
                     setState(TURN);
                  } else { //if (changeString.equals(ST_2)) {
                     setState(NOTURN);
                  }
               } else {
                  setState(IDLE);
               }
               break;

            case TURN :
               if (advance) {
                  setState(NOTURN);
               } else if (changeString.equals(ST_1)) {
                  setState(OVER_WIN);
               } else {
                  setState(OVER_LOSS);
               }
               break;

            case NOTURN :
               if (advance) {
                  setState(TURN);
               } else if (changeString.equals(ST_1)) {
                  setState(OVER_WIN);
               } else {
                  setState(OVER_LOSS);
               }
               break;
            case OVER_LOSS :
               if (!cancel) {
                  if (changeString.equals(ST_1)) {
                     setState(STARTING);
                     break;
                  }
               }

               setState(IDLE);
               break;

            case OVER_WIN :
               if (!cancel) {
                  if (changeString.equals(ST_1)) {
                     setState(STARTING);
                     break;
                  }
               }

               setState(IDLE);
               break;

            default :
               System.out.println("in default changeState : " + changeString);
               // do nothing
         }
      }

      void setState(int state) {
         id = state;
         switch (state) {
            case IDLE :
               setStateCategory(STATE_IDLE);
               clientMessage = null;
               break;

            case WAITING_FOR_OPP :
               setStateCategory(STATE_WAITING);
               clientMessage = new SimpleXmlMessage("Warte auf Gegner");
               break;

            case STARTING :
               clientMessage = new SimpleXmlMessage("Starte Spiel");
               break;

            case TURN :
               setStateCategory(STATE_PLAYING);
               clientMessage = null;
               break;

            case NOTURN :
               setStateCategory(STATE_PLAYING);
               clientMessage = null;
               break;

            case OVER_LOSS :
               setStateCategory(STATE_WAITING);
               clientMessage = null;
               break;

            case OVER_WIN :
               setStateCategory(STATE_WAITING);
               clientMessage = null;
               break;

            default :
               // do nothing
         }
      }

      /** @return the message to the client. */
      Message getClientMessage() {
         return clientMessage;
      }
   }
}
