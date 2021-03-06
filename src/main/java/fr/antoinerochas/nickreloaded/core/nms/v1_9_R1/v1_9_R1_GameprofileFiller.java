package fr.antoinerochas.nickreloaded.core.nms.v1_9_R1;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import fr.antoinerochas.nickreloaded.core.nms.impl.AbstractGameprofileFiller;
import net.minecraft.server.v1_9_R2.MinecraftServer;

public class v1_9_R1_GameprofileFiller
        implements AbstractGameprofileFiller
{
    @Override
    public GameProfile fillGameprofile(GameProfile gameProfile)
    {
        try
        {
            if (gameProfile != null)
            {
                GameProfile gameProfile1 = null;
                if (gameProfile.getName() != null)
                {
                    gameProfile1 = MinecraftServer.getServer().getUserCache().getProfile(gameProfile.getName());
                }
                if (gameProfile1 == null)
                {
                    gameProfile1 = MinecraftServer.getServer().getUserCache().a(gameProfile.getId());
                }
                if (gameProfile1 == null)
                {
                    gameProfile1 = gameProfile;
                }
                if (Iterables.getFirst(gameProfile1.getProperties().get("textures"),
                                       null) == null)
                {
                    gameProfile1 = MinecraftServer.getServer().ay().fillProfileProperties(gameProfile1,
                                                                                          true);
                }
                return gameProfile1;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
