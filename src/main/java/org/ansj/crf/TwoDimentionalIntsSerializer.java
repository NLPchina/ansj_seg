package org.ansj.crf;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * by zhl on 15/7/21.
 */
public class TwoDimentionalIntsSerializer extends Serializer<int[][]> {

    @Override
    public void write(final Kryo kryo, final Output output, final int[][] object) {
        output.writeInt(object.length);
        for (final int[] ints : object) {
            output.writeInt(ints.length);
            output.writeInts(ints);
        }
    }

    @Override
    public int[][] read(final Kryo kryo, final Input input, final Class<int[][]> type) {
        final int[][] object = new int[input.readInt()][];
        for (int i = 0; i < object.length; i++) {
            object[i] = input.readInts(input.readInt());
        }
        return object;
    }
}
