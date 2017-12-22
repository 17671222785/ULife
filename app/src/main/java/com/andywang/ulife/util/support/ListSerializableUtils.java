package com.andywang.ulife.util.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by parting_soul on 2016/11/8.
 */

public class ListSerializableUtils {

    public static <T> List<T> copyList(List<T> src) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        List<T> dest = null;
        try {
            objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(src);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(out.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteIn);

            dest = (List<T>) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dest;
    }
}
