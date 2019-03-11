package com.aimsio.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import java.beans.PropertyVetoException;
import java.sql.*;
import java.util.*;

public class DBUtils {
    private static volatile DBUtils dbConnection;
    private ComboPooledDataSource cpds;

    private DBUtils() {
        try {
            /**Get db config**/
            String driverClassName = ConfigManager.getInstance().getString("jdbc.driver_class");
            String url = ConfigManager.getInstance().getString("jdbc.connection.url");
            String username = ConfigManager.getInstance().getString("jdbc.connection.username");
            String password =ConfigManager.getInstance().getString("jdbc.connection.password");


            cpds = new ComboPooledDataSource();
            cpds.setDriverClass(driverClassName);
            cpds.setJdbcUrl(url);
            cpds.setUser(username);
            cpds.setPassword(password);

            cpds.setInitialPoolSize(3);
            cpds.setMaxPoolSize(10);
            cpds.setAcquireIncrement(1);
            cpds.setIdleConnectionTestPeriod(60);
            cpds.setMaxIdleTime(3000);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }


    public static DBUtils getInstance() {
        if (dbConnection == null) {
            synchronized (DBUtils.class) {
                if (dbConnection == null) {
                    dbConnection = new DBUtils();
                }
            }
        }
        return dbConnection;
    }


    public final synchronized Connection getConnection() throws SQLException {
        return cpds.getConnection();
    }


    protected void finalize() throws Throwable {
        DataSources.destroy(cpds);
        super.finalize();
    }

    public static List<Map<String, Object>> executeQuery(String sql) throws SQLException {
        return executeQuery(sql,null);
    }

    public static List<Map<String, Object>> executeQuery(String sql, Object... bindArgs) throws SQLException {
        List<Map<String, Object>> data = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /**Get connection from the pool**/
            connection = DBUtils.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(sql);
            if (bindArgs != null) {
                for (int i = 0; i < bindArgs.length; i++) {
                    preparedStatement.setObject(i + 1, bindArgs[i]);
                }
            }
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            HashMap<String,Object> hashMap=null;
            while(resultSet.next()){
                hashMap=new HashMap<String, Object>();
                for(int i=0;i<columnCount;i++){
                    hashMap.put((rsmd.getColumnLabel(i+1)).toLowerCase(),resultSet.getObject(i+1));
                }
                data.add(hashMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return data;
    }



}

