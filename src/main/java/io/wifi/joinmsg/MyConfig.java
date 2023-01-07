package io.wifi.joinmsg;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** */
/**
 * 读取properties文件
 * 
 * @author Qutr
 *
 */
public class MyConfig {
    private Properties propertie;
    private FileInputStream inputFile;
    private FileOutputStream outputFile;

    /** */
    /**
     * 初始化Configuration类
     */
    public MyConfig() {
        propertie = new Properties();
    }

    /** */
    /**
     * 初始化Configuration类
     * 
     * @param filePath 要读取的配置文件的路径+名称
     */
    public void WriteDefaultConfig(File filePath) {
        // return true;

        try {

            OutputStream outputStream = new FileOutputStream(filePath, false);// true表示拼接
            String str1 = "url=http://127.0.0.1/msg\r\njoin_prefix=Player \r\nleave_prefix=Player \r\njoin_after= Joined\r\nleave_after= Left\r\nserverStart=Server Has Started";
            outputStream.write(str1.getBytes("UTF-8"));// 只能输出字节数组
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[Config] Successfully wrote the default config.");
        // System.out.println("Done");

    }

    public MyConfig(String filePath) {
        File FilePath = new File(filePath);
        if (!FilePath.exists()) {
            WriteDefaultConfig(FilePath);
        }
        propertie = new Properties();
        try {
            inputFile = new FileInputStream(filePath);
            propertie.load(new InputStreamReader(inputFile, "UTF-8"));
            inputFile.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Read Config Failed! - Reason: " + filePath + " is not a file.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Read Config failed! - Reason: Unknown IOException");
            ex.printStackTrace();
        }
    }// end ReadConfigInfo(...)

    public void reloadConfig(String filePath) {
        propertie = new Properties();
        try {
            inputFile = new FileInputStream(filePath);
            propertie.load(new InputStreamReader(inputFile, "UTF-8"));
            inputFile.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Read Config Failed! - Reason: " + filePath + " is not a file.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Read Config failed! - Reason: Unknown IOException");
            ex.printStackTrace();
        }
    }

    /** */
    /**
     * 重载函数，得到key的值
     * 
     * @param key 取得其值的键
     * @return key的值
     */
    public String getValue(String key) {
        if (propertie.containsKey(key)) {
            String value = propertie.getProperty(key);// 得到某一属性的值
            return value;
        } else
            return "";
    }// end getValue(...)

    /** */
    /**
     * 重载函数，得到key的值
     * 
     * @param fileName properties文件的路径+文件名
     * @param key      取得其值的键
     * @return key的值
     */
    public String getValue(String fileName, String key) {
        try {
            String value = "";
            inputFile = new FileInputStream(fileName);
            propertie.load(inputFile);
            inputFile.close();
            if (propertie.containsKey(key)) {
                value = propertie.getProperty(key);
                return value;
            } else
                return value;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }// end getValue(...)

    /** */
    /**
     * 清除properties文件中所有的key和其值
     */
    public void clear() {
        propertie.clear();
    }// end clear();

    /** */
    /**
     * 改变或添加一个key的值，当key存在于properties文件中时该key的值被value所代替，
     * 当key不存在时，该key的值是value
     * 
     * @param key   要存入的键
     * @param value 要存入的值
     */
    public void setValue(String key, String value) {
        propertie.setProperty(key, value);
    }// end setValue(...)

    /** */
    /**
     * 将更改后的文件数据存入指定的文件中，该文件可以事先不存在。
     * 
     * @param fileName    文件路径+文件名称
     * @param description 对该文件的描述
     */
    public void saveFile(String fileName, String description) {
        try {
            outputFile = new FileOutputStream(fileName);
            propertie.store(outputFile, description);
            outputFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }// end saveFile(...)
}// end class ReadConfigInfo config {
