package net.dragonclaw.sqlite;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDatabase {

    private Connection c; // the connection to the database

    public SQLiteDatabase(String name) {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + name + ".db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTable(String name, Class<? extends SQLDataType> reference) {
        if (reference == null) {
            return;
        }
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE IF NOT EXISTS " + name.toUpperCase() + " (");
            Statement stmt = c.createStatement();
            boolean first = true;
            for (Field field : reference.getFields()) {
                SQLField properties = field.getAnnotation(SQLField.class);
                if (properties != null) {
                    if (first) {
                        builder.append(field.getName() + " " + properties.dataType().toUpperCase());
                        first = false;
                    } else {
                        builder.append(", " + field.getName() + " " + properties.dataType().toUpperCase());
                    }
                    if (properties.notNull()) {
                        builder.append(" NOT NULL");
                    }
                    if (properties.primaryKey()) {
                        builder.append(" PRIMARY KEY");
                    }
                    if (properties.unique()) {
                        builder.append(" UNIQUE");
                    }
                    if (properties.autoIncrement()) {
                        builder.append(" AUTOINCREMENT");
                    }
                }
            }
            // TODO something with safely adding new fields
            builder.append(");");
            stmt.executeUpdate(builder.toString());
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * creating a new record
     */
    public SQLResult insertIntoTable(String name, SQLDataType type) {
        if (type == null) {
            return new SQLResult("the data type is null");
        }
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("INSERT INTO " + name.toUpperCase() + " (");
            Statement stmt = c.createStatement();
            boolean first = true;
            for (Field field : type.getClass().getFields()) {
                SQLField properties = field.getAnnotation(SQLField.class);
                if (properties != null) {
                    if (properties.autoIncrement()) {
                        continue;
                    }
                    if (first) {
                        builder.append(field.getName());
                        first = false;
                    } else {
                        builder.append(", " + field.getName());
                    }
                }
            }
            first = true;
            builder.append(") VALUES (");
            for (Field field : type.getClass().getFields()) {
                SQLField properties = field.getAnnotation(SQLField.class);
                if (properties != null) {
                    if (properties.autoIncrement()) {
                        continue;
                    }
                    if (first) {
                        builder.append("'" + field.get(type).toString() + "'");
                        first = false;
                    } else {
                        builder.append(", '" + field.get(type).toString() + "'");
                    }
                }
            }
            builder.append(");");
            stmt.execute(builder.toString());
            stmt.close();
            return new SQLResult("succes", true);
        } catch (SQLException e) {
            return new SQLResult(e.getErrorCode() + "");
        } catch (IllegalArgumentException | IllegalAccessException e) {
            return new SQLResult(e.getMessage());
        }
    }

    /**
     * updating a record
     */
    public SQLResult updateTable(String name, SQLDataType type) {
        if (type == null) {
            return new SQLResult("the data type is null");
        }
        if (!type.isValid()) {
            return new SQLResult("the data type is not valid");
        }
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("UPDATE " + name.toUpperCase() + " SET ");
            Statement stmt = c.createStatement();
            boolean first = true;
            for (Field field : type.getClass().getFields()) {
                SQLField properties = field.getAnnotation(SQLField.class);
                if (properties != null) {
                    if (properties.autoIncrement()) {
                        continue;
                    }
                    if (first) {
                        builder.append(field.getName() + " = '" + field.get(type).toString() + "'");
                        first = false;
                    } else {
                        builder.append(", " + field.getName() + " = '" + field.get(type).toString() + "'");
                    }
                }
            }
            first = true;
            builder.append(" WHERE ");
            for (Field field : type.getClass().getFields()) {
                SQLField properties = field.getAnnotation(SQLField.class);
                if (properties != null) {
                    if (!properties.primaryKey()) {
                        continue;
                    }
                    if (first) {
                        builder.append(field.getName() + " = '" + field.get(type).toString() + "'");
                        first = false;
                    } else {
                        builder.append(", " + field.getName() + " = '" + field.get(type).toString() + "'");
                    }
                }
            }
            builder.append(";");
            // TODO something with safely adding new fields
            stmt.executeUpdate(builder.toString());
            stmt.close();
            return new SQLResult("succes", true);
        } catch (SQLException e) {
            return new SQLResult(e.getErrorCode() + "");
        } catch (IllegalArgumentException | IllegalAccessException e) {
            return new SQLResult(e.getMessage());
        }
    }

    public SQLResult getAllFromTable(String name, Class<? extends SQLDataType> reference) {
        return getFromTable(name, reference, new SQLGetter[] {});
    }

    public SQLResult getFromTable(String name, Class<? extends SQLDataType> reference, SQLGetter getter) {
        return getFromTable(name, reference, new SQLGetter[] {getter});
    }

    public SQLResult getFromTable(String name, Class<? extends SQLDataType> reference, SQLGetter... getters) {
        if (reference == null || getters == null) {
            return new SQLResult("the data type is null");
        }
        StringBuilder builder = new StringBuilder();
        if (getters.length >= 1) {
            builder.append("SELECT * FROM " + name.toUpperCase() + " WHERE ");
        } else {
            builder.append("SELECT * FROM " + name.toUpperCase());
        }
        boolean first = true;
        for (SQLGetter getter : getters) {
            if (getter != null) {
                if (first) {
                    builder.append(getter.name + " = '" + getter.value + "'");
                    first = false;
                } else {
                    builder.append(" AND " + getter.name + " = '" + getter.value + "'");
                }
            }
        }
        builder.append(";");
        try {
            Statement stmt = c.createStatement();
            ResultSet set = stmt.executeQuery(builder.toString().replace("*", "COUNT(*) AS total"));
            int count = set.getInt("total");
            set.close();
            stmt.close();
            return executeStatement(builder.toString(), count, reference);
        } catch (SQLException e) {
            return new SQLResult(e.getErrorCode() + "");
        }
    }

    private <T extends SQLDataType> SQLResult executeStatement(String sql, int count, Class<T> reference) {
        try {
            SQLResult result = new SQLResult("");
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (count == 1) {
                T type = reference.newInstance();
                for (Field field : reference.getFields()) {
                    SQLField properties = field.getAnnotation(SQLField.class);
                    if (properties != null) {
                        Constructor<?> constructor = field.getType().getConstructor(String.class);
                        String str = rs.getObject(field.getName()).toString();
                        Object obj = constructor.newInstance(str);
                        field.set(type, obj);
                    }
                }
                result = new SQLResult(type);
            }
            while (rs.next()) {

            }
            rs.close();
            stmt.close();
            return result;
        } catch (SQLException e) {
            return new SQLResult(e.getErrorCode() + "");
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException e) {
            return new SQLResult(e.getMessage());
        }

    }
}
