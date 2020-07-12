//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.statistics;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UserData {
    public static final String NAME_KEY = "name";
    public static final String USERNAME_KEY = "username";
    public static final String EMAIL_KEY = "email";
    public static final String ORG_KEY = "organization";
    public static final String PHONE_KEY = "phone";
    public static final String PICTURE_KEY = "picture";
    public static final String PICTURE_PATH_KEY = "picturePath";
    public static final String GENDER_KEY = "gender";
    public static final String BYEAR_KEY = "byear";
    public static final String CUSTOM_KEY = "custom";
    public static String name;
    public static String username;
    public static String email;
    public static String org;
    public static String phone;
    public static String picture;
    public static String picturePath;
    public static String gender;
    public static Map<String, String> custom;
    public static int byear = 0;
    public static boolean isSynced = true;

    public UserData() {
    }

    static void setData(Map<String, String> data) {
        if (data.containsKey("name")) {
            name = (String)data.get("name");
        }

        if (data.containsKey("username")) {
            username = (String)data.get("username");
        }

        if (data.containsKey("email")) {
            email = (String)data.get("email");
        }

        if (data.containsKey("organization")) {
            org = (String)data.get("organization");
        }

        if (data.containsKey("phone")) {
            phone = (String)data.get("phone");
        }

        if (data.containsKey("picturePath")) {
            picturePath = (String)data.get("picturePath");
        }

        if (picturePath != null) {
            File sourceFile = new File(picturePath);
            if (!sourceFile.isFile()) {
                if (Statistics.sharedInstance().isLoggingEnabled()) {
                    Log.w("Statistics", "Provided file " + picturePath + " can not be opened");
                }

                picturePath = null;
            }
        }

        if (data.containsKey("picture")) {
            picture = (String)data.get("picture");
        }

        if (data.containsKey("gender")) {
            gender = (String)data.get("gender");
        }

        if (data.containsKey("byear")) {
            try {
                byear = Integer.parseInt((String)data.get("byear"));
            } catch (NumberFormatException var2) {
                if (Statistics.sharedInstance().isLoggingEnabled()) {
                    Log.w("Statistics", "Incorrect byear number format");
                }

                byear = 0;
            }
        }

        isSynced = false;
    }

    static void setCustomData(Map<String, String> data) {
        custom = new HashMap();
        custom.putAll(data);
        isSynced = false;
    }

    static String getDataForRequest() {
        if (!isSynced) {
            isSynced = true;
            JSONObject json = toJSON();
            if (json != null) {
                String result = json.toString();

                try {
                    result = URLEncoder.encode(result, "UTF-8");
                    if (result != null && !result.equals("")) {
                        result = "&user_details=" + result;
                        if (picturePath != null) {
                            result = result + "&picturePath=" + URLEncoder.encode(picturePath, "UTF-8");
                        }
                    } else {
                        result = "";
                        if (picturePath != null) {
                            result = result + "&user_details&picturePath=" + URLEncoder.encode(picturePath, "UTF-8");
                        }
                    }
                } catch (UnsupportedEncodingException var3) {
                }

                if (result != null) {
                    return result;
                }
            }
        }

        return "";
    }

    static JSONObject toJSON() {
        JSONObject json = new JSONObject();

        try {
            if (name != null) {
                if (name == "") {
                    json.put("name", JSONObject.NULL);
                } else {
                    json.put("name", name);
                }
            }

            if (username != null) {
                if (username == "") {
                    json.put("username", JSONObject.NULL);
                } else {
                    json.put("username", username);
                }
            }

            if (email != null) {
                if (email == "") {
                    json.put("email", JSONObject.NULL);
                } else {
                    json.put("email", email);
                }
            }

            if (org != null) {
                if (org == "") {
                    json.put("organization", JSONObject.NULL);
                } else {
                    json.put("organization", org);
                }
            }

            if (phone != null) {
                if (phone == "") {
                    json.put("phone", JSONObject.NULL);
                } else {
                    json.put("phone", phone);
                }
            }

            if (picture != null) {
                if (picture == "") {
                    json.put("picture", JSONObject.NULL);
                } else {
                    json.put("picture", picture);
                }
            }

            if (gender != null) {
                if (gender == "") {
                    json.put("gender", JSONObject.NULL);
                } else {
                    json.put("gender", gender);
                }
            }

            if (byear != 0) {
                if (byear > 0) {
                    json.put("byear", byear);
                } else {
                    json.put("byear", JSONObject.NULL);
                }
            }

            if (custom != null) {
                if (custom.isEmpty()) {
                    json.put("custom", JSONObject.NULL);
                } else {
                    json.put("custom", new JSONObject(custom));
                }
            }
        } catch (JSONException var2) {
            if (Statistics.sharedInstance().isLoggingEnabled()) {
                Log.w("Statistics", "Got exception converting an UserData to JSON", var2);
            }
        }

        return json;
    }

    static void fromJSON(JSONObject json) {
        if (json != null) {
            name = json.optString("name", (String)null);
            username = json.optString("username", (String)null);
            email = json.optString("email", (String)null);
            org = json.optString("organization", (String)null);
            phone = json.optString("phone", (String)null);
            picture = json.optString("picture", (String)null);
            gender = json.optString("gender", (String)null);
            byear = json.optInt("byear", 0);
            if (!json.isNull("custom")) {
                try {
                    JSONObject customJson = json.getJSONObject("custom");
                    HashMap<String, String> custom = new HashMap(customJson.length());
                    Iterator nameItr = customJson.keys();

                    while(nameItr.hasNext()) {
                        String key = (String)nameItr.next();
                        if (!customJson.isNull(key)) {
                            custom.put(key, customJson.getString(key));
                        }
                    }
                } catch (JSONException var5) {
                    if (Statistics.sharedInstance().isLoggingEnabled()) {
                        Log.w("Statistics", "Got exception converting an Custom Json to Custom User data", var5);
                    }
                }
            }
        }

    }

    public static String getPicturePathFromQuery(URL url) {
        String query = url.getQuery();
        if (query == null) {
            return "";
        } else {
            String[] pairs = query.split("&");
            String ret = "";
            if (url.getQuery().contains("picturePath")) {
                String[] var4 = pairs;
                int var5 = pairs.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    String pair = var4[var6];
                    int idx = pair.indexOf("=");
                    if (pair.substring(0, idx).equals("picturePath")) {
                        try {
                            ret = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
                        } catch (UnsupportedEncodingException var10) {
                            ret = "";
                        }
                        break;
                    }
                }
            }

            return ret;
        }
    }
}
