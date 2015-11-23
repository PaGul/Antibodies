/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author pavel
 */
public class MyReader {

    private BufferedInputStream in;

    private final int bufSize = 1 << 16;
    private final byte b[] = new byte[bufSize];

    public MyReader(InputStream in) {
        this.in = new BufferedInputStream(in, bufSize);
    }

    public int nextInt() throws IOException {
        int c;
        while ((c = nextChar()) <= 32)
				;
        int x = 0, sign = 1;
        if (c == '-') {
            sign = -1;
            c = nextChar();
        }
        while (c >= '0') {
            x = x * 10 + (c - '0');
            c = nextChar();
        }
        return x * sign;
    }

    private StringBuilder _buf = new StringBuilder();

    public String nextWord() throws IOException {
        int c;
        _buf.setLength(0);
        while ((c = nextChar()) <= 32 && c != -1)
				;
        if (c == -1) {
            return null;
        }
        while (c > 32) {
            _buf.append((char) c);
            c = nextChar();
        }
        return _buf.toString();
    }
    
    public String nextString() throws IOException {
        int c;
        _buf.setLength(0);
        while ((c = nextChar()) <= 32 && c != -1)
				;
        if (c == -1) {
            return null;
        }
        while (c != '\n') {
            _buf.append((char) c);
            c = nextChar();
        }
        return _buf.toString();
    }
    
    private int bn = bufSize, k = bufSize;

    public int nextChar() throws IOException {
        if (bn == k) {
            k = in.read(b, 0, bufSize);
            bn = 0;
        }
        return bn >= k ? -1 : b[bn++];
    }

    public int nextNotSpace() throws IOException {
        int ch;
        while ((ch = nextChar()) <= 32 && ch != -1)
				;
        return ch;
    }
}