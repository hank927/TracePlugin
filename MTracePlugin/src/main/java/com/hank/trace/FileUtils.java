package com.hank.trace;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * author : Administrator
 * date   : 2020/5/15
 * desc   : 文件读取
 */
public class FileUtils {

    public static String readFileAsString(String fullFilename) {
        StringBuffer fileData = new StringBuffer();
        InputStreamReader fileReader = null;
        int BUFFER_SIZE = 16384;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(fullFilename);
            fileReader = new InputStreamReader(inputStream, "UTF-8");
            char[] buf = new char[BUFFER_SIZE];
            int numRead = fileReader.read(buf);
            while (numRead != -1) {
                String readData = new String(buf, 0, numRead);
                fileData.append(readData);
                numRead = fileReader.read(buf);
            }
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                    fileReader = null;
                }
                if (inputStream != null) {
                    inputStream.close();
                    inputStream = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileData.toString();
    }
}
