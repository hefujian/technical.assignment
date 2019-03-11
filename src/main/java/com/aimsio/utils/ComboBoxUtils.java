package com.aimsio.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class ComboBoxUtils {
    public static List<String> getAssetUN() throws SQLException {
        return getAssetUN("");
    }
    public static List<String> getAssetUN(String status) throws SQLException {
        List<String> result = new ArrayList<>();
        List<Map<String, Object>> assetUNList = new ArrayList<>();
        String sql = "SELECT DISTINCT `AssetUN` FROM `assets`.`signal`";
        if(status!=null && status!=""){
            sql = "SELECT DISTINCT `AssetUN` FROM `assets`.`signal` WHERE STATUS =?";
            assetUNList = DBUtils.executeQuery(sql, status);
        }else{
            assetUNList = DBUtils.executeQuery(sql);
        }
        ListIterator<Map<String, Object>> iterator =assetUNList.listIterator();
        while (iterator.hasNext()){
            result.add(iterator.next().get("assetun").toString());
        }
        return result;
    }

    public static List<String> getStatus(String assetUN) throws SQLException {
        List<String> result = new ArrayList<>();
        List<Map<String, Object>> assetUNList = new ArrayList<>();
        String sql = "SELECT DISTINCT `status` FROM `assets`.`signal`";
        if(assetUN!=null && assetUN!=""){
            sql = "SELECT DISTINCT `status` FROM `assets`.`signal` WHERE `AssetUN`=?";
            assetUNList = DBUtils.executeQuery(sql,assetUN);
        }else{
            assetUNList = DBUtils.executeQuery(sql);
        }

        ListIterator<Map<String, Object>> iterator =assetUNList.listIterator();
        while (iterator.hasNext()){
            result.add(iterator.next().get("status").toString());
        }
        return result;
    }

    public static List<String> getStatus() throws SQLException {
        return getStatus("");
    }
}
