package test.sim.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;


/**
 * DOCUMENT ME!
 *
 * @author kariem
 */
public class Prefs {

   static final byte INITIAL_SIZE = 10;
   static final byte GROWTH_FACTOR = 5;


   RecordStore rs;
   RecordEnumeration enum;
   String name;
   byte numberOfRecords;
   byte[] data;
   DataInputStream dis;
   DataOutputStream dos;
   ByteArrayOutputStream baos;


   /**
    * Creates a new Prefs object.
    *
    * @param name DOCUMENT ME!
    * @param nbRecords DOCUMENT ME!
    */
   public Prefs(String name, byte nbRecords) {
      setName(name);
      setNumberOfRecords(nbRecords);
      data = new byte[INITIAL_SIZE];
      dis = new DataInputStream(new ByteArrayInputStream(data));
      baos = new ByteArrayOutputStream();
      dos = new DataOutputStream(baos);
   }


   /**
    * DOCUMENT ME!
    *
    * @throws PrefsException DOCUMENT ME!
    */
   public void open() throws PrefsException {
      try {
         rs = RecordStore.openRecordStore("user", true);
         enum = rs.enumerateRecords(null, null, false);
      } catch (RecordStoreFullException e) {
         throw new PrefsException(e.getMessage());
      } catch (RecordStoreNotFoundException e) {
         throw new PrefsException(e.getMessage());
      } catch (RecordStoreException e) {
         throw new PrefsException(e.getMessage());
      }
   }

   /**
    * DOCUMENT ME!
    *
    * @throws PrefsException DOCUMENT ME!
    */
   public void close() throws PrefsException {
      if (rs != null) {
         try {
            rs.closeRecordStore();
         } catch (RecordStoreNotOpenException e1) {
            throw new PrefsException(e1.getMessage());
         } catch (RecordStoreException e1) {
            throw new PrefsException(e1.getMessage());
         }
      }
   }

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public boolean hasNext() {
      return enum.hasNextElement();
   }

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public boolean hasPrevious() {
      return enum.hasPreviousElement();
   }

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws PrefsException DOCUMENT ME!
    */
   public int currentSize() throws PrefsException {
      try {
         return rs.getNumRecords();
      } catch (RecordStoreNotOpenException e) {
         throw new PrefsException(e.getMessage());
      }
   }

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws PrefsException DOCUMENT ME!
    */
   public byte[] readNext() throws PrefsException {
      try {
         return enum.nextRecord();
      } catch (InvalidRecordIDException e) {
         throw new PrefsException(e.getMessage());
      } catch (RecordStoreNotOpenException e) {
         throw new PrefsException(e.getMessage());
      } catch (RecordStoreException e) {
         throw new PrefsException(e.getMessage());
      }
   }

   /**
    * DOCUMENT ME!
    *
    * @param b DOCUMENT ME!
    *
    * @throws PrefsException DOCUMENT ME!
    */
   public void writeNext(byte[] b) throws PrefsException {
      try {
         // go through recordset and change information
         int id = enum.nextRecordId();
         rs.setRecord(id, b, 0, b.length);
      } catch (RecordStoreNotOpenException e) {
         throw new PrefsException(e.getMessage());
      } catch (InvalidRecordIDException e) {
         throw new PrefsException(e.getMessage());
      } catch (RecordStoreFullException e) {
         throw new PrefsException(e.getMessage());
      } catch (RecordStoreException e) {
         throw new PrefsException(e.getMessage());
      }
   }

   /**
    * DOCUMENT ME!
    *
    * @throws PrefsException DOCUMENT ME!
    */
   public void writeNext() throws PrefsException {
      writeNext(baos.toByteArray());
      baos.reset();
   }

   /**
    * DOCUMENT ME!
    *
    * @param b DOCUMENT ME!
    *
    * @throws PrefsException DOCUMENT ME!
    */
   public void addNext(byte[] b) throws PrefsException {
      try {
         rs.addRecord(b, 0, b.length);
      } catch (RecordStoreNotOpenException e) {
         throw new PrefsException(e.getMessage());
      } catch (InvalidRecordIDException e) {
         throw new PrefsException(e.getMessage());
      } catch (RecordStoreFullException e) {
         throw new PrefsException(e.getMessage());
      } catch (RecordStoreException e) {
         throw new PrefsException(e.getMessage());
      }
   }

   /**
    * DOCUMENT ME!
    *
    * @throws PrefsException DOCUMENT ME!
    */
   public void addNext() throws PrefsException {
      addNext(baos.toByteArray());
      baos.reset();
   }

   void writeToBuffer(String s) throws PrefsException {
      try {
         dos.writeUTF(s);
      } catch (IOException e) {
         throw new PrefsException(e.getMessage());
      }
   }

   void resetEnum() {
      enum.rebuild();
      enum.reset();
   }

   /*
   public byte[] readPrevious() throws PrefsException {
      try {
         return enum.previousRecord();
      } catch (InvalidRecordIDException e) {
         throw new PrefsException(e.getMessage());
      } catch (RecordStoreNotOpenException e) {
         throw new PrefsException(e.getMessage());
      } catch (RecordStoreException e) {
         throw new PrefsException(e.getMessage());
      }
   }
   */

   /**
    * DOCUMENT ME!
    *
    * @return String
    */
   public String getName() {
      return name;
   }

   /**
    * Sets the name.
    *
    * @param name The name to set
    */
   public void setName(String name) {
      this.name = name;
   }

   /**
    * DOCUMENT ME!
    *
    * @return byte
    */
   public byte getNumberOfRecords() {
      return numberOfRecords;
   }

   /**
    * Sets the numberOfRecords.
    *
    * @param numberOfRecords The numberOfRecords to set
    */
   public void setNumberOfRecords(byte numberOfRecords) {
      this.numberOfRecords = numberOfRecords;
   }
}
