package net.uku3lig.genderplugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

public final class GenderPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("Enabling GenderPlugin");

        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.CUSTOM_PAYLOAD) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                StructureModifier<Object> m = event.getPacket().getModifier();
                MinecraftKey c = (MinecraftKey) m.readSafely(0);
                if (!c.toString().equalsIgnoreCase("wildfire_gender:send_gender_info")) return;
                if (m.readSafely(1) instanceof ByteBuf buf) {
                    GenderPlayer p = parsePlayer(buf);
                    PacketContainer container = manager.createPacket(PacketType.Play.Server.CUSTOM_PAYLOAD, false);
                    container.getModifier().write(0, new MinecraftKey("wildfire_gender", "sync"));
                    container.getModifier().write(1, new PacketDataSerializer(exportPlayer(p)));
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        try {
                            manager.sendServerPacket(player, container);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private static GenderPlayer parsePlayer(ByteBuf buf) {
        GenderPlayer p = new GenderPlayer();

        p.setUuid(Util.readUUID(buf));
        p.setGender(buf.readInt());
        p.setBustSize(buf.readFloat());
        p.setHurtSounds(buf.readBoolean());
        p.setBreastPhysics(buf.readBoolean());
        p.setBreastPhysicsInArmor(buf.readBoolean());
        p.setBreastOffsetX(buf.readFloat());
        p.setBreastOffsetY(buf.readFloat());
        p.setBreastOffsetZ(buf.readFloat());
        p.setBreastCleavage(buf.readFloat());
        p.setDualPhysics(buf.readBoolean());
        p.setBounceMultiplier(buf.readFloat());
        p.setFloppyMultiplier(buf.readFloat());

        return p;
    }

    private static ByteBuf exportPlayer(GenderPlayer p) {
        ByteBuf buf = Unpooled.buffer();

        Util.writeUUID(p.getUuid(), buf);
        buf.writeInt(p.getGender());
        buf.writeFloat(p.getBustSize());
        buf.writeBoolean(p.isHurtSounds());
        buf.writeBoolean(p.isBreastPhysics());
        buf.writeBoolean(p.isBreastPhysicsInArmor());
        buf.writeBoolean(p.isShowInArmor());
        buf.writeFloat(p.getBreastOffsetX());
        buf.writeFloat(p.getBreastOffsetY());
        buf.writeFloat(p.getBreastOffsetZ());
        buf.writeFloat(p.getBreastCleavage());
        buf.writeBoolean(p.isDualPhysics());
        buf.writeFloat(p.getBounceMultiplier());
        buf.writeFloat(p.getFloppyMultiplier());

        return buf;
    }
}
