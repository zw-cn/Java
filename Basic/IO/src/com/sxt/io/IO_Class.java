package com.sxt.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;

public class IO_Class {
    /*IO中包含4个大类
     * 1.InputStream
     *  *close()
     *  *read()
     * 2.Reader
     *  *close()
     *  *read()
     * 3.OutputStream
     *  *close()
     *  *write()
     *  *flush()
     * 4.Writer
     *  *close()
     *  *write()
     *  *flush()
     */
    @Test
    public void basicIO() {
        File src;
        InputStream is = null;
        try {
            //（1/4）创建源
            src = new File("resources/basicIoTest.txt");
            //（2/4）选择流
            is = new FileInputStream(src);
            //（3/4）操作流
            int temp;
            while ((temp = is.read()) != -1) {
                System.out.print((char) temp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //（4/4）释放流
            try {
                if (null != is) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void inputSteamWithBuffer() {
        File src = new File("resources/basicIoTest.txt");
        InputStream is = null;
        try {
            is = new FileInputStream(src);
            byte[] buffer = new byte[3];
            int len = -1;//
            while ((len = is.read(buffer)) != -1) {
                String str = new String(buffer, 0, len);
                System.out.println(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void outputStreamWithByte() {
        File src = new File("resources/basicOutputTest.txt");
        OutputStream os = null;
        try {
            //os = new FileOutputStream(src);
            os = new FileOutputStream(src, true);
            String str = "hello outputStream\r\n";
            byte[] datas = str.getBytes();
            os.write(datas, 0, datas.length);
            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ParameterizedTest(name = "第{index}组 -> inputFilePath={0}, outputFilePath={1}")
    @CsvSource({"resources/EasyConnect.ico,resources/fileCopy.ico"})
    public void copyFile(String inputFilePath, String outputFilePath) {
        InputStream is = null;
        OutputStream os = null;
        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);
        try {
            is = new FileInputStream(inputFile);
            os = new FileOutputStream(outputFile);
            byte[] datas = new byte[1024];
            int len = -1;
            while ((len = is.read(datas)) != -1) {
                os.write(datas, 0, len);
            }
            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @ParameterizedTest(name = "第{index}组 -> srcPath={0}, destPath={1}")
    @CsvSource({"resources,resourcesCopy"})
    public void copyDir(String srcPath, String destPath) {
        File srcFile = new File(srcPath);
        File destFile = new File(destPath);
        if (null == srcFile && !srcFile.exists()) {
            return;
        } else {
            File temp = new File(destFile, srcFile.getName());//temp指向目标的相应位置
            if (srcFile.isDirectory()) {
                //若源为目录，在目标位置创建目录，并再次递归
                temp.mkdirs();
                for (File files :
                        srcFile.listFiles()) {
                    copyDir(files.getPath(), temp.getPath());
                }
            } else {
                //若源为文件，copy源到目标位置
                copyFile(srcPath, temp.getPath());
            }
        }
    }
    /*字符操作类
     * Reader
     * Writer
     */
    @Test
    public void readFile() {
        File src = new File("resources/hello/keeplearning.txt");
        Reader reader = null;
        try {
            reader = new FileReader(src);
            char[] cbuf = new char[1024];
            int len = -1;
            while ((len = reader.read(cbuf)) != -1){
                String str = new String(cbuf, 0, len);
                System.out.println(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null != reader){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @ParameterizedTest
    @ValueSource(strings = {"resources/hello/writeFile.txt"})
    public void writeFile(String destPath) {
        File src = new File(destPath);
        Writer writer = null;
        try {
            writer = new FileWriter(src);
            //写法1
//            String msg = "你好，Java Writer。\r\n很高兴认识你!--写法1";
//            char[] cbuf = msg.toCharArray();
//            writer.write(cbuf, 0, cbuf.length);

            //写法2
//            String msg = "你好，Java Writer。\r\n很高兴认识你!--写法2";
//            writer.write(msg);
            //写法3
            writer.append("你好，Java Writer。\r\n").append("很高兴认识你!--写法3");

            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 纯文本方式拷贝文件
     * @param srcFilePath
     * @param destFilePath
     */
    @ParameterizedTest(name = "参数{index} -> srcFilePath={0},destFilePath={1}")
    @CsvSource({"resources/hello/keeplearning.txt,resources/hello/copy_keeplearning.txt"})
    public void copyFileByPlainText(String srcFilePath,String destFilePath) {
        File srcFile = new File(srcFilePath);
        File destFile = new File(destFilePath);
        Reader reader = null;
        Writer writer = null;
        if (srcFile != null && srcFile.isFile()){
            try {
                reader = new FileReader(srcFile);
                writer = new FileWriter(destFile);
                char[] cbuf = new char[1024];
                int len = -1;
                while ((len = reader.read(cbuf)) != -1){
                    writer.write(cbuf, 0, len);
                }
                writer.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != writer){
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(null != reader){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
