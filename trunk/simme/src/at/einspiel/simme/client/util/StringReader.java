package at.einspiel.simme.client.util;

import java.io.IOException;
import java.io.Reader;

/**
 * Copied from java.io.StringReader to use it for reading Strings.
 *
 * @author kariem
 */
public class StringReader extends Reader {
    private String str;
    private int length;
    private int next = 0;
    private int mark = 0;

    /**
     * Create a new string reader.
     *
     * @param s  String providing the character stream.
     */
    public StringReader(String s) {
        this.str = s;
        this.length = s.length();
    }

    /**
     * Check to make sure that the stream has not been closed 
     * @throws IOException if the stream is closed.
     */
    private void ensureOpen() throws IOException {
        if (str == null) {
            throw new IOException("Stream closed");
        }
    }

    /**
     * Read a single character.
     *
     * @return     The character read, or -1 if the end of the stream has been
     *             reached
     *
     * @exception  IOException  If an I/O error occurs
     */
    public int read() throws IOException {
        synchronized (lock) {
            ensureOpen();

            if (next >= length) {
                return -1;
            }

            return str.charAt(next++);
        }
    }

    /**
     * Read characters into a portion of an array.
     *
     * @param      cbuf  Destination buffer
     * @param      off   Offset at which to start writing characters
     * @param      len   Maximum number of characters to read
     *
     * @return     The number of characters read, or -1 if the end of the
     *             stream has been reached
     *
     * @exception  IOException  If an I/O error occurs
     */
    public int read(char[] cbuf, int off, int len) throws IOException {
        synchronized (lock) {
            ensureOpen();

            if ((off < 0) || (off > cbuf.length) || (len < 0)
                    || ((off + len) > cbuf.length) || ((off + len) < 0)) {
                throw new IndexOutOfBoundsException();
            } else if (len == 0) {
                return 0;
            }

            if (next >= length) {
                return -1;
            }

            int n = Math.min(length - next, len);
            str.getChars(next, next + n, cbuf, off);
            next += n;

            return n;
        }
    }

    /**
     * Skip characters.
     * 
     * @param ns the characters to be skipped.
     * @return the number of characters that were effectively skipped. 
     * @throws  IOException  If an I/O error occurs
     * 
     * @see java.io.StringReader#skip(long) * @return
     */
    public long skip(long ns) throws IOException {
        synchronized (lock) {
            ensureOpen();

            if (next >= length) {
                return 0;
            }

            long n = Math.min(length - next, ns);
            next += n;

            return n;
        }
    }

    /**
     * Tell whether this stream is ready to be read.
     *
     * @return True if the next read() is guaranteed not to block for input
     *
     * @exception  IOException  If the stream is closed
     */
    public boolean ready() throws IOException {
        synchronized (lock) {
            ensureOpen();

            return true;
        }
    }

    /**
     * Reset the stream to the most recent mark, or to the beginning of the
     * string if it has never been marked.
     *
     * @exception  IOException  If an I/O error occurs
     */
    public void reset() throws IOException {
        synchronized (lock) {
            ensureOpen();
            next = mark;
        }
    }

    /**
     * Close the stream.
     */
    public void close() {
        str = null;
    }
}
