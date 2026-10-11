package com.util;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCTool {


    public static <T> List<T> resultSetToList(ResultSet rs, Class<T> clazz) throws Exception {
        List<T> list = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()) {
            T obj = clazz.newInstance();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object value = rs.getObject(i);
                Field field = clazz.getDeclaredField(columnName);
                field.setAccessible(true);
                field.set(obj, value);
            }
            list.add(obj);
        }
        return list;
    }

    /**
     * 保存新增对象
     * @param obj 实体对象
     * @param connection 数据库连接
     * @return 受影响行数
     */
    public static <T> int save(T obj, Connection connection) throws Exception {
        Class<?> clazz = obj.getClass();
        String tableName = clazz.getSimpleName().toLowerCase();
        Field[] fields = clazz.getDeclaredFields();

        StringBuilder colNames = new StringBuilder();
        StringBuilder placeHolder = new StringBuilder();
        List<Object> paramList = new ArrayList<>();

        for (Field field : fields) {
            field.setAccessible(true);
            Object val = field.get(obj);
            // id自增，id为null不插入
            if ("id".equals(field.getName()) && val == null) {
                continue;
            }
            colNames.append(field.getName()).append(",");
            placeHolder.append("?,");
            paramList.add(val);
        }
        if (colNames.length() > 0) {
            colNames.deleteCharAt(colNames.length() - 1);
            placeHolder.deleteCharAt(placeHolder.length() - 1);
        }
        String sql = "insert into " + tableName + "(" + colNames + ") values(" + placeHolder + ")";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        for (int i = 0; i < paramList.size(); i++) {
            pstmt.setObject(i + 1, paramList.get(i));
        }
        int rows = pstmt.executeUpdate();
        close(pstmt, null);
        return rows;
    }

    /**
     * 更新对象
     * @param obj 带id的实体
     * @param connection 连接
     * @return 受影响行数
     */
    public static <T> int update(T obj, Connection connection) throws Exception {
        Class<?> clazz = obj.getClass();
        String tableName = clazz.getSimpleName().toLowerCase();
        Field[] fields = clazz.getDeclaredFields();

        StringBuilder sqlBuilder = new StringBuilder("update " + tableName + " set ");
        List<Object> paramList = new ArrayList<>();
        Object idValue = null;

        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object val = field.get(obj);
            if ("id".equals(fieldName)) {
                idValue = val;
                continue;
            }
            sqlBuilder.append(fieldName).append("=?,");
            paramList.add(val);
        }
        sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        sqlBuilder.append(" where id=?");
        paramList.add(idValue);

        PreparedStatement pstmt = connection.prepareStatement(sqlBuilder.toString());
        for (int i = 0; i < paramList.size(); i++) {
            pstmt.setObject(i + 1, paramList.get(i));
        }
        int rows = pstmt.executeUpdate();
        close(pstmt, null);
        return rows;
    }

    /**
     * 删除对象，根据对象内部id删除
     * @param obj 带id实体
     * @param connection 连接
     * @return 受影响行数
     */
    public static <T> int delete(T obj, Connection connection) throws Exception {
        Class<?> clazz = obj.getClass();
        String tableName = clazz.getSimpleName().toLowerCase();
        Field idField = clazz.getDeclaredField("id");
        idField.setAccessible(true);
        Object idVal = idField.get(obj);

        String sql = "delete from " + tableName + " where id=?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setObject(1, idVal);
        int rows = pstmt.executeUpdate();
        close(pstmt, null);
        return rows;
    }

    /**
     * 根据id查询单个对象
     * @param id 编号字符串
     * @param clazz 实体Class
     * @param connection 连接
     * @return 实体对象
     */
    public static <T> T getOneById(String id, Class<T> clazz, Connection connection) throws Exception {
        String tableName = clazz.getSimpleName().toLowerCase();
        String sql = "select * from " + tableName + " where id=?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, id);
        ResultSet rs = pstmt.executeQuery();
        List<T> list = resultSetToList(rs, clazz);
        close(pstmt, rs);
        return list.isEmpty() ? null : list.get(0);
    }

    //辅助方法：关闭资源
    public static void close(PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static Connection getConnection() throws SQLException {
        // 修改：localhost换成你的服务器名，sa密码改成你自己的SQL Server密码
        String url = "jdbc:sqlserver://localhost:1433;databaseName=DateTest;encrypt=true;trustServerCertificate=true;";
        String user = "sa";
        String password = "hpm060612HPM";
        return DriverManager.getConnection(url, user, password);
    }
}
