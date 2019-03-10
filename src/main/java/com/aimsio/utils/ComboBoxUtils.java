package com.aimsio.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class ComboBoxUtils {
    public static List<String> getAllAssetUN() throws SQLException {
        List<String> result = new ArrayList<>();
        List<Map<String, Object>> assetUNList = DBUtils.executeQuery("SELECT \tDISTINCT `AssetUN` FROM `assets`.`signal` ");
        ListIterator<Map<String, Object>> iterator =assetUNList.listIterator();
        while (iterator.hasNext()){
            result.add(iterator.next().get("assetun").toString());
        }
        return result;
    }

    public static List<String> getStatus() throws SQLException {
        List<String> result = new ArrayList<>();
        List<Map<String, Object>> assetUNList = DBUtils.executeQuery("SELECT \tDISTINCT `status` FROM `assets`.`signal` ");
        ListIterator<Map<String, Object>> iterator =assetUNList.listIterator();
        while (iterator.hasNext()){
            result.add(iterator.next().get("status").toString());
        }
        return result;
    }
}
