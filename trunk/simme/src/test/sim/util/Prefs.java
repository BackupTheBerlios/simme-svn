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
 * @author kariem
 *
 */
public class Prefs {

   static byte INITIAL_SIZE = 10;
   static byte GROWTH_FACTOR = 5;

   RecordStore rs;
   RecordEnumeration enum;
   String name;
   byte numberOfRecords;
   byte[] data;
   DataInputStream dis;
   DataOutputStream dos;
   ByteArrayOutputStream baos;

   public Prefs(String name, byte nbRecords) {
      setName(name);
      setNumberOfRecords(nbRecords);
      data = new byte[INITIAL_SIZE];
      dis = new DataInputStream(new ByteArrayInputStream(data));
      baos = new ByteArrayOutputStream();
      dos = new DataOutputStream(baos);
   }

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
      System.out.println("------------ opened ------------");
   }

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
		System.out.println("------------ closed ------------");
   }

   public boolean hasNext() {
      return enum.hasNextElement();
   }

   public boolean hasPrevious() {
      return enum.hasPreviousElement();
   }
   
   public int currentSize() throws PrefsException {
      try {
         return rs.getNumRecords();
      } catch (RecordStoreNotOpenException e) {
         throw new PrefsException(e.getMessage());
      }
   }

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

	/*
   public String readNextString() throws PrefsException {
      loadNext();
      try {
			dis.reset();
         String s = dis.readUTF();
         System.out.println("read: " + s);
         return s;
      } catch (IOException e) {
         e.printStackTrace();
         throw new PrefsException(e.getMessage());
      }
   }

	private int loadNext() throws PrefsException {
		try {
			int id = enum.nextRecordId();
			System.out.println("loading (id=" + id +")");
			int len = rs.getRecordSize(id);
			if (len > data.length) {
				data = new byte[len + GROWTH_FACTOR];
			}
			return rs.getRecord(id, data, 0);
		} catch (InvalidRecordIDException e) {
			e.printStackTrace();
			throw new PrefsException(e.getMessage());
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
			throw new PrefsException(e.getMessage());
		} catch (RecordStoreException e) {
			e.printStackTrace();
			throw new PrefsException(e.getMessage());
		}
	}
	*/

   public void writeNext(byte[] b) throws PrefsException {
      try {
         // go through recordset and change information
         int id = enum.nextRecordId();
         System.out.println("writing next (id=" + id + ")");
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
   
   public void writeNext() throws PrefsException {
		writeNext(baos.toByteArray());
		baos.reset();   	
   }
   
	public void addNext(byte[] b) throws PrefsException {
		try {
			System.out.println("adding next (id=" + rs.addRecord(b, 0, b.length) + ")");
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
	
	public void addNext() throws PrefsException {
		addNext(baos.toByteArray());
		baos.reset();		
	}


	/*
   public void writeNext(String s) throws PrefsException {
   	writeToBuffer(s);
      writeNext(baos.toByteArray());
      baos.reset();
   }

	public void addNext(String s) throws PrefsException {
		writeToBuffer(s);
		addNext(baos.toByteArray());
		baos.reset();
	}
	*/

	void writeToBuffer(String s) throws PrefsException{
		try {
			dos.writeUTF(s);
		} catch (IOException e) {
			throw new PrefsException(e.getMessage());
		}
		System.out.println("Writing \"" + s + "\"");
	}



   void resetEnum() {
      System.out.println("resetting enum");
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
    * @return String
    */
   public String getName() {
      return name;
   }

   /**
    * Sets the name.
    * @param name The name to set
    */
   public void setName(String name) {
      this.name = name;
   }

   /**
    * @return byte
    */
   public byte getNumberOfRecords() {
      return numberOfRecords;
   }

   /**
    * Sets the numberOfRecords.
    * @param numberOfRecords The numberOfRecords to set
    */
   public void setNumberOfRecords(byte numberOfRecords) {
      this.numberOfRecords = numberOfRecords;
   }

}
