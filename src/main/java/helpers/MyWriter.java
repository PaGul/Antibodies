/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author pavel
 */
public class MyWriter implements AutoCloseable {

    BufferedOutputStream out;

    private final int bufSize = 1 << 16;
    private int n;
    private byte b[] = new byte[bufSize];

    public MyWriter(OutputStream out) {
        this.out = new BufferedOutputStream(out, bufSize);
        this.n = 0;
    }

    private byte c[] = new byte[20];

    public void print(int x) throws IOException {
        int cn = 0;
        if (n + 20 >= bufSize) {
            flush();
        }
        if (x < 0) {
            b[n++] = (byte) ('-');
            x = -x;
        }
        while (cn == 0 || x != 0) {
            c[cn++] = (byte) (x % 10 + '0');
            x /= 10;
        }
        while (cn-- > 0) {
            b[n++] = c[cn];
        }
    }

    public void print(char x) throws IOException {
        if (n == bufSize) {
            flush();
        }
        b[n++] = (byte) x;
    }

    public void print(String s) throws IOException {
        for (int i = 0; i < s.length(); i++) {
            print(s.charAt(i));
        }
    }

    public void println(String s) throws IOException {
        print(s);
        println();
    }

    private static final String newLine = System.getProperty("line.separator");

    public void println() throws IOException {
        print(newLine);
    }

    public void flush() throws IOException {
        out.write(b, 0, n);
        n = 0;
    }

    public void close() throws IOException {
        flush();
        out.close();
    }
}