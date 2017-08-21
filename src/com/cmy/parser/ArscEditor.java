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

    final void seekTo(long pos) throws IOException {
        randomAccessFile.seek(pos);
    }

    final void back(int n) throws IOException {
        seekTo(getCurPos() - n);
    }

    final long getCurPos() throws IOException {
        return randomAccessFile.getFilePointer();
    }

    final byte readByte() throws IOException {
        return randomAccessFile.readByte();
    }

    final byte[] readBytes(int n) throws IOException {
        byte[] b = new byte[n];
        randomAccessFile.read(b);
        return b;
    }

    final short readShort() throws IOException {
        ByteBuffer bb = ByteBuffer.wrap(readBytes(2));
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getShort();
    }

    final int readInt() throws IOException {
        ByteBuffer bb = ByteBuffer.wrap(readBytes(4));
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getInt();
    }

    final int[] readIntArray(int length) throws IOException {
        int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = readInt();
        }
        return array;
    }

    final void writeByte(byte v) throws IOException {
        byte[] buffer = new byte[1];
        buffer[0] = (byte) (v & 0xFF);
        writeBytes(buffer);
    }

    final void writeShort(short v) throws IOException {
        byte[] buffer = new byte[2];
        buffer[1] = (byte) ((v >> 8) & 0xFF);
        buffer[0] = (byte) (v & 0xFF);
        writeBytes(buffer);
    }

    final void writeInt(int v) throws IOException {
        byte[] buffer = new byte[4];
        buffer[3] = (byte) ((v >> 24) & 0xFF);
        buffer[2] = (byte) ((v >> 16) & 0xFF);
        buffer[1] = (byte) ((v >> 8) & 0xFF);
        buffer[0] = (byte) (v & 0xFF);
        writeBytes(buffer);
    }

    final void writeBytes(byte[] v) throws IOException {
        randomAccessFile.write(v);
    }

}
