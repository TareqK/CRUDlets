/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.data;

import com.tna.utils.JSON;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    public static final String CREATE_OBJECT_SQL = "INSERT INTO %s (%s) VALUES (%s) ";
    public static final String READ_OBJECT_SQL = "SELECT * FROM %s WHERE id = ?";
    public static final String UPDATE_OBJECT_SQL = "UPDATE %s SET %s where id = ?";
    public static final String DELETE_OBJECT_SQL = "DELETE FROM %s WHERE id = ?";
    public static final String LIST_OBJECT_SQL = "SELECT * FROM %s";

    public static final String GET_PRIVILEGE_AND_ID_SQL = "SELECT id,level FROM %s WHERE token = ? ";
    public static final String GET_PASSWORD_SQL = "SELECT password,id FROM %s WHERE userName = ? ";
    public static final String SET_TOKEN_SQL = "UPDATE %s set token = ? where id = ?";

    public static final String READ_OBJECT_USER_SQL = "SELECT user FROM %s WHERE id = ?";
    public static final String LIST_USER_OBJECTS_SQL = "SELECT * FROM %s WHERE user = (SELECT id FROM %s WHERE token = ? )";
    public static final String READ_USER_OBJECT_SQL = "SELECT * FROM %s WHERE id = ? AND user = (SELECT id FROM %s WHERE token = ? ) ";
    public static final String CREATE_USER_OBJECT_SQL = "INSERT INTO %s (user) VALUES (?) WHERE id = ?; ";
    public static final String UPDATE_USER_OBJECT_SQL = "UPDATE %s SET %s where id = ? AND user = (SELECT id FROM %s WHERE token = ? )";
    public static final String DELETE_USER_OBJECT_SQL = "DELETE FROM %s WHERE id = ? and user = (SELECT id FROM %s WHERE token = ? )";

    public static final String SEARCH_BY_PROPERTY_SQL = "SELECT * from %s WHERE %s";

    /**
     * Creates a new object and ties it to the user. Returns a success message
     * if it works, returns null otherwise.
     *
     * @param object
     * @param author
     * @param json
     * @return Returns a JSONObject with a success message if the object was
     * successfully created, returns null otherwise.
     */
    public static JSONObject create(Class object, Class author, JSONObject json) {

        JSONObject result = JSON.successResponse();
        try {
            String authorName = author.getSimpleName();
            String className = object.getSimpleName();

            PreparedStatement pstmt2 = Access.connection.prepareStatement(String.format(GET_PRIVILEGE_AND_ID_SQL, authorName));
            ResultSet rs = pstmt2.executeQuery();
            rs.next();
            long user = rs.getLong("id");
            JSONObject key = Persistence.create(object, json);
            PreparedStatement pstmt = Access.connection.prepareStatement(String.format(CREATE_USER_OBJECT_SQL, className));
            pstmt.setObject(1, user);
            pstmt.setObject(2, Integer.parseInt(key.get("key").toString()));
            ResultSet rs2 = pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return result;
    }

    /**
     * Reads a specific object belonging to the user. If it does not belong to
     * him, it returns nothing.
     *
     * @param object
     * @param author
     * @param json
     * @param resource
     * @return returns a JSONObject of the object called if it belongs to the
     * user, returns null otherwise.
     */
    public static JSONObject read(Class object, Class author, JSONObject json, long resource) {

        JSONObject result = new JSONObject();
        try {
            String authorName = object.getSimpleName();
            String className = author.getSimpleName();
            PreparedStatement pstmt = Access.connection.prepareStatement((String.format(READ_USER_OBJECT_SQL, className, authorName)), Statement.RETURN_GENERATED_KEYS);
            pstmt.setObject(1, resource);
            pstmt.setObject(2, json.get("token"));
            ResultSet rs = pstmt.executeQuery();
            Field[] fields = getAllFields(object);
            rs.next();
            for (Field field : fields) {
                try {
                    result.put(field.getName(), rs.getObject(field.getName()));
                } catch (IllegalArgumentException ex) {
                }

            }
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return result;
    }

    /**
     * Updates an object belonging to the user. If it does not belong to the
     * user, it returns null.
     *
     * @param object
     * @param author
     * @param json
     * @param resource
     * @return returns a JSONObject with the updated object if it belongs to the
     * user, returns null otherwise.
     */
    public static JSONObject update(Class object, Class author, JSONObject json, long resource) {

        JSONObject result = new JSONObject();
        try {
            String authorName = object.getSimpleName();
            String className = author.getSimpleName();

            Field[] fields = getAllFields(object);
            StringBuilder values = new StringBuilder();
            Set keySet = json.keySet();
            for (Object key : keySet) {
                try {
                    object.getDeclaredField(key.toString());
                    values.append(key.toString()).append(" = ?,");
                } catch (NoSuchFieldException | SecurityException ex) {
                    throw new SQLException();
                }
            }
            values.deleteCharAt((values.length() - 1));
            PreparedStatement pstmt = Access.connection.prepareStatement((String.format(UPDATE_USER_OBJECT_SQL, className, values.toString(), authorName)), Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            for (Object key : keySet) {
                try {
                    pstmt.setObject(i, json.get(key));
                } catch (IllegalArgumentException ex) {
                }
                i++;
            }
            pstmt.setObject(i, resource);
            pstmt.setObject(i + 1, json.get("token"));
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            for (Field field : fields) {
                try {
                    result.put(field.getName(), rs.getObject(field.getName()));
                } catch (IllegalArgumentException ex) {
                }

            }
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return result;
    }

    /**
     * Deletes an object belonging to the user. Returns a success message if it
     * does, returns null otherwise.
     *
     * @param object
     * @param author
     * @param json
     * @param resource
     * @return returns a JSONObject with a success message if the object belongs
     * to the user, returns null otherwise.
     */
    public static JSONObject delete(Class object, Class author, JSONObject json, long resource) {

        JSONObject result = JSON.successResponse();

        try {
            String authorName = object.getSimpleName();
            String className = author.getSimpleName();
            PreparedStatement pstmt = Access.connection.prepareStatement((String.format(DELETE_USER_OBJECT_SQL, className, authorName)), Statement.RETURN_GENERATED_KEYS);
            pstmt.setObject(1, resource);
            pstmt.setObject(2, json.get("token"));
            ResultSet rs = pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return result;
    }

    /**
     * Lists all the objects belonging to the user.
     *
     * @param object
     * @param author
     * @param json
     * @return returns a JSONObject of the user's associated object of this type
     */
    public static JSONObject list(Class object, Class author, JSONObject json) {

        JSONObject result = new JSONObject();
        try {
            String className = object.getSimpleName();
            String authorName = author.getSimpleName();
            PreparedStatement pstmt = Access.connection.prepareStatement((String.format(LIST_USER_OBJECTS_SQL, className, authorName)), Statement.RETURN_GENERATED_KEYS);
            pstmt.setObject(1, json.get("token"));
            ResultSet rs = pstmt.executeQuery();
            Field[] fields = getAllFields(object);

            while (rs.next()) {
                JSONObject temp = new JSONObject();
                for (Field field : fields) {
                    try {
                        temp.put(field.getName(), rs.getObject(field.getName()));
                    } catch (IllegalArgumentException ex) {
                    }
                }
                result.put(rs.getObject("id"), temp);
            }
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return result;
    }

    /**
     * Searchers for an object by a set of properties and values. Returns the
     * first one.
     *
     * @param object
     * @param properties
     * @param values
     * @return returns a JSONObject of the first object fulfilling this
     * criteria.
     */
    public static JSONObject readByProperties(Class object, String[] properties, Object[] values) {

        JSONObject result = new JSONObject();

        try {
            if (properties.length != values.length) {
                throw new SQLException();
            }
            StringBuilder valuesString = new StringBuilder();
            for (int i = 0; i < properties.length; i++) {
                try {
                    object.getDeclaredField(properties[i]);
                    valuesString.append(properties[i]);

                    valuesString.append(" = ?");
                    if (i < properties.length - 1) {
                        valuesString.append(" and ");
                    }
                } catch (NoSuchFieldException | SecurityException ex) {
                    throw new SQLException();
                }

            }

            String className = object.getSimpleName();
            PreparedStatement pstmt = Access.connection.prepareStatement((String.format(SEARCH_BY_PROPERTY_SQL, className, valuesString.toString())), Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            for (Object o : values) {
                pstmt.setObject(i, o.toString());
                i++;
            }
            ResultSet rs = pstmt.executeQuery();
            Field[] fields = getAllFields(object);

            rs.next();
            for (Field field : fields) {
                try {
                    result.put(field.getName(), rs.getObject(field.getName()));
                } catch (IllegalArgumentException ex) {

                }
            }

        } catch (SQLException e) {
            System.out.println(e);

            return null;
        }
        return result;

    }

    /**
     * Searchers for objects by a set of properties and values. Returns a list
     * of them all.
     *
     * @param object
     * @param properties
     * @param values
     * @return returns a JSONObject of all the objects fulfilling this criteria.
     */
    public static JSONObject listByProperties(Class object, String[] properties, Object[] values) {

        JSONObject result = new JSONObject();

        try {
            if (properties.length != values.length) {
                throw new SQLException();
            }
            StringBuilder valuesString = new StringBuilder();
            for (int i = 0; i < properties.length; i++) {
                try {
                    object.getDeclaredField(properties[i]);
                    valuesString.append(properties[i]);
                    valuesString.append(" = ?");
                    if (i < properties.length - 1) {
                        valuesString.append(" and ");
                    }
                } catch (NoSuchFieldException | SecurityException ex) {
                    throw new SQLException();
                }

            }

            String className = object.getSimpleName();
            PreparedStatement pstmt = Access.connection.prepareStatement((String.format(SEARCH_BY_PROPERTY_SQL, className, valuesString.toString())), Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            for (Object o : values) {
                pstmt.setObject(i, o.toString());
                i++;
            }
            ResultSet rs = pstmt.executeQuery();
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

        } catch (SQLException e) {
            System.out.println(e);

            return null;
        }
        return result;

    }

    /**
     * Creates a new object.
     *
     * @param object
     * @param json
     * @return returns a success code if creation was successful, returns null
     * otherwise.
     */
    public static JSONObject create(Class object, JSONObject json) {
        JSONObject result = JSON.successResponse();

        try {
            String className = object.getSimpleName();
            Field[] fields = getAllFields(object);
            StringBuilder columns = new StringBuilder();
            StringBuilder values = new StringBuilder();
            for (Field field : fields) {
                columns.append(field.getName());
                values.append("?,");
                columns.append(",");
            }
            columns.deleteCharAt((columns.length() - 1));
            values.deleteCharAt((values.length() - 1));
            PreparedStatement pstmt = Access.connection.prepareStatement((String.format(CREATE_OBJECT_SQL, className, columns.toString(), values.toString())), Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            for (Field field : fields) {
                try {
                    pstmt.setObject(i, json.get(field.getName()));
                } catch (IllegalArgumentException ex) {
                }
                i++;
            }

            pstmt.execute();
            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            result.put("key", rs.getLong(1));

        } catch (SQLException e) {
            System.out.println(e);
            return null;

        }
        return result;

    }

    /**
     * Reads an object.
     *
     * @param object
     * @param id
     * @return returns a JSONObject if the object exists, returns null
     * otherwise.
     */
    public static JSONObject read(Class object, long id) {
        JSONObject result = new JSONObject();
        try {
            String className = object.getSimpleName();
            PreparedStatement pstmt;
            pstmt = Access.connection.prepareStatement((String.format(READ_OBJECT_SQL, className)), Statement.RETURN_GENERATED_KEYS);
            Field[] fields = getAllFields(object);
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            rs.next();
            for (Field field : fields) {
                try {
                    result.put(field.getName(), rs.getObject(field.getName()));
                } catch (IllegalArgumentException ex) {
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return result;
    }

    /**
     * Updates an existing object.
     *
     * @param object
     * @param id
     * @param json
     * @return returns a JSONObject with a success message if the object was
     * updated, returns null otherwise.
     */
    public static JSONObject update(Class object, long id, JSONObject json) {
        try {
            String className = object.getSimpleName();
            Field[] fields = getAllFields(object);
            StringBuilder values = new StringBuilder();
            Set keySet = json.keySet();
            for (Object key : keySet) {
                try {
                    object.getDeclaredField(key.toString());
                    values.append(key.toString()).append(" = ?,");
                } catch (NoSuchFieldException | SecurityException ex) {
                    throw new SQLException();
                }
            }
            values.deleteCharAt((values.length() - 1));
            PreparedStatement pstmt = Access.connection.prepareStatement((String.format(UPDATE_OBJECT_SQL, className, values.toString())), Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            for (Object key : keySet) {
                try {
                    pstmt.setObject(i, json.get(key));
                } catch (IllegalArgumentException ex) {
                }
                i++;

            }
            pstmt.setObject(i, id);
            pstmt.execute();
        } catch (SQLException e) {
            System.out.println(e);

            return null;
        }

        return JSON.successResponse();
    }

    /**
     * Deletes an object.
     *
     * @param object
     * @param id
     * @return returns a JSONObject with a success message if deletion worked,
     * returns null otherwise.
     */
    public static JSONObject delete(Class object, long id) {
        JSONObject result = JSON.successResponse();
        try {
            String className = object.getSimpleName();
            PreparedStatement pstmt = Access.connection.prepareStatement((String.format(DELETE_OBJECT_SQL, className)), Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, id);

            pstmt.execute();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
        } catch (SQLException e) {
            System.out.println(e);

            return null;
        }
        return result;

    }

    /**
     * Lists all objects
     *
     * @param object
     * @return returns a JSONObject of JSONObjects of objects with their Ids.
     * Returns null if there are no objects.
     */
    public static JSONObject list(Class object) {

        JSONObject result = new JSONObject();
        try {
            String className = object.getSimpleName();
            PreparedStatement pstmt = Access.connection.prepareStatement((String.format(LIST_OBJECT_SQL, className)), Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = pstmt.executeQuery();
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

        } catch (SQLException e) {
            System.out.println(e);

            return null;
        }
        return result;
    }

    /**
     * Returns all the field of a class and its superclasses.
     *
     * @param type
     * @return returns an array of Fields.
     */
    public static Field[] getAllFields(Class<?> type) {
        ArrayList<Field> fields = new ArrayList<>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return (fields.toArray(new Field[fields.size()]));
    }
}
