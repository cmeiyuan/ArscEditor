package com.cmy.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by cmy on 2017/6/6
 */
public class ArscEditor {
    private RandomAccessFile randomAccessFile;

    public ArscEditor(File file) throws FileNotFoundException {
        randomAccessFile = new RandomAccessFile(file, "rw");
    }

    public final void seekTo(long pos) throws IOException {
        randomAccessFile.seek(pos);
    }

    public final void back(int n) throws IOException {
        seekTo(getCurPos() - n);
    }

    public final long getCurPos() throws IOException {
        return randomAccessFile.getFilePointer();
    }

    public final boolean readBoolean() throws IOException {
        return randomAccessFile.readBoolean();
    }

    public final byte readByte() throws IOException {
        return randomAccessFile.readByte();
    }

    public final int readByteInt() throws IOException {
        byte b = readByte();
        if (b < 0) {
            return b + 256;
        }
        return b;
    }

    public final byte[] readBytes(int n) throws IOException {
        byte[] b = new byte[n];
        randomAccessFile.read(b);
        return b;
    }

    public final short readShort() throws IOException {
        ByteBuffer bb = ByteBuffer.wrap(readBytes(2));
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getShort();
    }

    public final int readInt() throws IOException {
        ByteBuffer bb = ByteBuffer.wrap(readBytes(4));
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getInt();
    }

    public final int[] readIntArray(int length) throws IOException {
        int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = readInt();
        }
        return array;
    }

    public final String readString(int length) throws IOException {
        return new String(readBytes(length), "utf-8");
    }

    public final void writeByte(int v) throws IOException {
        randomAccessFile.writeByte(v);
    }

    public final void writeShort(int v) throws IOException {
        randomAccessFile.writeShort(v);
    }

    public final void writeInt(int v) throws IOException {
        randomAccessFile.writeInt(v);
    }

}
