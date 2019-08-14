package com.sxt.io;

import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

public class IO_File_Charset {
    /*编码
     * 字符 --> 字节
     * */
    @Test
    public void encode() {
        String msg = "性命 生命 使命";
        //编码
        byte[] datas = msg.getBytes();//默认使用工程字符集
        System.out.println(datas.length);
        //编码成其他字符集
        try {
            datas = msg.getBytes("UTF-16LE");
            System.out.println(datas.length);
            datas = msg.getBytes("GBK");
            System.out.println(datas.length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /*解码
     * 字节 --> 字符
     * */
    @Test
    public void decode() throws UnsupportedEncodingException {
        String msg = "性命 生命 使命";
        //编码
        byte[] datas = msg.getBytes();//默认使用工程字符集

        //解码：String(byte[] bytes, int offset, int length, Charset charset)
        String msgdecode = new String(datas, 0, datas.length, "utf8");
        System.out.println(msgdecode);
        /**乱码
         *原因：
         * 1.字节数不够
         * 2.字符集不统一
         */
        //1.字节数不够
        msgdecode = new String(datas, 0, datas.length-1, "utf8");
        System.out.println(msgdecode);
        //2.字符集不统一
        msgdecode = new String(datas, 0, datas.length, "GBK");
        System.out.println(msgdecode);

    }



}
