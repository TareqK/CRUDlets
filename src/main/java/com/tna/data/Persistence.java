/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.data;

import com.tna.common.AccessError;
import com.tna.common.AccessError.ERROR_TYPE;
import com.tna.common.UserAccessControl;
import com.tna.utils.JSON;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 *
 */
public class Persistence {

    private static final String CREATE_OBJECT_SQL = "INSERT INTO %s (%s) VALUES (%s) ";
    private static final String READ_OBJECT_SQL = "SELECT * FROM %s WHERE id = ?";
    private static final String UPDATE_OBJECT_SQL = "UPDATE %s SET %s where id = ?";
    private static final String DELETE_OBJECT_SQL = "DELETE FROM %s WHERE id = ?";
    private static final String LIST_OBJECT_SQL = "SELECT * FROM %s";

    private static final String LIST_USER_OBJECTS_SQL = "SELECT * FROM %s WHERE user = (SELECT id FROM %s WHERE token = ? )";
    private static final String READ_USER_OBJECT_SQL = "SELECT * FROM %s WHERE id = ? AND user = (SELECT id FROM %s WHERE token = ? ) ";
    private static final String ASSIGN_OBJECT_TO_USER_SQL = "UPDATE %s set user = ? WHERE id = ?; ";
    private static final String UPDATE_USER_OBJECT_SQL = "UPDATE %s SET %s where id = ? AND user = (SELECT id FROM %s WHERE token = ? )";
    private static final String DELETE_USER_OBJECT_SQL = "DELETE FROM %s WHERE id = ? and user = (SELECT id FROM %s WHERE token = ? )";

    private static final String SEARCH_BY_PROPERTIES_SQL = "SELECT * FROM %s WHERE %s";
    private static final String UPDATE_BY_PROPERTIES_SQL = "UPDATE %s SET %s WHERE %s";

    private static final String DELETE_BY_PROPERTIES_SQL = "DELETE FROM %s WHERE %s";

    private static final String LIST_NEWER_THAN_SQL = "SELECT * FROM %s Where timeStamp >= ?";

    /**
     * Creates a new object and assigns it to a user.
     *
     * @param object The class of the object we want to create.
     * @param author The class of the user we want to assign it to.
     * @param json The properties of the object.
     * @param token The session token of the user.
     * @return returns the generated id of the object, and the user it is
     * assigned to.
     * @throws AccessError if the object is not authorised or if creation
     * failed.
     */
    public static JSONObject create(Class object, Class author, JSONObject json, String token) throws AccessError {
        JSONObject result;
        Connection conn = Access.pool.checkOut();
        try {
            String authorName = author.getSimpleName();
            String className = object.getSimpleName();

            JSONObject generatedKeys;
            long user = (long) UserAccessControl.fetchUserByToken(author, token).get("id");
            generatedKeys = Persistence.create(object, json);
            try (PreparedStatement pstmt = conn.prepareStatement(String.format(ASSIGN_OBJECT_TO_USER_SQL, className))) {
                pstmt.setObject(1, user);
                pstmt.setObject(2, generatedKeys.get("key"));
                if (pstmt.executeUpdate() == 0) {
                    throw new AccessError(ERROR_TYPE.ENTITY_NOT_FOUND);
                }
            }

            result = generatedKeys;

        } catch (SQLException e) {
            System.out.println(e);
            throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
        } finally {
            Access.pool.checkIn(conn);
        }
        return result;
    }

    /**
     * Reads an object that has a user assigned to it.
     *
     * @param object The class of the object we want to read.
     * @param author The class of the user we want to authorise.
     * @param resource The id of the object we want to authorise access to.
     * @param token The session token of the user.
     * @return returns the JSON formatted object.
     * @throws AccessError if the user is not authorised or the object does not
     * exist.
     */
    public static JSONObject read(Class object, Class author, long resource, String token) throws AccessError {
        Connection conn = Access.pool.checkOut();
        try {
            String authorName = author.getSimpleName();
            String className = object.getSimpleName();
            UserAccessControl.authAccess(object, author, token, resource);
            JSONObject result = Persistence.read(object, resource);
            return result;
        } finally {
            Access.pool.checkIn(conn);
        }

    }

    /**
     * Updates an object that belongs to a user.
     *
     * @param object The class of the object we want to update.
     * @param author The class of the user the object belongs to.
     * @param json The data we want to update.
     * @param resource The id of the object we want to update.
     * @param token The session token of the user.
     * @return A success response in JSON format.
     * @throws AccessError If the user is not authorised or the object does not
     * exist.
     */
    public static JSONObject update(Class object, Class author, JSONObject json, long resource, String token) throws AccessError {
        Connection conn = Access.pool.checkOut();
        try {
            String authorName = author.getSimpleName();
            String className = object.getSimpleName();
            UserAccessControl.authAccess(object, author, token, resource);
            JSONObject result = Persistence.update(object, resource, json);
            return result;
        } finally {
            Access.pool.checkIn(conn);
        }

    }

    /**
     * Deletes an object belonging to a user.
     *
     * @param object The class of the object we want to access.
     * @param author The class of the user we want to authorise.
     * @param resource The id of the object we want to delete.
     * @param token The session id of the user.
     * @return a success response in JSON format.
     * @throws AccessError if the user is not authorised, or if the object does
     * not exist.
     */
    public static JSONObject delete(Class object, Class author, long resource, String token) throws AccessError {
        Connection conn = Access.pool.checkOut();
        try {
            String authorName = author.getSimpleName();
            String className = object.getSimpleName();
            UserAccessControl.authAccess(object, author, token, resource);
            JSONObject result = Persistence.delete(object, resource);
            return result;
        } finally {
            Access.pool.checkIn(conn);
        }
    }

    /**
     * Lists all the objects belonging to the user.
     *
     * @param object The class of the object we want to list.
     * @param author The class of the user we want to authorise.
     * @param token The session id of the user.
     * @return A JSON formatted list of objects belonging to the user.
     * @throws AccessError if the user is not authorised, or if the object does
     * not exist.
     */
    public static JSONObject list(Class object, Class author, String token) throws AccessError {
        JSONObject result = new JSONObject();
        Connection conn = Access.pool.checkOut();

        try {
            String className = object.getSimpleName();
            String authorName = author.getSimpleName();
            UserAccessControl.checkToken(author, token);
            try (PreparedStatement pstmt = conn.prepareStatement((String.format(LIST_USER_OBJECTS_SQL, className, authorName)), Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setObject(1, token);
                try (ResultSet rs = pstmt.executeQuery()) {
                    Field[] fields = getAllFields(object);
                    int i = 0;
                    if (rs.next() == false) {
                        throw new AccessError(ERROR_TYPE.ENTITY_NOT_FOUND);

                    }
                    do {
                        JSONObject temp = new JSONObject();
                        for (Field field : fields) {
                            try {
                                temp.put(field.getName(), rs.getObject(field.getName()));
                            } catch (IllegalArgumentException ex) {
                            }
                        }
                        result.put(i, temp);
                        i++;
                    } while (rs.next());
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
        } finally {
            Access.pool.checkIn(conn);
        }
        return result;
    }

    /**
     * Updates objects matching some criteria.
     *
     * @param object The class of the object we want to update.
     * @param query The query we want to match against.
     * @param json The data we want to update.
     * @return a success response in JSON format.
     * @throws AccessError
     */
    public static JSONObject updateByProperties(Class object, JSONObject query, JSONObject json) throws AccessError {
        JSONObject result = new JSONObject();
        Connection conn = Access.pool.checkOut();
        try {
            StringBuilder queryString = new StringBuilder();
            Set queryKeys = query.keySet();
            int i, length;
            i = 0;
            length = queryKeys.size();
            for (Object key : queryKeys) {
                try {
                    object.getDeclaredField(key.toString());
                    queryString.append(key.toString());
                    queryString.append(" = ?");
                    if (i < length - 1) {
                        queryString.append(" and ");
                    }
                    i++;
                } catch (NoSuchFieldException | SecurityException ex) {
                    throw new SQLException();
                }
            }

            StringBuilder jsonString = new StringBuilder();
            Set jsonKeys = json.keySet();
            i = 0;
            length = jsonKeys.size();
            for (Object key : jsonKeys) {
                try {
                    object.getDeclaredField(key.toString());
                    jsonString.append(key.toString());
                    jsonString.append(" = ?");
                    if (i < length - 1) {
                        jsonString.append(" and ");
                    }
                    i++;
                } catch (NoSuchFieldException | SecurityException ex) {
                    throw new SQLException();
                }
            }

            String className = object.getSimpleName();
            try (PreparedStatement pstmt = conn.prepareStatement((String.format(UPDATE_BY_PROPERTIES_SQL, className, jsonString.toString(), queryString.toString())), Statement.RETURN_GENERATED_KEYS)) {
                i = 0;
                for (Object key : jsonKeys) {
                    pstmt.setObject(i, json.get(key));
                }
                for (Object key : queryKeys) {
                    pstmt.setObject(i, query.get(key));
                }

                int effected = pstmt.executeUpdate();
                if (effected == 0) {
                    result = null;
                } else {
                    result = JSON.successResponse();
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
        } finally {
            Access.pool.checkIn(conn);
        }
        return result;
    }

    /**
     * Reads an object based on some criteria. Does not check if there is only
     * one result, it simply returns the first one.
     *
     * @param object The class of the object we want to read.
     * @param query The query we want to match against.
     * @return a JSON formatted object, or null if no object was found.
     */
    public static JSONObject readByProperties(Class object, JSONObject query) {
        JSONObject result = new JSONObject();
        Connection conn = Access.pool.checkOut();

        try {
            StringBuilder valuesString = new StringBuilder();
            Set keys = query.keySet();
            int i = 0;
            int length = keys.size();
            for (Object key : keys) {
                try {
                    object.getDeclaredField(key.toString());
                    valuesString.append(key.toString());
                    valuesString.append(" = ?");
                    if (i < length - 1) {
                        valuesString.append(" and ");
                    }
                    i++;
                } catch (NoSuchFieldException | SecurityException ex) {
                    throw new SQLException();
                }
            }
            String className = object.getSimpleName();
            try (PreparedStatement pstmt = conn.prepareStatement((String.format(SEARCH_BY_PROPERTIES_SQL, className, valuesString.toString())), Statement.RETURN_GENERATED_KEYS)) {
                i = 1;
                for (Object key : keys) {
                    pstmt.setObject(i, query.get(key));
                    i++;
                }
                try (ResultSet rs = pstmt.executeQuery()) {
                    Field[] fields = getAllFields(object);
                    rs.next();
                    for (Field field : fields) {
                        try {
                            result.put(field.getName(), rs.getObject(field.getName()));
                        } catch (IllegalArgumentException ex) {

                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            result = null;
        } finally {
            Access.pool.checkIn(conn);
        }
        return result;
    }

    /**
     * Returns all objects matching some criteria.
     *
     * @param object The class of the object we want to read from.
     * @param query The query we want to match against.
     * @return returns a JSON formatted list of the objects, or null if non are
     * found.
     */
    public static JSONObject listByProperties(Class object, JSONObject query) {
        JSONObject result = new JSONObject();
        Connection conn = Access.pool.checkOut();

        try {
            StringBuilder valuesString = new StringBuilder();
            Set keys = query.keySet();
            int i = 0;
            int length = keys.size();
            for (Object key : keys) {
                try {
                    object.getDeclaredField(key.toString());
                    valuesString.append(key.toString());
                    valuesString.append(" = ?");
                    if (i < length - 1) {
                        valuesString.append(" and ");
                    }
                    i++;
                } catch (NoSuchFieldException | SecurityException ex) {
                    throw new SQLException();
                }
            }
            String className = object.getSimpleName();
            try (PreparedStatement pstmt = conn.prepareStatement((String.format(SEARCH_BY_PROPERTIES_SQL, className, valuesString.toString())), Statement.RETURN_GENERATED_KEYS)) {
                i = 1;
                for (Object key : keys) {
                    pstmt.setObject(i, query.get(key));
                    i++;
                }
                try (ResultSet rs = pstmt.executeQuery()) {
                    Field[] fields = getAllFields(object);
                    while (rs.next()) {
                        JSONObject obj = new JSONObject();
                        for (Field field : fields) {
                            try {
                                obj.put(field.getName(), rs.getObject(field.getName()));
                            } catch (IllegalArgumentException ex) {
                            }
                        }
                        result.put(rs.getObject("id"), obj);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            result = null;
        } finally {
            Access.pool.checkIn(conn);
        }
        return result;

    }

    public static JSONObject searchByProperties(Class object, JSONObject query) {
        JSONObject result = new JSONObject();
        Connection conn = Access.pool.checkOut();

        try {
            StringBuilder valuesString = new StringBuilder();
            JSONObject orQuery = (JSONObject) query.get("or");
            JSONObject andQuery = (JSONObject) query.get("and");
            Set orQueryKeys = null;
            Set andQueryKeys = null;

            int i = 0;
            if (orQuery != null) {
                orQueryKeys = orQuery.keySet();
                int length = orQueryKeys.size();
                if (length > 0) {
                    valuesString.append("(");
                    for (Object key : orQueryKeys) {
                        try {
                            object.getDeclaredField(key.toString());
                            valuesString.append(key.toString());
                            valuesString.append(" = ?");
                            if (i < length - 1) {
                                valuesString.append(" or ");
                            }
                            i++;
                        } catch (NoSuchFieldException | SecurityException ex) {
                            throw new SQLException();
                        }
                    }
                    valuesString.append(")");
                }
            }
            if (andQuery != null) {
                andQueryKeys = andQuery.keySet();
                int length = orQueryKeys.size();
                if (i > 0 && length > 0) {
                    valuesString.append("AND (");
                }
                i = 0;
                for (Object key : andQueryKeys) {
                    try {
                        object.getDeclaredField(key.toString());
                        valuesString.append(key.toString());
                        valuesString.append(" = ?");
                        if (i < length - 1) {
                            valuesString.append(" and ");
                        }
                        i++;
                    } catch (NoSuchFieldException | SecurityException ex) {
                        throw new SQLException();
                    }
                }
                if (i > 0) {
                    valuesString.append(")");
                }
            }
            String className = object.getSimpleName();
            try (PreparedStatement pstmt = conn.prepareStatement((String.format(SEARCH_BY_PROPERTIES_SQL, className, valuesString.toString())), Statement.RETURN_GENERATED_KEYS)) {
                i = 1;
                if (orQuery != null) {
                    for (Object key : orQueryKeys) {
                        pstmt.setObject(i, orQuery.get(key));
                        i++;
                    }
                }
                if (andQuery != null) {
                    for (Object key : andQueryKeys) {
                        pstmt.setObject(i, andQuery.get(key));
                        i++;
                    }
                }
                try (ResultSet rs = pstmt.executeQuery()) {
                    Field[] fields = getAllFields(object);
                    while (rs.next()) {
                        JSONObject obj = new JSONObject();
                        for (Field field : fields) {
                            try {
                                obj.put(field.getName(), rs.getObject(field.getName()));
                            } catch (IllegalArgumentException ex) {
                            }
                        }
                        result.put(rs.getObject("id"), obj);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            result = null;
        } finally {
            Access.pool.checkIn(conn);
        }
        return result;

    }

    /**
     * Creates a new object.
     *
     * @param object The class of the object we want to create.
     * @param json The data of the object we want to create.
     * @return returns the generated id of the object, and the user it is
     * assigned to.
     * @throws AccessError if creation failed.
     */
    public static JSONObject create(Class object, JSONObject json) throws AccessError {
        Connection conn = Access.pool.checkOut();
        JSONObject result = JSON.successResponse();

        try {
            String className = object.getSimpleName();
            Field[] fields = object.getDeclaredFields();
            StringBuilder columns = new StringBuilder();
            StringBuilder values = new StringBuilder();
            for (Field field : fields) {
                columns.append(field.getName());
                values.append("?,");
                columns.append(",");
            }
            columns.deleteCharAt((columns.length() - 1));
            values.deleteCharAt((values.length() - 1));
            try (PreparedStatement pstmt = conn.prepareStatement((String.format(CREATE_OBJECT_SQL, className, columns.toString(), values.toString())), Statement.RETURN_GENERATED_KEYS)) {
                int i = 1;
                for (Field field : fields) {
                    try {
                        pstmt.setObject(i, json.get(field.getName()));
                    } catch (IllegalArgumentException ex) {
                    }
                    i++;
                }
                pstmt.execute();
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new AccessError(ERROR_TYPE.ENTITY_NOT_FOUND);
                    }
                    result.put("key", rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
        } finally {
            Access.pool.checkIn(conn);
        }
        return result;
    }

    /**
     * Reads an object.
     *
     * @param object The class of the object we want to read.
     * @param resource The id of the object we want to read.
     * @return the JSON formatted object.
     * @throws AccessError if the object was not found or on failure.
     */
    public static JSONObject read(Class object, long resource) throws AccessError {
        JSONObject result = new JSONObject();
        Connection conn = Access.pool.checkOut();
        try {
            String className = object.getSimpleName();
            try (PreparedStatement pstmt = conn.prepareStatement((String.format(READ_OBJECT_SQL, className)), Statement.RETURN_GENERATED_KEYS)) {
                Field[] fields = getAllFields(object);
                pstmt.setLong(1, resource);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new AccessError(ERROR_TYPE.ENTITY_NOT_FOUND);
                    }
                    for (Field field : fields) {
                        try {
                            result.put(field.getName(), rs.getObject(field.getName()));
                        } catch (IllegalArgumentException ex) {
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
        } finally {
            Access.pool.checkIn(conn);
        }
        return result;
    }

    /**
     * Updates an object.
     *
     * @param object The class of the object we want to update.
     * @param resource The id of the object we want to update.
     * @param json The data we want to update.
     * @return A success response in JSON format.
     * @throws AccessError If the object does not exist.
     */
    public static JSONObject update(Class object, long resource, JSONObject json) throws AccessError {
        Connection conn = Access.pool.checkOut();
        JSONObject result = null;
        try {
            String className = object.getSimpleName();
            StringBuilder values = new StringBuilder();
            Set keySet = json.keySet();
            for (Object key : keySet) {
                try {
                    object.getDeclaredField(key.toString());
                    values.append(key.toString()).append(" = ?,");
                } catch (NoSuchFieldException | SecurityException ex) {
                    throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
                }
            }
            values.deleteCharAt((values.length() - 1));
            try (PreparedStatement pstmt = conn.prepareStatement((String.format(UPDATE_OBJECT_SQL, className, values.toString())), Statement.RETURN_GENERATED_KEYS)) {
                int i = 1;
                for (Object key : keySet) {
                    try {
                        pstmt.setObject(i, json.get(key));
                    } catch (IllegalArgumentException ex) {
                    }
                    i++;
                }
                pstmt.setObject(i, resource);
                int effected = pstmt.executeUpdate();
                if (effected == 0) {
                    result = null;
                } else {
                    result = JSON.successResponse();
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
        } finally {
            Access.pool.checkIn(conn);
        }
        return result;
    }

    /**
     * Deletes an object.
     *
     * @param object The class of the object we want to delete.
     * @param resource The resource of the object we want to delete.
     * @return a success response in JSON format.
     * @throws AccessError if the object does not exist.
     */
    public static JSONObject delete(Class object, long resource) throws AccessError {
        JSONObject result = JSON.successResponse();
        Connection conn = Access.pool.checkOut();

        try {
            String className = object.getSimpleName();
            try (PreparedStatement pstmt = conn.prepareStatement((String.format(DELETE_OBJECT_SQL, className)), Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setLong(1, resource);
                int effected = pstmt.executeUpdate();
                pstmt.close();
                if (effected == 0) {
                    throw new AccessError(ERROR_TYPE.ENTITY_NOT_FOUND);
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            result = null;
        } finally {
            Access.pool.checkIn(conn);
        }
        return result;
    }

    /**
     * Lists all objects.
     *
     * @param object The class of the object we want to list.
     * @return A JSON formatted list of objects.
     * @throws AccessError if the object does not exist.
     */
    public static JSONObject list(Class object) throws AccessError {
        JSONObject result = new JSONObject();
        Connection conn = Access.pool.checkOut();

        try {
            String className = object.getSimpleName();
            try (PreparedStatement pstmt = conn.prepareStatement((String.format(LIST_OBJECT_SQL, className)), Statement.RETURN_GENERATED_KEYS)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    Field[] fields = getAllFields(object);
                    int i = 0;
                    if (rs.next() == false) {
                        throw new AccessError(ERROR_TYPE.ENTITY_NOT_FOUND);
                    }
                    do {
                        JSONObject obj = new JSONObject();
                        for (Field field : fields) {
                            try {
                                obj.put(field.getName(), rs.getObject(field.getName()));
                            } catch (IllegalArgumentException ex) {
                            }
                        }
                        result.put(i, obj);
                        i++;
                    } while (rs.next());

                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
        } finally {
            Access.pool.checkIn(conn);
        }
        return result;
    }

    private static Field[] getAllFields(Class<?> type) {
        ArrayList<Field> fields = new ArrayList<>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return (fields.toArray(new Field[fields.size()]));
    }

    private static String buildValues(Class object, JSONObject json) {
        StringBuilder values = new StringBuilder();
        Set keySet = json.keySet();
        keySet.forEach((key) -> {
            try {
                object.getDeclaredField(key.toString());
                values.append(key.toString()).append(" = ?,");
            } catch (NoSuchFieldException | SecurityException ex) {

            }
        });
        values.deleteCharAt((values.length() - 1));
        return values.toString();
    }

    public static JSONObject listNewerThan(Class object, Timestamp timeStamp) throws AccessError {
        JSONObject result = new JSONObject();
        Connection conn = Access.pool.checkOut();

        try {
            String className = object.getSimpleName();
            try (PreparedStatement pstmt = conn.prepareStatement((String.format(LIST_NEWER_THAN_SQL, className)), Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setTimestamp(1, timeStamp);
                try (ResultSet rs = pstmt.executeQuery()) {
                    Field[] fields = getAllFields(object);
                    int i = 0;
                    if (rs.next() == false) {
                        throw new AccessError(ERROR_TYPE.ENTITY_NOT_FOUND);
                    }
                    do {
                        JSONObject obj = new JSONObject();
                        for (Field field : fields) {
                            try {
                                obj.put(field.getName(), rs.getObject(field.getName()));
                            } catch (IllegalArgumentException ex) {
                            }
                        }
                        result.put(i, obj);
                        i++;
                    } while (rs.next());

                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
        } finally {
            Access.pool.checkIn(conn);
        }
        return result;
    }

    public static JSONObject deleteByProperties(Class object, JSONObject query) {
        JSONObject result = new JSONObject();
        Connection conn = Access.pool.checkOut();

        try {
            StringBuilder valuesString = new StringBuilder();
            Set keys = query.keySet();
            int i = 0;
            int length = keys.size();
            for (Object key : keys) {
                try {
                    object.getDeclaredField(key.toString());
                    valuesString.append(key.toString());
                    valuesString.append(" = ?");
                    if (i < length - 1) {
                        valuesString.append(" and ");
                    }
                    i++;
                } catch (NoSuchFieldException | SecurityException ex) {
                    throw new SQLException();
                }
            }
            String className = object.getSimpleName();
            try (PreparedStatement pstmt = conn.prepareStatement((String.format(DELETE_BY_PROPERTIES_SQL, className, valuesString.toString())), Statement.RETURN_GENERATED_KEYS)) {
                i = 1;
                for (Object key : keys) {
                    pstmt.setObject(i, query.get(key));
                    i++;
                }
                pstmt.executeUpdate();
                result = JSON.successResponse();
            }
        } catch (SQLException e) {
            System.out.println(e);
            result = null;
        } finally {
            Access.pool.checkIn(conn);
        }
        return result;

    }
}
