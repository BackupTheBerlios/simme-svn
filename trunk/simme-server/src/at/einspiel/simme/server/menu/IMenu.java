// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: IMenu.java
//                  $Date: 2003/12/30 23:04:47 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;


interface IMenu {
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
}