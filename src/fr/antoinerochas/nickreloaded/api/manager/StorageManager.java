package fr.antoinerochas.nickreloaded.api.manager;

import fr.antoinerochas.nickreloaded.api.config.Config;
import fr.antoinerochas.nickreloaded.api.config.FileProvider;
import fr.antoinerochas.nickreloaded.api.logger.NickReloadedLogger;
import fr.antoinerochas.nickreloaded.api.storage.core.CacheStorageMode;
import fr.antoinerochas.nickreloaded.api.storage.core.DatabaseImpl;
import fr.antoinerochas.nickreloaded.api.storage.core.DatabaseStorageMode;
import fr.antoinerochas.nickreloaded.api.storage.core.RequestHandler;
import fr.antoinerochas.nickreloaded.api.storage.mysql.MySQLDatabase;
import fr.antoinerochas.nickreloaded.api.storage.mysql.Table;
import fr.antoinerochas.nickreloaded.api.storage.redisson.RedisCrendentials;
import fr.antoinerochas.nickreloaded.api.storage.redisson.RedisManager;
import fr.antoinerochas.nickreloaded.api.storage.sqlite.SQLiteDatabase;

public class StorageManager
{
    private FileProvider configFile;
    private FileProvider langFile;
    private DatabaseImpl database;
    private RedisManager redis;
    private Table table, random;

    public static StorageManager getInstance()
    {
        return new StorageManager();
    }

    public void setupStorage()
    {
        NickReloadedLogger.log(NickReloadedLogger.Level.INFO, "Loading data...");

        configFile = new FileProvider("config.yml");

        langFile = new FileProvider("lang.yml");

        detectStorage();

        String ip = configFile.getString(Config.STORAGE_MYSQL_IP.getValue()), username = configFile.getString(Config.STORAGE_MYSQL_USER.getValue()), password = configFile.getString(Config.STORAGE_MYSQL_PASSWORD.getValue()), database_name = configFile.getString(Config.STORAGE_TABLE_NAME.getValue()), random_name = configFile.getString(Config.STORAGE_TABLE_RANDOMNAME.getValue());
        int port = configFile.getFileConfiguration().getInt(Config.STORAGE_MYSQL_PORT.getValue());

        String redisIP = configFile.getString(Config.STORAGE_REDIS_IP.getValue()), redisPassword = configFile.getString(Config.STORAGE_REDIS_PASSWORD.getValue());
        int redisPort = configFile.getFileConfiguration().getInt(Config.STORAGE_REDIS_PORT.getValue());

        RequestHandler requestHandler = new RequestHandler(database);

        if(CacheStorageMode.isMode(CacheStorageMode.PLUGIN_MESSAGE))
        {
            //SOCKETS
        }
        else
        {
            redis = new RedisManager(new RedisCrendentials(redisIP, redisPassword, redisPort));
        }

        if (DatabaseStorageMode.isMode(DatabaseStorageMode.SQLITE))
        {
            database = new SQLiteDatabase(configFile.getString(Config.STORAGE_SQLITE_FILENAME.getValue()));

            requestHandler.executeUpdate("CREATE TABLE IF NOT EXISTS `" + configFile.getString(Config.STORAGE_TABLE_NAME.getValue()) + "` (" + "`UUID` TEXT UNIQUE, " + " `NICKED` INTEGER, " + " `NAME` TEXT, " + " `SKIN` TEXT " + ");");

            requestHandler.executeUpdate("CREATE TABLE IF NOT EXISTS `" + configFile.getString(Config.STORAGE_TABLE_RANDOMNAME.getValue()) + "` (" + "`NAME` TEXT" + ");");
        }
        else
        {
            database = new MySQLDatabase(ip,
                                         port,
                                         username,
                                         password,
                                         database_name);

            requestHandler.executeUpdate("CREATE TABLE IF NOT EXISTS `" + configFile.getString(Config.STORAGE_TABLE_NAME.getValue()) + "` (`UUID` VARCHAR(255), `NICKED` tinyint(1), `NAME` VARCHAR(16), `SKIN` VARCHAR(16), UNIQUE (uuid));");

            requestHandler.executeUpdate("CREATE TABLE IF NOT EXISTS `" + configFile.getString(Config.STORAGE_TABLE_RANDOMNAME.getValue()) + "` " + "(`NAME` VARCHAR(16));");
        }

        database.connect();

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

    public FileProvider getConfigFile()
    {
        return configFile;
    }

    public FileProvider getLangFile()
    {
        return langFile;
    }

    private void detectStorage()
    {
        if (! configFile.getFileConfiguration().getBoolean(Config.STORAGE_COMMON_MYSQL.getValue()))
        {
            DatabaseStorageMode.setMode(DatabaseStorageMode.SQLITE);
        }
        else
        {
            DatabaseStorageMode.setMode(DatabaseStorageMode.MYSQL);
        }

        if(! configFile.getFileConfiguration().getBoolean(Config.STORAGE_COMMON_REDIS.getValue()))
        {
            CacheStorageMode.setMode(CacheStorageMode.PLUGIN_MESSAGE);
        }
        else
        {
            CacheStorageMode.setMode(CacheStorageMode.REDIS);
        }
    }
}