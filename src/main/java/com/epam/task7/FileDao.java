package com.epam.task7;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.*;

public class FileDao {
    private String src = "C:\\Users\\Maxim_Mandzhiev\\Documents\\batch_actions.txt";
    private String dst = "C:\\Users\\Maxim_Mandzhiev\\Documents\\recieve111.txt";

    public static void main(String[] args) throws ClassNotFoundException {
        FileDao fileDao = new FileDao();
//        boolean save = fileDao.save(fileDao.src, "batch_actions.txt");
//        System.out.println(save);
        fileDao.recieve(1, fileDao.dst);
    }

    public boolean save(String filePath, String fileName) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "max", "1111");
             FileInputStream fis = new FileInputStream(new File(filePath))) {
            CallableStatement callStat = conn.prepareCall("CALL save_file(?,?)");

            callStat.setString(1, fileName);
            callStat.setBinaryStream(2, fis);
            return callStat.execute();
        } catch (SQLException | FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void recieve(int id, String filePath) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "max", "1111");
             FileOutputStream fos = new FileOutputStream(filePath)) {
            CallableStatement callStat = conn.prepareCall("CALL recieve_file(?, ?,?)");

            callStat.setInt(1, id);
            callStat.registerOutParameter(2, Types.VARCHAR);
            callStat.registerOutParameter(3, Types.BLOB);
            callStat.execute();
            ByteBuffer buffer = ByteBuffer.wrap(callStat.getBytes(3));
            FileChannel channel = fos.getChannel();

            channel.write(buffer);

            channel.close();
            fos.close();
        } catch (SQLException | FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
