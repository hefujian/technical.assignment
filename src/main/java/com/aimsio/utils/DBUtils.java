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
            /**获取属性文件中的值**/
            String driverClassName = ConfigManager.getInstance().getString("jdbc.driver_class");
            String url = ConfigManager.getInstance().getString("jdbc.connection.url");
            String username = ConfigManager.getInstance().getString("jdbc.connection.username");
            String password =ConfigManager.getInstance().getString("jdbc.connection.password");

            /**数据库连接池对象**/
            cpds = new ComboPooledDataSource();

            /**设置数据库连接驱动**/
            cpds.setDriverClass(driverClassName);
            /**设置数据库连接地址**/
            cpds.setJdbcUrl(url);
            /**设置数据库连接用户名**/
            cpds.setUser(username);
            /**设置数据库连接密码**/
            cpds.setPassword(password);

            /**初始化时创建的连接数,应在minPoolSize与maxPoolSize之间取值.默认为3**/
            cpds.setInitialPoolSize(3);
            /**连接池中保留的最大连接数据.默认为15**/
            cpds.setMaxPoolSize(10);
            /**当连接池中的连接用完时，C3PO一次性创建新的连接数目;**/
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

    public static List<Map<String, Object>> executeQuery(String sql, Object[] bindArgs) throws SQLException {
        List<Map<String, Object>> data = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /**获取数据库连接池中的连接**/
            connection = DBUtils.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(sql);
            if (bindArgs != null) {
                /**设置sql占位符中的值**/
                for (int i = 0; i < bindArgs.length; i++) {
                    preparedStatement.setObject(i + 1, bindArgs[i]);
                }
            }
            /**执行sql语句，获取结果集**/
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

