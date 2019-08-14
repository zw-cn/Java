package com.sxt.io;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;

public class IO_Class_ByteArray {
    /*
     * 1.数据源为字节数组
     * 2.可以不用关闭（GC自动关闭)
     * 3.任何东西都能转换成字符数组
     * 4.字节数组不要太大
     */

    @ParameterizedTest
    @ValueSource(strings = {"talk is cheap, show me the code!"})
    public void byetArrayInputStream(String msg) {
        InputStream is = new ByteArrayInputStream(msg.getBytes());
        byte[] cbuf = new byte[5];
        int len = -1;
        try {
            while ((len = is.read(cbuf)) != -1) {
                String str = new String(cbuf, 0, len);
                System.out.println(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"talk is cheap, show me the code!"})
    public void byteArrayOutputStream(String msg) {
        //无需指定目的地
        byte[] destByte = null;
        //toByteArray()是ByteArrayOutputStream特有的方法，因此不能使用多态的方式调用它
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            os.write(msg.getBytes(), 0, msg.getBytes().length);//由jvm指定内存，并写入
            os.flush();
            destByte = os.toByteArray();//获取内存中的数据
            System.out.println(new String(destByte, 0, os.size()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest(name = "srcPath = {0}, destPath = {1}")
    @CsvSource({"resources/hello/keeplearning.txt,resources/hello/byteReadFileWrite.txt",
            "resources/EasyConnect.ico,resources/Copy_EasyConnect.ico"})
    public void FileToFileByByteArray(String srcPath, String destPath) {
        File srcFile = new File(srcPath);
        File descFile = new File(destPath);
        InputStream is = null;
        OutputStream os = null;
        ByteArrayInputStream bis = null;
        ByteArrayOutputStream bos = null;
        try {
            is = new FileInputStream(srcFile);
            bos = new ByteArrayOutputStream();
            byte[] bbuf = new byte[1024];
            int len = -1;
            while ((len = is.read(bbuf)) != -1) {
                bos.write(bbuf, 0, len);
            }
            bos.flush();

            bis = new ByteArrayInputStream(bos.toByteArray(), 0, bos.size());//bos写入内存，设置bis为进行内存字节读取的源
            os = new FileOutputStream(descFile, false);//开启追加，防止覆盖
            bbuf = new byte[2];//重新指定字节数组（变相清空）
            len = -1;
            while ((len = bis.read(bbuf)) != -1) {
                os.write(bbuf, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeMe(os, bis, bos, is);
        }
    }

    /**
     * 将实现Closeabel接口的所有对象进行关闭，按照参数列表顺序进行关闭
     *
     * @param closeableArray
     */
    public void closeMe(Closeable... closeableArray) {
        for (Closeable c :
                closeableArray) {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
