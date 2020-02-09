package com.sloera.mng.core.db;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecordBuilder {
    public RecordBuilder() {
    }

    public static final List<Record> build(ResultSet resultSet) throws SQLException {
        ArrayList arrayList = new ArrayList();
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columnSize = resultSetMetaData.getColumnCount();
        String[] columnLabel = new String[columnSize + 1];
        int[] columnType = new int[columnSize + 1];
        getColumn(resultSetMetaData, columnLabel, columnType);
        while (resultSet.next()) {
            Record record = new Record();
            Map map = record.getColumns();
            for (int i = 1; i <= columnSize; i++) {
                Object object;
                if (columnType[i] < 2004) {
                    object = resultSet.getObject(i);
                } else if (columnType[i] == 2005) {
                    object = handleClob(resultSet.getClob(i));
                } else if (columnType[i] == 2011) {
                    object = handleClob(resultSet.getNClob(i));
                } else if (columnType[i] == 2004) {
                    object = handleBlob(resultSet.getBlob(i));
                } else {
                    object = resultSet.getObject(i);
                }
                map.put(columnLabel[i], object);
            }
            arrayList.add(record);
        }
        return arrayList;
    }

    private static String handleClob(Clob clob) throws SQLException {
        if (null == clob) {
            return null;
        } else {
            Reader reader = null;
            String res;
            try {
                reader = clob.getCharacterStream();
                char[] chars = new char[(int) clob.length()];
                reader.read(chars);
                res = new String(chars);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return res;
        }
    }

    private static byte[] handleBlob(Blob blob) throws SQLException {
        if (null == blob) {
            return null;
        } else {
            InputStream inputStream = null;
            byte[] bytes;
            try {
                inputStream = blob.getBinaryStream();
                byte[] bytes1 = new byte[(int) blob.length()];
                inputStream.read(bytes1);
                inputStream.close();
                bytes = bytes1;
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (null != inputStream) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return bytes;
        }
    }

    private static final void getColumn(ResultSetMetaData resultSetMetaData, String[] columnLabel, int[] columnType) throws SQLException {
        for (int i = 1; i < columnLabel.length; i++) {
            columnLabel[i] = resultSetMetaData.getColumnLabel(i);
            columnType[i] = resultSetMetaData.getColumnType(i);
        }
    }
}
