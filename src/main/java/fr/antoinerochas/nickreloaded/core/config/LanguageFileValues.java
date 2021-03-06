package fr.antoinerochas.nickreloaded.core.config;

public enum LanguageFileValues
{
    DESCRIPTION_MAIN("Shows help <reload config files, check a player status>."),
    DESCRIPTION_NICK("Gives you a nickname."),
    DESCRIPTION_UNNICK("Removes your nickname."),

    DESCRIPTION_ADMNICK("Manage all the players. (unnick all, etc...)"),
    MESSAGES_COMMANDS_MAIN_TITLE("&6NickReloaded's commands:"),
    MESSAGES_COMMANDS_MAIN_RELOADING("&aReloading config file..."),
    MESSAGES_COMMANDS_MAIN_DONERELOADED("&aConfig file reloaded !"),
    MESSAGES_COMMANDS_MAIN_INCORRECTPLAYER("&c'&l%name%' &cis not a valid name !"),
    MESSAGES_COMMANDS_MAIN_INVALIDNAME("&cThis name contains invalid charcaters/is too long &7(must be < 16)"),
    MESSAGES_COMMANDS_MAIN_OFFLINEPLAYER("&c'%name%' is offline !"),

    MESSAGES_COMMANDS_MAIN_STATUS("&aPlayer '%name%' is currently nicked : %status%"),
    MESSAGES_COMMANDS_NICK_ACTIVE("&fYou are currently &cNICKED"),
    MESSAGES_COMMANDS_NICK_NONAME("&cPlease type a name !"),
    MESSAGES_COMMANDS_NICK_SUCCESS("&6Successfully nicked to &b%nick% &6with &b%skin%&6's skin !"),
    MESSAGES_COMMANDS_NICK_TOUNNICK("&7(Type '/unnick' to unnick.)"),
    MESSAGES_COMMANDS_NICK_ALREADYNICKED("&cYou are already nicked !"),

    MESSAGES_COMMANDS_NICK_OWNNAME("&cYou can't nick with your name !"),
    MESSAGES_COMMANDS_UNNICK_SUCCESS("&aSuccessfully unnicked !"),

    MESSAGES_COMMANDS_UNNICK_NOTNICKED("&cYou are not nicked !"),
    MESSAGES_COMMANDS_ADMNICK_UNNICKALLSUCCESS("&aYou unnicked all the nicked players with success."),

    MESSAGES_COMMANDS_ADMNICK_UNNICKALLFAILED("&cThere is no players to unnick !"),
    MESSAGES_COMMANDS_ARGSMISSING("&cArguments are missing !"),
    MESSAGES_COMMANDS_NOPERMISSION("&cYou can't execute this command !"),
    MESSAGES_COMMANDS_UNKNOWNCOMMAND("&cSubcommand unknown, see help: /nickreloaded !");

    public Object value;

    LanguageFileValues(Object value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return name().replace("_", ".").toLowerCase();
    }
}
