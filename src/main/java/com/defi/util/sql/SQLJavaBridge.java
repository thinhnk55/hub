package com.defi.util.sql;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.defi.util.json.GsonUtil;
import com.defi.util.log.DebugLogger;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SQLJavaBridge {
    public HikariClient sqlClient;

    public SQLJavaBridge(HikariClient sqlClient) {
        this.sqlClient = sqlClient;
    }

    public Integer queryInteger(String query, Object... params) throws Exception {
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        Exception exception = null;
        Integer value = null;
        try {
            connection = sqlClient.getConnection();
            st = buildQueryStatement(connection, query, params);
            rs = st.executeQuery();
            if (rs.next()) {
                value = rs.getInt(1);
            }
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < params.length; i++) {
                if (i != params.length - 1) {
                    sb.append(params[i]).append(",");
                } else {
                    sb.append(params[i]);
                }
            }
            DebugLogger.logger.error("query error {} params {}", query, sb.toString());
            exception = e;
        } finally {
            sqlClient.releaseConnection(connection, st, rs);
            if (exception != null) {
                throw exception;
            }
        }
        return value;
    }

    public Long queryLong(String query, Object... params) throws Exception {
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        Exception exception = null;
        Long value = null;
        try {
            connection = sqlClient.getConnection();
            st = buildQueryStatement(connection, query, params);
            rs = st.executeQuery();
            if (rs.next()) {
                value = rs.getLong(1);
            }
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < params.length; i++) {
                if (i != params.length - 1) {
                    sb.append(params[i]).append(",");
                } else {
                    sb.append(params[i]);
                }
            }
            DebugLogger.logger.error("query error {} params {}", query, sb.toString());
            exception = e;
        } finally {
            sqlClient.releaseConnection(connection, st, rs);
            if (exception != null) {
                throw exception;
            }
        }
        return value;
    }

    public Double queryDouble(String query, Object... params) throws Exception {
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        Exception exception = null;
        Double value = null;
        try {
            connection = sqlClient.getConnection();
            st = buildQueryStatement(connection, query, params);
            rs = st.executeQuery();
            if (rs.next()) {
                value = rs.getDouble(1);
            }
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < params.length; i++) {
                if (i != params.length - 1) {
                    sb.append(params[i]).append(",");
                } else {
                    sb.append(params[i]);
                }
            }
            DebugLogger.logger.error("query error {} params {}", query, sb.toString());
            exception = e;
        } finally {
            sqlClient.releaseConnection(connection, st, rs);
            if (exception != null) {
                throw exception;
            }
        }
        return value;
    }

    public String queryString(String query, Object... params) throws Exception {
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        Exception exception = null;
        String value = null;
        try {
            connection = sqlClient.getConnection();
            st = buildQueryStatement(connection, query, params);
            rs = st.executeQuery();
            if (rs.next()) {
                value = rs.getString(1);
            }
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < params.length; i++) {
                if (i != params.length - 1) {
                    sb.append(params[i]).append(",");
                } else {
                    sb.append(params[i]);
                }
            }
            DebugLogger.logger.error("query error {} params {}", query, sb.toString());
            exception = e;
        } finally {
            sqlClient.releaseConnection(connection, st, rs);
            if (exception != null) {
                throw exception;
            }
        }
        return value;
    }

    public boolean queryExist(String query, Object... params) throws Exception {
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        Exception exception = null;
        boolean exist = false;
        try {
            connection = sqlClient.getConnection();
            st = buildQueryStatement(connection, query, params);
            rs = st.executeQuery();
            exist = rs.next();
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < params.length; i++) {
                if (i != params.length - 1) {
                    sb.append(params[i]).append(",");
                } else {
                    sb.append(params[i]);
                }
            }
            DebugLogger.logger.error("query error {} params {}", query, sb.toString());
            exception = e;
        } finally {
            sqlClient.releaseConnection(connection, st, rs);
            if (exception != null) {
                throw exception;
            }
        }
        return exist;
    }

    public int queryAmmount(String query, Object... params) throws Exception {
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        Exception exception = null;
        int count = 0;
        try {
            connection = sqlClient.getConnection();
            st = buildQueryStatement(connection, query, params);
            rs = st.executeQuery();
            while (rs.next()) {
                count++;
            }
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < params.length; i++) {
                if (i != params.length - 1) {
                    sb.append(params[i]).append(",");
                } else {
                    sb.append(params[i]);
                }
            }
            DebugLogger.logger.error("query error {} params {}", query, sb.toString());
            exception = e;
        } finally {
            sqlClient.releaseConnection(connection, st, rs);
            if (exception != null) {
                throw exception;
            }
        }
        return count;
    }

    public JsonObject queryOne(String query, Object... params) throws Exception {
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        Exception exception = null;
        JsonObject json = null;
        try {
            connection = sqlClient.getConnection();
            st = buildQueryStatement(connection, query, params);
            rs = st.executeQuery();
            json = toJsonObject(rs);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < params.length; i++) {
                if (i != params.length - 1) {
                    sb.append(params[i]).append(",");
                } else {
                    sb.append(params[i]);
                }
            }
            DebugLogger.logger.error("query error {} params {}", query, sb.toString());
            exception = e;
        } finally {
            sqlClient.releaseConnection(connection, st, rs);
            if (exception != null) {
                throw exception;
            }
        }
        return json;
    }

    public JsonArray query(String query, Object... params) throws Exception {
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        Exception exception = null;
        JsonArray array = new JsonArray();
        try {
            connection = sqlClient.getConnection();
            st = buildQueryStatement(connection, query, params);
            rs = st.executeQuery();
            array = toJsonArray(rs);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < params.length; i++) {
                if (i != params.length - 1) {
                    sb.append(params[i]).append(",");
                } else {
                    sb.append(params[i]);
                }
            }
            DebugLogger.logger.error("query error {} params {}", query, sb.toString());
            exception = e;
        } finally {
            sqlClient.releaseConnection(connection, st, rs);
            if (exception != null) {
                throw exception;
            }
        }
        return array;
    }

    public int update(String query, Object... params) throws Exception {
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        Exception exception = null;
        int x = 0;
        try {
            connection = sqlClient.getConnection();
            st = buildQueryStatement(connection, query, params);
            x = st.executeUpdate();
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < params.length; i++) {
                if (i != params.length - 1) {
                    sb.append(params[i]).append(",");
                } else {
                    sb.append(params[i]);
                }
            }
            DebugLogger.logger.error("update error {} params {}", query, sb.toString());
            exception = e;
        } finally {
            sqlClient.releaseConnection(connection, st, rs);
            if (exception != null) {
                throw exception;
            }
        }
        return x;
    }

    public JsonObject toJsonObject(ResultSet rs) throws SQLException {
        if (rs.next()) {
            int total_columns = rs.getMetaData().getColumnCount();
            JsonObject obj = new JsonObject();
            for (int i = 0; i < total_columns; i++) {
                addToJson(obj, rs.getMetaData().getColumnLabel(i + 1), rs.getObject(i + 1));
            }
            return obj;
        } else {
            return null;
        }
    }

    public JsonArray toJsonArray(ResultSet rs) throws SQLException {
        JsonArray array = new JsonArray();
        while (rs.next()) {
            int total_columns = rs.getMetaData().getColumnCount();
            JsonObject obj = new JsonObject();
            for (int i = 0; i < total_columns; i++) {
                addToJson(obj, rs.getMetaData().getColumnLabel(i + 1), rs.getObject(i + 1));
            }
            array.add(obj);
        }
        return array;
    }

    public static void addToJson(JsonObject json, String label, Object value) {
        if (value == null) {
            json.add(label, null);
            return;
        }
        if (value instanceof String) {
            boolean result = addAsJson(json, label, (String) value);
            if (!result) {
                json.addProperty(label, (String) value);
            }
        } else if (value instanceof Boolean) {
            json.addProperty(label, ((Boolean) value) ? 1 : 0);
        } else {
            json.addProperty(label, (Number) value);
        }
    }

    public static boolean addAsJson(JsonObject json, String label, String value) {
        JsonObject obj = GsonUtil.toJsonObject(value);
        if (obj != null) {
            json.add(label, obj);
            return true;
        }
        JsonArray array = GsonUtil.toJsonArray(value);
        if (array != null) {
            json.add(label, array);
            return true;
        }
        return false;
    }

    public PreparedStatement buildQueryStatement(Connection connection, String query, Object... params) throws SQLException {
        PreparedStatement st = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        int count = 1;
        for (int i = 0; i < params.length; i++) {
            if(params[i] != null) {
                addParamsByValue(st, count++, params[i], params[i].getClass());
            }else{
                st.setString(count++, null);
            }
        }
        return st;
    }

    public static void addParamsByValue(PreparedStatement st, int index, Object value, Type type) throws SQLException {
        if (type == String.class) {
            st.setString(index, (String) value);
        }else if (type == Boolean.class || type == boolean.class) {
            st.setBoolean(index, (Boolean) value);
        }else if (type == Integer.class || type == int.class) {
            st.setInt(index, (Integer) value);
        } else if (type == Long.class || type == long.class) {
            st.setLong(index, (Long) value);
        } else if (type == Float.class || type == float.class) {
            st.setFloat(index, (Float) value);
        } else if (type == Double.class || type == double.class) {
            st.setDouble(index, (Double) value);
        } else if (type == BigDecimal.class) {
            st.setBigDecimal(index, (BigDecimal) value);
        } else if (type == Integer[].class || type == int[].class) {
            st.setString(index, GsonUtil.gson.toJson(value));
        } else if (type == Long[].class || type == long[].class) {
            st.setString(index, GsonUtil.gson.toJson(value));
        } else if (type == Double[].class || type == double[].class) {
            st.setString(index, GsonUtil.gson.toJson(value));
        } else if (type == JsonObject.class || type == JsonArray.class) {
            st.setString(index, value.toString());
        }
    }


    public int updateObjectToDb(String table, String primaryKey, JsonObject json) throws Exception {
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        Exception exception = null;
        int x = 0;
        try {
            connection = sqlClient.getConnection();
            st = buildUpdateStatement(connection, table, primaryKey, json);
            x = st.executeUpdate();
        } catch (Exception e) {
            DebugLogger.logger.error("updateObjectToDb {}, {}", table, json);
            exception = e;
        } finally {
            sqlClient.releaseConnection(connection, st, rs);
            if (exception != null) {
                throw exception;
            }
        }
        return x;
    }

    public int updateObjectToDb(String table, JsonObject json) throws Exception {
        return updateObjectToDb(table, "id", json);
    }

    public void insertObjectToDB(String table, String primaryKey, JsonObject json) throws Exception {
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        Exception exception = null;
        try {
            connection = sqlClient.getConnection();
            st = buildInsertStatement(connection, table, json);
            st.executeUpdate();
            rs = st.getGeneratedKeys();
            if (rs.next()) {
                long id = rs.getLong("insert_id");
                json.addProperty(primaryKey, id);
            }
        } catch (Exception e) {
            DebugLogger.logger.error("insertObjectToDB {}, {}", table, json);
            exception = e;
        } finally {
            sqlClient.releaseConnection(connection, st, rs);
            if (exception != null) {
                throw exception;
            }
        }
    }

    public void insertObjectToDB(String table, JsonObject json) throws Exception {
        insertObjectToDB(table, "id", json);
    }


    public PreparedStatement buildInsertStatement(Connection connection, String table, JsonObject json) throws Exception {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(table).append(" (");
        Set<Map.Entry<String, JsonElement>> fields = json.entrySet();
        Iterator<Map.Entry<String, JsonElement>> iterator = fields.iterator();
        int size = fields.size();
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < size; i++) {
            Map.Entry<String, JsonElement> entry = iterator.next();
            String key = entry.getKey();
            sb1.append(key);
            if (i == size - 1) {
                sb1.append(") VALUES (");
                sb2.append("?)");
            } else {
                sb1.append(",");
                sb2.append("?,");
            }
        }
        sb.append(sb1).append(sb2);
        String query = sb.toString();
        PreparedStatement st = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        iterator = fields.iterator();
        for (int i = 1; i <= size; i++) {
            Map.Entry<String, JsonElement> entry = iterator.next();
            addParamsByField(st, i, entry.getValue());
        }
        return st;
    }

    public void insertManyObjectToDB(String table, JsonArray array) throws Exception {
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        Exception exception = null;
        try {
            connection = sqlClient.getConnection();
            connection.setAutoCommit(false);
            st = buildInsertManyStatement(connection, table, array);
            st.executeBatch();
            connection.commit();
        } catch (Exception e) {
            DebugLogger.logger.error("insertObjectToDB {}, {}", table, array);
            exception = e;
        } finally {
            sqlClient.releaseConnection(connection, st, rs);
            if (exception != null) {
                throw exception;
            }
        }
    }

    public PreparedStatement buildInsertManyStatement(Connection connection, String table, JsonArray array) throws Exception {
        if (array.size() > 0) {
            JsonObject json = array.get(0).getAsJsonObject();
            StringBuilder sb = new StringBuilder("INSERT INTO ");
            sb.append(table).append(" (");
            Set<Map.Entry<String, JsonElement>> fields = json.entrySet();
            Iterator<Map.Entry<String, JsonElement>> iterator = fields.iterator();
            int size = fields.size();
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            for (int i = 0; i < size; i++) {
                Map.Entry<String, JsonElement> entry = iterator.next();
                String key = entry.getKey();
                sb1.append(key);
                if (i == size - 1) {
                    sb1.append(") VALUES (");
                    sb2.append("?)");
                } else {
                    sb1.append(",");
                    sb2.append("?,");
                }
            }
            sb.append(sb1).append(sb2);
            String query = sb.toString();
            PreparedStatement st = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            for (int j = 0; j < array.size(); j++) {
                JsonObject object = array.get(j).getAsJsonObject();
                fields = object.entrySet();

                iterator = fields.iterator();
                for (int i = 1; i <= size; i++) {
                    Map.Entry<String, JsonElement> entry = iterator.next();
                    addParamsByField(st, i, entry.getValue());
                }
                st.addBatch();
            }
            return st;
        }
        return null;
    }

    public PreparedStatement buildUpdateStatement(Connection connection, String table, String primaryField, JsonObject json) throws SQLException, IllegalAccessException {
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(table).append(" SET ");
        Set<Map.Entry<String, JsonElement>> fields = json.entrySet();
        Iterator<Map.Entry<String, JsonElement>> iterator = fields.iterator();
        int size = fields.size();
        for (int i = 0; i < size; i++) {
            Map.Entry<String, JsonElement> entry = iterator.next();
            String key = entry.getKey();
            if (!key.equals(primaryField)) {
                sb.append(key).append(" = ? ");
            }
            if (i != size - 1 && !key.equals(primaryField)) {
                sb.append(", ");
            }
        }
        sb.append("WHERE ").append(primaryField).append("= ?");
        String query = sb.toString();
        PreparedStatement st = connection.prepareStatement(query);
        int count = 1;
        JsonElement idValue = null;
        iterator = fields.iterator();
        for (int i = 0; i < size; i++) {
            Map.Entry<String, JsonElement> entry = iterator.next();
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if (!key.equals(primaryField)) {
                addParamsByField(st, count++, value);
            } else {
                idValue = value;
            }
        }

        addParamsByField(st, count, idValue);
        return st;
    }

    public static void addParamsByField(PreparedStatement st, int index, JsonElement value) throws IllegalAccessException, SQLException {
        if (value.isJsonNull()) {
            st.setObject(index, null);
        } else if (value.isJsonPrimitive()) {
            JsonPrimitive primitive = value.getAsJsonPrimitive();
            if (primitive.isBoolean()) {
                st.setBoolean(index, value.getAsBoolean());
            } else if (primitive.isNumber()) {
                st.setDouble(index, value.getAsDouble());
            } else if (primitive.isString()) {
                st.setString(index, value.getAsString());
            }
        } else {
            st.setString(index, value.toString());
        }
    }


    public Long insertOne(String query, Object... params) throws Exception {
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        Exception exception = null;
        try {
            connection = sqlClient.getConnection();
            st = buildQueryStatement(connection, query, params);
            int x = st.executeUpdate();
            if(x == 1){
                ResultSet generatedKeys = st.getGeneratedKeys();
                if (generatedKeys.next()) {
                    long insertedId = generatedKeys.getLong(1);
                    return insertedId;
                }
            }
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < params.length; i++) {
                if (i != params.length - 1) {
                    sb.append(params[i]).append(",");
                } else {
                    sb.append(params[i]);
                }
            }
            DebugLogger.logger.error("update error {} params {}", query, sb.toString());
            exception = e;
        } finally {
            sqlClient.releaseConnection(connection, st, rs);
            if (exception != null) {
                throw exception;
            }
        }
        return -1L;
    }

    public boolean checkTableExisting(String table) {
        boolean result = false;
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = sqlClient.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            resultSet = metaData.getTables(null, null, table, null);
            if (resultSet.next()) {
                result = true;
            }
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
        }finally {
            sqlClient.releaseConnection(connection, resultSet);
            return result;
        }
    }
    public boolean createTable(String table, String createTableSQL, String ... createIndexSql) {
        boolean result = false;
        Connection connection = null;
        PreparedStatement[] listSt = null;
        try{
            createTableSQL = createTableSQL.replace("table_name", table);
            for(int i = 0; i < createIndexSql.length; i++){
                createIndexSql[i] = createIndexSql[i].replace("table_name", table);
            }
            connection = sqlClient.getConnection();
            connection.setAutoCommit(false);
            listSt = new PreparedStatement[createIndexSql.length+1];
            listSt[0] = connection.prepareStatement(createTableSQL);
            listSt[0].executeUpdate();
            for(int i = 1; i <= createIndexSql.length; i++){
                listSt[i] = connection.prepareStatement(createIndexSql[i-1]);
                listSt[i].executeUpdate();
            }
            connection.setAutoCommit(true);
            connection.commit();
            result = true;
        }catch (Exception e){
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
                    DebugLogger.logger.error(ExceptionUtils.getStackTrace(ex));
                }
            }
            result = false;
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
        }finally {
            sqlClient.releaseConnection(connection, listSt);
            return result;
        }
    }
}
