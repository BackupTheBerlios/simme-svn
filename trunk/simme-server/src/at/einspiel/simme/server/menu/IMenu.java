// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: IMenu.java
//                  $Date: 2004/02/21 23:03:13 $
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;


interface IMenu {

   /** The default id for menus. The current value is <code>0</code>. */
   String DEFAULT_ID = "0";
   
   /**
    * Returns the title.
    * @return the title.
    */
   String getTitle();
   
   /**
    * Returns the id.
    * @return the id.
    */
   String getId();
   
   /**
    * Returns an xml representation. This representation is sent to the
    * client.
    * @return an xml representation.
    */
   String getXml();
   
   /**
    * Returns the options for a selection on this menu.
    * @return the possible selections on this menu.
    */
   String[] getOptions();

   /**
    * Returns the id for the corresponding selection
    * @param selection the user's selection.
    * @return the id of the menu that correspond's to the user's selection for
    *          this menu.
    */
   String getIdFor(String selection);
   
}