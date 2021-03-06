package fr.antoinerochas.nickreloaded.core.storage.sqlite;

import fr.antoinerochas.nickreloaded.core.storage.core.DatabaseImpl;
import fr.antoinerochas.nickreloaded.core.storage.mysql.Table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDatabase
        implements DatabaseImpl
{
    private Connection connection;
    private String host;

    private boolean connected;

    public SQLiteDatabase(final String database)
    {
        this.host = "jdbc:sqlite:./plugins/NickReloaded/" + database + ".db";
    }

    @Override
    public Table getTable(String name)
    {
        return new Table(this,
                         name);
    }

    @Override
    public void connect()
    {
        if (connected)
        {
            return;
        }

        try
        {
            Class.forName("org.sqlite.JDBC").newInstance();

            this.connection = DriverManager.getConnection(host);
            this.connected = true;
        }
        catch (SQLException | IllegalAccessException | InstantiationException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void close()
    {
        if (! connected)
        {
            return;
        }

        try
        {
            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection()
    {
        return connection;
    }

    @Override
    public void setConnection(Connection connection)
    {
        this.connection = connection;
    }

    @Override
    public boolean isConnected()
    {
        return connected;
    }
}
