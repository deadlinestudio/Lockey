package com.deadlinestudio.lockey.control;

import android.util.JsonReader;
import android.util.Log;

import com.deadlinestudio.lockey.model.Data;
import com.deadlinestudio.lockey.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @biref Class to exchange JSON data with server
 */
public class ReadJSON {

    /**
     * @biref method to call private method named readUser.
     * to be used from RequestHttpConnection Class
     * @param in Inputstream object
     * @return User object received from server
     * @throws IOException
     */
    public User readJsonUser(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readUser(reader);
        } finally {
            reader.close();
        }
    }

    /**
     * @biref receive user data of JSON type from server and parse it.
     * @param reader JsonReader Object
     * @return User received data from server
     * @throws IOException
     */
    private User readUser(JsonReader reader) throws IOException {
        User user = User.getInstance();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                user.setId(reader.nextString());
            } else if (name.equals("nickname")) {
                user.setNickname(reader.nextString());
                Log.e("read_nickname", user.getNickname());
            } else if (name.equals("job")) {
                user.setJob(reader.nextString());
                Log.e("read_job", user.getJob());
            } else if (name.equals("age")) {
                user.setAge(reader.nextInt());
            } else if (name.equals("isUser")) {
                user.setisUser((reader.nextString()).equals("yes") ? true : false);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return user;
    }

    /**
     * @brief  method to call private method named readTimeArray.
     * to be used from RequestHttpConnection Class
     * @param in InputStream Object
     * @return ArrayList<Data>
     * @throws IOException
     */
    public HashMap<String, Long> readJsonTime(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            Log.d("classify", "readJSONTime()");
            return readTimeArray(reader);
        } finally {
            reader.close();
        }

    }

    /**
     * @brief  method to call private method named readTime.
     * @param reader JsonRead Object
     * @return ArrayList<Data> data list received from server
     * @throws IOException
     */
    private HashMap<String, Long> readTimeArray(JsonReader reader) throws IOException {
        HashMap<String, Long> classfied_data = new HashMap<>();
        reader.beginArray();
        while (reader.hasNext()) {
            classfied_data.putAll(readTime(reader));
        }
        reader.endArray();
        Log.d("classify", "readTimeArray()");

        return classfied_data;
    }

    /**
     * @biref receive time data of JSON type from server and parse it.
     * @param reader JsonReader Object
     * @return Data data received from server
     * @throws IOException
     */
    private HashMap<String, Long> readTime(JsonReader reader) throws IOException {
        HashMap<String, Long> data = new HashMap<>();
        reader.beginObject();
        String key = "";
        long val = 0;
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                key = reader.nextString();
            } else if (name.equals("amount")) {
                String amount = reader.nextString();
                if(amount.equals("None"))   val = 0;
                else                        val = Long.parseLong(amount)/60;
            } else {
                reader.skipValue();
            }
        }
        data.put(key, val);
        reader.endObject();
        Log.d("classify", "readTime()");
        return data;
    }
}
