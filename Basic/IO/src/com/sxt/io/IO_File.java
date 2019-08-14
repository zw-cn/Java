package com.sxt.io;

import com.junit5.parameter.FileResolver;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;


public class IO_File {
    /**
     * File类常用方法
     * 名称或路径
     * getName():返回名称
     * getPath():相对->相对；绝对->绝对
     * getAbsolutePath():绝对路径
     * getParent();上路径。不存在->null;
     */
    @Test
    public void fileMethods() throws IOException {
        String key = "全部2";
        switch (key) {
            case "全部" -> key = "基本信息";
            case "全部2", "基本信息" -> System.out.println("5");
            case "文件状态" -> {
                System.out.println("6");
                System.out.println("7");
            }

        }
        System.out.println("fileMethods");
        File src = new File("resources/EasyConnect.ico");
        System.out.println(System.getProperty("user.dir"));
        System.out.println(src.length());
        /*基本信息*/
        System.out.println("名称:" + src.getName());
        System.out.println("路径:" + src.getPath());
        System.out.println("绝对路径:" + src.getAbsolutePath());
        System.out.println("父路径:" + src.getParent());
        System.out.println("父对象:" + src.getParentFile().getName());
        /*文件状态*/
        /**
         * 存在 ?
         *   -否 ->
         *   -是 -> 是否为文件 ? */
        System.out.println("是否存在:" + src.exists());
        System.out.println("是否为文件:" + src.isFile());
        System.out.println("是否为文件夹:" + src.isDirectory());
        File src2 = new File("resources");
        System.out.println("是否存在:" + src2.exists());
        System.out.println("是否为文件:" + src2.isFile());
        System.out.println("是否为文件夹:" + src2.isDirectory());
        /**
         * 文件状态范例
         */
        File file = new File("XX");
        if (null == file || !file.exists()) {
            System.out.println("文件不存在！");
        } else {
            if (file.isFile()) {
                System.out.println("文件操作。。。");
            } else {
                System.out.println("目录操作。。。");
            }
        }
        /*其他信息*/
        File fileOther = new File(src.getPath());
        System.out.println("长度" + fileOther.length());
        File dirOther = new File(src.getParent());
        System.out.println("长度" + dirOther.length());//文件夹的字节长度为0

        File newFile = new File("newFile.txt");
        boolean flag = newFile.createNewFile();//不存在->创建成功；存在->创建失败
        System.out.println(newFile.getName() + "是否创建成功:" + flag);
        File newDir = new File("resources/dir");
        flag = newDir.createNewFile();//文件夹不能被创建，保存成dir文件
        //con,com[1-9]...等为OS关键字，不能创建成功
        System.out.println(newDir.getName() + "是否创建成功:" + flag);

        flag = newFile.delete();//删除
        System.out.println(newFile.getName() + "是否删除成功:" + flag);
        flag = newDir.delete();
        System.out.println(newDir.getName() + "是否删除成功:" + flag);

        /*文件夹操作
         * mkdir() -> 要求父目录必须存在
         * mkdirs() -> 父目录不存在，会直接创建父目录
         *
         * list() -> 下一级名称（非子孙级）
         * listFiles -> 下一级File对象
         * listRoots() -> 根路径
         */
        File dir = new File("D:/study/Java/Basic/IO/testDir/Dir");
        flag = dir.mkdir();
        System.out.println("mkdir()->路径：" + dir.getAbsoluteFile() + "是否创建成功?" + flag);
        flag = dir.mkdirs();
        System.out.println("mkdirs()->路径：" + dir.getAbsoluteFile() + "是否创建成功?" + flag);


        //列出下级名称
        File dirList = new File("D:/study/Java/Basic/IO");
        String[] subDir = dirList.list();
        for (String name :
                subDir) {
            System.out.println(name);
        }
        System.out.println("--------------------------");
        //获取下级文件对象
        File[] subFile = dirList.listFiles();
        for (File innerFile :
                subFile) {
            System.out.println(innerFile.getName());
        }
        //列出所有盘符
        System.out.println("--------------------------");
        File[] roots = dirList.listRoots();
        for (File f :
                roots) {
            System.out.println(f.getAbsolutePath());
        }
    }

    /**
     * 递归举例
     *
     * @param value
     */
    @ParameterizedTest
    @ValueSource(ints = {1, 5})
    public void recursive(int value) {
        if (value > 10) {
            return;
        }
        System.out.println(value);
        recursive(value + 1);
    }

    /*打印子孙级目录和文件的名称*/
    @ParameterizedTest(name = "{index} ==> deep={0}")
    @CsvSource({"0"})
    @ExtendWith(FileResolver.class)
    public void printName(int deep, File src) {
        for (int i = 0; i < deep; i++) {
            System.out.print("-");
        }
        System.out.println(src.getName());
        if (src == null || !src.exists()) {
            return;
        } else {
            if (src.isDirectory()) {
                for (File f :
                        src.listFiles()) {
                    printName(deep + 1, f);
                }
            }
        }
    }

    /*文件夹大小*/
    static long size = 0;

    public void fileSize(File src) {
        if (null == src || !src.exists()) {
            return;
        } else {
            if (src.isFile()) {//大小
                size += src.length();
            } else {//子孙级
                for (File f :
                        src.listFiles()) {
                    fileSize(f);
                }
            }
        }
    }

    @Test
    @ExtendWith(FileResolver.class)
    public void fileSizeTest(File src) {
        //普通统计
        fileSize(src);
        System.out.println(size);
        //面向对象统计
        Dir dir = new Dir(System.getProperty("user.dir"));
        System.out.println("文件夹大小为："+dir.getSize()+"\t文件个数为："+dir.fileNum+"文件夹个数为："+dir.dirNum);

    }

    //面向对象统计文件夹大小
    private class Dir {
        private String path;
        private long size;
        private File src;
        private long dirNum = -1;//不计本文件夹，因此设置为-1。
        private long fileNum;

        public Dir(String path) {
            this.path = path;
            this.src = new File(path);
            countSize(this.src);
        }

        private void countSize(File file) {
            if (null != file && file.exists()) {
                if (file.isFile()) {//大小
                    fileNum++;
                    size += file.length();
                } else {
                    dirNum++;
                    for (File s :
                            file.listFiles()) {
                        countSize(s);
                    }
                }
            }
        }

        public long getSize() {
            return size;
        }

        public void setDirNum(long dirNum) {
            this.dirNum = dirNum;
        }

        public void setFileNum(long fileNum) {
            this.fileNum = fileNum;
        }
    }

    /*
     * junit5 参数化测试
     * */
    @ParameterizedTest(name = "第{index}组 -> a={0}, b={1}")
    @CsvSource({"foo, 15", "bar, 2", "'baz, qux', 3"})
    @ExtendWith({FileResolver.class})
    public void jTest(String a, long b, File file, File file2) {
        System.out.println(file.getAbsolutePath());
        System.out.println(file2.getAbsolutePath());
        System.out.println(a + b);
    }
}
