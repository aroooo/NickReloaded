package fr.antoinerochas.nickreloaded.core.string;

import fr.antoinerochas.nickreloaded.core.config.ConfigFileValues;
import fr.antoinerochas.nickreloaded.core.manager.StorageManager;

public class MessageBuilder
{
    private String message;

    public MessageBuilder(String message)
    {
        this.message = message;
    }

    public String prefix()
    {
        return StorageManager.getInstance().getConfigFile().getString(ConfigFileValues.PREFIX.getValue());
    }

    public String errorPrefix() { return prefix() + StorageManager.getInstance().getConfigFile().getString(ConfigFileValues.ERROR_PREFIX.getValue()); }

    public String buildError()
    {
        return errorPrefix() + message;
    }

    public String buildNormal()
    {
        return prefix() + message;
    }

    public String buildSuccess()
    {
        return prefix() + "§a" + message;
    }
}
