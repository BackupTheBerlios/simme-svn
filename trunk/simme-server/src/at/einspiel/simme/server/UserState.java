// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: UserState.java
//                  $Date: 2003/12/30 10:18:25 $
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server;

import at.einspiel.messaging.Message;
import at.einspiel.messaging.SimpleClientMessage;
import at.einspiel.mgmt.*;

/**
 * <p>Models the user's state. This class has three main state categories for
 * a user. Each of these categories are treated differently by the session
 * manager.</p>
 * 
 * <p>A <code>ManagedUser</code> holds a state and is updated whenever the state
 * itself has changed. This class models several states and a certain method to
 * traverse from one state to another.</p> 
 * 
 * @author kariem
 */
public class UserState implements IChangeSupport{

   private ChangeSupport changeSupport;

   /** cancels the current operation.  */
   public static final String ST_CANCEL = "cancel";
   /** advances to the next state. */
   public static final String ST_NEXT = "next";

   /** state 1: first choice (or win) */
   public static final String ST_1 = "1";
   /** state 2: second choice (or loss) */
   public static final String ST_2 = "2";

   // general state categories
   private int stateCategory;
   /** user has just logged in and has not yet started a game */
   public static final int STATE_IDLE = 0;
   /** user is waiting for other player to play with him */
   public static final int STATE_WAITING = 1;
   /** user is playing */
   public static final int STATE_PLAYING = 2;

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
      changeSupport = new ChangeSupport();
   }

   /**
    * Sets the state category.
    * 
    * @param state the category.  One of {@link #STATE_IDLE},
    *         {@link #STATE_WAITING}, {@link #STATE_PLAYING}.
    */
   public void setStateCategory(int state) {
      int old = stateCategory;
      if ((state >= 0) && (state <= 2)) {
         this.stateCategory = state;
         user.update();
      }
      if (old != stateCategory) {
         fireStateEvent(new StateEvent(user, new Integer(old), new Integer(state)));
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
    * Whether the state is in playing.
    * @return <code>true</code> if the state is in playing state,
    *          <code>false</code> otherwise.
    */
   public boolean isPlaying() {
      return stateCategory == UserState.STATE_PLAYING;
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
    * Sets the state to {@linkplain #STATE_WAITING} and updates the internal
    * state. If the user is playing or starting a game, this method returns
    * without changing anything. 
    */
   public void waitForGame() {
      if (stateCategory == STATE_PLAYING || detailedState.isPlaying()) {
         return;
      }
      stateCategory = STATE_WAITING;
      detailedState.waitForGame();
   }

   /**
    * Advances to the next state.
    * @throws StateException if there no next step is defined.
    */
   public void next() throws StateException {
      doAction(ST_NEXT);
   }

   /**
    * Cancels the current operation.
    * @throws StateException if the cancel operation is not defined for the
    *          current state.
    */
   public void cancel() throws StateException {
      doAction(ST_CANCEL);
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

      /** idle */
      private static final int IDLE = 0;
      private static final int WAITING_FOR_GAME = 1;
      private static final int STARTING = 2;
      private static final int TURN = 3;
      private static final int NOTURN = 4;
      /** game over, game won */
      private static final int OVER_WIN = 5;
      /** game over, game lost */
      private static final int OVER_LOSS = 6;

      /**
       * Changes the state from one <code>DetailedState</code> to another
       * <code>DetailedState</code> with a string that initiates the change.
       * 
       * @param changeString the string which changes the state
       * 
       * @throws StateException if the given <code>changeString</code> does
       *         not change the state or has produced an error.
       */
      void changeState(String changeString) throws StateException {
         boolean advance = changeString.equals(ST_NEXT);
         boolean cancel = changeString.equals(ST_CANCEL);
         switch (id) {
            case WAITING_FOR_GAME :
               if (advance) {
                  setState(STARTING);
               } else if (cancel) {
                  setState(IDLE);
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
               } else { // cancel
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

      void waitForGame() {
         id = WAITING_FOR_GAME;
      }

      /**
       * Returns whether state is in playing.
       * @return <code>true</code> if currently starting a game or playing.
       */
      boolean isPlaying() {
         return id == STARTING || id == TURN || id == NOTURN;
      }

      void setState(int state) {
         id = state;
         switch (state) {
            case IDLE :
               setStateCategory(STATE_IDLE);
               clientMessage = null;
               break;

            case WAITING_FOR_GAME :
               setStateCategory(STATE_WAITING);
               clientMessage = new SimpleClientMessage("Warte auf Gegner");
               break;

            case STARTING :
               clientMessage = new SimpleClientMessage("Starte Spiel");
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

   /** @see IChangeSupport#addStateListener(StateListener) */
   public void addStateListener(StateListener listener) {
      changeSupport.addStateListener(listener);
   }

   /** @see IChangeSupport#removeStateListener(StateListener) */
   public void removeStateListener(StateListener listener) {
      changeSupport.removeStateListener(listener);
   }
   
   /**
    * Informs all state listeners of the state event.
    * @param evt the event ot be sent.
    */
   void fireStateEvent(StateEvent evt) {
      changeSupport.fireStateEvent(evt);
   }
}