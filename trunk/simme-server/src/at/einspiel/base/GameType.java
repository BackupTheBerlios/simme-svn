// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: GameType.java
//                  $Date: 2003/12/29 07:24:27 $
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package at.einspiel.base;

/**
 * Different game types for different games. This class only holds some
 * information on the game itself (title, description). 
 * 
 * @author kariem
 */
public class GameType {

   private String title;
   private String description;
   

   /**
    * Creates a new instance of <code>GameType</code>.
    * @param title the title.
    * @param description the description.
    */
   public GameType(String title, String description) {
      super();
      this.title = title;
      this.description = description;
   }

   /**
    * @return Returns the description.
    */
   public String getDescription() {
      return description;
   }
   /**
    * @param description The description to set.
    */
   public void setDescription(String description) {
      this.description = description;
   }
   /**
    * @return Returns the title.
    */
   public String getTitle() {
      return title;
   }
   /**
    * @param title The title to set.
    */
   public void setTitle(String title) {
      this.title = title;
   }
}
