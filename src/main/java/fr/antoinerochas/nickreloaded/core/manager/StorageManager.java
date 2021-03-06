package fr.antoinerochas.nickreloaded.core.manager;

import fr.antoinerochas.nickreloaded.core.config.ConfigFileValues;
import fr.antoinerochas.nickreloaded.core.config.ConfigFileProvider;
import fr.antoinerochas.nickreloaded.core.config.LanguageFileProvider;
import fr.antoinerochas.nickreloaded.core.logger.NRLogger;
import fr.antoinerochas.nickreloaded.core.storage.core.CacheStorageMode;
import fr.antoinerochas.nickreloaded.core.storage.core.DatabaseImpl;
import fr.antoinerochas.nickreloaded.core.storage.core.DatabaseStorageMode;
import fr.antoinerochas.nickreloaded.core.storage.core.RequestHandler;
import fr.antoinerochas.nickreloaded.core.storage.mysql.MySQLDatabase;
import fr.antoinerochas.nickreloaded.core.storage.mysql.Table;
import fr.antoinerochas.nickreloaded.core.storage.redisson.RedisCrendentials;
import fr.antoinerochas.nickreloaded.core.storage.redisson.RedisManager;
import fr.antoinerochas.nickreloaded.core.storage.sqlite.SQLiteDatabase;

public class StorageManager
{
    private ConfigFileProvider configFile;
    private LanguageFileProvider langFile;
    private DatabaseImpl database;
    private RedisManager redis;
    private Table table, random;

    public static StorageManager getInstance()
    {
        return new StorageManager();
    }

    public void setupStorage()
    {
        NRLogger.log(NRLogger.NRLPrefix.INFO, "§aLoading data...");

        configFile = new ConfigFileProvider("config.yml");

        langFile = new LanguageFileProvider("lang.yml");

        detectStorage();

        String ip = configFile.getString(ConfigFileValues.STORAGE_MYSQL_IP.getValue()), username = configFile.getString(ConfigFileValues.STORAGE_MYSQL_USER.getValue()), password = configFile.getString(ConfigFileValues.STORAGE_MYSQL_PASSWORD.getValue()), database_name = configFile.getString(ConfigFileValues.STORAGE_TABLE_NAME.getValue()), random_name = configFile.getString(ConfigFileValues.STORAGE_TABLE_RANDOMNAME.getValue());
        int port = configFile.getFileConfiguration().getInt(ConfigFileValues.STORAGE_MYSQL_PORT.getValue());

        String redisIP = configFile.getString(ConfigFileValues.STORAGE_REDIS_IP.getValue()), redisPassword = configFile.getString(ConfigFileValues.STORAGE_REDIS_PASSWORD.getValue());
        int redisPort = configFile.getFileConfiguration().getInt(ConfigFileValues.STORAGE_REDIS_PORT.getValue());

        if(CacheStorageMode.isMode(CacheStorageMode.SOCKETS))
        {
            //SOCKETS
        }
        else
        {
            redis = new RedisManager(new RedisCrendentials(redisIP, redisPassword, redisPort));
        }

        if (DatabaseStorageMode.isMode(DatabaseStorageMode.SQLITE))
        {
            database = new SQLiteDatabase(configFile.getString(ConfigFileValues.STORAGE_SQLITE_FILENAME.getValue()));
        }
        else
        {
            database = new MySQLDatabase(ip,
                                         port,
                                         username,
                                         password,
                                         database_name);
        }

        database.connect();

        RequestHandler requestHandler = new RequestHandler(database);

        if (DatabaseStorageMode.isMode(DatabaseStorageMode.SQLITE))
        {
            requestHandler.executeUpdate("CREATE TABLE IF NOT EXISTS `" + configFile.getString(ConfigFileValues.STORAGE_TABLE_NAME.getValue()) + "` (" + "`UUID` TEXT UNIQUE, " + " `NICKED` INTEGER, " + " `NAME` TEXT, " + " `SKIN` TEXT " + ");");

            requestHandler.executeUpdate("CREATE TABLE IF NOT EXISTS `" + configFile.getString(ConfigFileValues.STORAGE_TABLE_RANDOMNAME.getValue()) + "` (" + "`NAME` TEXT" + ");");
        }
        else
        {
            requestHandler.executeUpdate("CREATE TABLE IF NOT EXISTS `" + configFile.getString(ConfigFileValues.STORAGE_TABLE_NAME.getValue()) + "` (`UUID` VARCHAR(255), `NICKED` tinyint(1), `NAME` VARCHAR(16), `SKIN` VARCHAR(16), UNIQUE (uuid));");

            requestHandler.executeUpdate("CREATE TABLE IF NOT EXISTS `" + configFile.getString(ConfigFileValues.STORAGE_TABLE_RANDOMNAME.getValue()) + "` " + "(`NAME` VARCHAR(16));");
        }

        table = database.getTable(database_name);
        random = database.getTable(random_name);
    }

    public Table getMainTable()
    {
        return table;
    }

    public Table getRandomNamesTable()
    {
        return random;
    }

    public ConfigFileProvider getConfigFile()
    {
        return configFile;
    }

    public LanguageFileProvider getLangFile()
    {
        return langFile;
    }

    private void detectStorage()
    {
        if (! configFile.getFileConfiguration().getBoolean(ConfigFileValues.STORAGE_COMMON_MYSQL.getValue()))
        {
            DatabaseStorageMode.setMode(DatabaseStorageMode.SQLITE);
        }
        else
        {
            DatabaseStorageMode.setMode(DatabaseStorageMode.MYSQL);
        }

        if(! configFile.getFileConfiguration().getBoolean(ConfigFileValues.STORAGE_COMMON_REDIS.getValue()))
        {
            CacheStorageMode.setMode(CacheStorageMode.SOCKETS);
        }
        else
        {
            CacheStorageMode.setMode(CacheStorageMode.REDIS);
        }
    }
}
