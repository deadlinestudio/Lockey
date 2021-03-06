package com.deadlinestudio.lockey.control;

import android.util.JsonReader;
import android.util.Log;

import com.deadlinestudio.lockey.model.Data;
import com.deadlinestudio.lockey.model.Time;
import com.deadlinestudio.lockey.model.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

/**
 * @brief Class to communicate android with server
 */
public class RequestHttpConnection {
    private static final String SERVER_ADDR = "http://thedeadlinestudio.com";
    private InputStream is = null;

    /**
     * @brief method to connect to the server
     * @param url the url you want to communicate with
     * @return Connected HttpURLConnection Object
     */
    private HttpURLConnection connectHTTP(String url) throws Exception {
        HttpURLConnection httpCon;
        try {
            URL urlCon = new URL(SERVER_ADDR + url);
            httpCon = (HttpURLConnection) urlCon.openConnection();
            httpCon.setRequestMethod("POST");
            httpCon.setRequestProperty("Accept", "application/json");
            httpCon.setRequestProperty("Content-type", "application/json");
            // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
            httpCon.setDoOutput(true);
            // InputStream으로 서버로 부터 응답을 받겠다는 옵션.
            httpCon.setDoInput(true);
            return httpCon;
        } catch (ProtocolException e) {
            throw e;
        } catch (MalformedURLException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * @brief method to get user data from server with specific id
     * @param url
     * @param id
     * @return User Object received from server
     */
    public void getUser(String url, String id) throws Exception {
        try {
            HttpURLConnection httpCon = connectHTTP(url);
            String json = "";

            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("id", id);

            // convert JSONObject to JSON to String
            json = jsonObject.toString();
            Log.e("post_data", json);

            OutputStream os = httpCon.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.flush();
            os.close();

            is = httpCon.getInputStream();
            ReadJSON readJson = new ReadJSON();
            try {
                // convert inputstream to string
                if(is != null) {
                    readJson.readJsonUser(is);
                    Log.e("isUser in requestHTTP", User.getInstance().getisUser() ? "yes" : "no");
                }
            }
            catch (Exception e) {
                throw e;
            }
            finally {
                is.close();
                httpCon.disconnect();
            }
        }
        catch (IOException e) {
            throw e;
        }
        catch (Exception e) {
            throw e;
        }
    }

    /**
     * @brief method to register user data to server
     * @param url
     * @param user
     * @return 'complete' if successful, 'fail' if not
     */
    public String registerUser(String url, User user) throws Exception {
        InputStream is = null;
        String result = "";
        try {
            HttpURLConnection httpCon = connectHTTP(url);

            String json = "";

            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("id", user.getId());
            jsonObject.accumulate("nickname", user.getNickname());
            jsonObject.accumulate("age", Integer.toString(user.getAge()));
            jsonObject.accumulate("job", user.getJob());

            // convert JSONObject to JSON to String
            json = jsonObject.toString();
            Log.e("post_data", json);

            OutputStream os = httpCon.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.flush();
            os.close();

            is = httpCon.getInputStream();
            try {
                // convert inputstream to string
                if(is != null)
                    result = convertInputStreamToString(is);
                else
                    result = "fail";

            }
            catch (Exception e) {
                throw e;
            }
            finally {
                is.close();
                httpCon.disconnect();
            }
        }
        catch (IOException e) {
            throw e;
        }
        catch (Exception e) {
            throw e;
        }
        return result;
    }

    /**
     * @brief method to update the user data to server
     * @param url
     * @param user
     * @return 'complete' if successful, 'fail' if not
     */
    public void updateUser(String url, User user) throws Exception {
        try {
            HttpURLConnection httpCon = connectHTTP(url);

            String json = "";

            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("id", user.getId());
            jsonObject.accumulate("nickname", user.getNickname());
            jsonObject.accumulate("job", user.getJob());

            // convert JSONObject to JSON to String
            json = jsonObject.toString();
            Log.e("update-user", json);

            OutputStream os = httpCon.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.flush();
            os.close();

            is = httpCon.getInputStream();
            try {
                if(is != null)
                    convertInputStreamToString(is);
            }
            catch (Exception e) {
                throw e;
            }
            finally {
                httpCon.disconnect();
            }
        }
        catch (IOException e) {
            Log.e("update-user", "IOEcxeption");
        }
        catch (Exception e) {
            Log.e("update-user", e.getMessage());
        }
    }

    /**
     * @brief method to reset data
     * @param url
     * @param id
     * @return 'complete' if successful, 'fail' if not
     */
    public void resetTime(String url, String id) throws Exception {
        try {
            HttpURLConnection httpCon = connectHTTP(url);

            String json = "";

            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("id", id);

            // convert JSONObject to JSON to String
            json = jsonObject.toString();

            OutputStream os = httpCon.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.flush();
            os.close();

            is = httpCon.getInputStream();
            try {
                if(is != null)
                    convertInputStreamToString(is);
            }
            catch (Exception e) {
                throw e;
            }
            finally {
                is.close();
                httpCon.disconnect();
            }
        }
        catch (IOException e) {
            throw e;
        }
        catch (Exception e) {
            throw e;
        }
    }

    /**
     * @brief method to get user data from server with specific id
     * @param url
     * @param id
     * @return Time Object received from server
     */
    public HashMap<String, Long> getClassfiedTime(String url, String id, String period) throws Exception {
        InputStream is = null;
        HashMap<String, Long> recv_data = null;
        try {
            HttpURLConnection httpCon = connectHTTP(url);
            String json = "";

            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("id", id);
            jsonObject.accumulate("period", period);

            // convert JSONObject to JSON to String
            json = jsonObject.toString();
            Log.e("post_data", json);

            OutputStream os = httpCon.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.flush();
            os.close();

            is = httpCon.getInputStream();
            ReadJSON readJson = new ReadJSON();

            try {
                // convert inputstream to string
                if(is != null) {
                    Log.d("get_time", "read start");
                    recv_data = readJson.readJsonTime(is);
                    Log.d("get_time", "read end");
                    Log.d("get_time", Integer.toString(recv_data.size()));
                } else
                    recv_data = null;
            }
            catch (Exception e) {
                throw e;
            }
            finally {
                is.close();
                httpCon.disconnect();
            }
        }
        catch (IOException e) {
            throw e;
        }
        catch (Exception e) {
            throw e;
        }
        return (recv_data.size() > 0) ? recv_data : null;
    }

    /**
     * @brief method to register time data to server
     * @param url
     * @param id
     * @param time
     * @return 'complete' if successful, 'fail' if not
     */
    public String registerTime(String url, String id, Data time) throws Exception {
        String result = "";
        try {
            HttpURLConnection httpCon = connectHTTP(url);
            String json = "";

            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("id", id);
            jsonObject.accumulate("category", time.getCategory());
            jsonObject.accumulate("amount", time.getAmount());

            // convert JSONObject to JSON to String
            json = jsonObject.toString();
            Log.e("post_time", json);

            OutputStream os = httpCon.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.flush();
            os.close();

            is = httpCon.getInputStream();
            try {
                if(is != null)
                    result = convertInputStreamToString(is);
                else
                    result = "fail";
            }
            catch (Exception e) {
                throw e;
            }
            finally {
                is.close();
                httpCon.disconnect();
            }
        }
        catch (IOException e) {
            throw e;
        }
        catch (Exception e) {
            throw e;
        }
        return result;
    }

    /**
     * @brief method to convert data of Inputstream type to data of String type
     * @param inputStream
     * @return converted String data
     * @throws IOException
     */
    private String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
}
