package net.uku3lig.genderplugin;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

// me, steal code from minecraft? I would NEVER do such a thing
// please don't sue me microsoft
public class Util {
    public static UUID readUUID(ByteBuf buf) {
        int i = readVarInt(buf);
        String string = buf.toString(buf.readerIndex(), i, StandardCharsets.UTF_8);
        buf.readerIndex(buf.readerIndex() + i);
        return UUID.fromString(string);
    }

    public static void writeUUID(UUID uuid, ByteBuf buf) {
        byte[] bs = uuid.toString().getBytes(StandardCharsets.UTF_8);
        writeVarInt(bs.length, buf);
        buf.writeBytes(bs);
    }

    public static int readVarInt(ByteBuf buf) {
        int i = 0;
        int j = 0;

        byte b;
        do {
            b = buf.readByte();
            i |= (b & 127) << j++ * 7;
            if (j > 5) {
                throw new IllegalArgumentException("VarInt too big");
            }
        } while ((b & 128) == 128);

        return i;
    }

    public static void writeVarInt(int value, ByteBuf buf) {
        while ((value & -128) != 0) {
            buf.writeByte(value & 127 | 128);
            value >>>= 7;
        }

        buf.writeByte(value);
    }

    private Util() {
    }
}
