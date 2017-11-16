package fr.antoinerochas.nickreloaded.listener;

import fr.antoinerochas.nickreloaded.NickReloaded;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener extends Listener<PlayerQuitEvent>
{
    public PlayerQuitListener()
    {
        super(NickReloaded.getInstance());
    }

    @Override
    @EventHandler
    public void event(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();

        //STOP TASK, UNNICK AND SAVE DATA
    }
}