//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

import io.rong.common.RLog;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserOnlineStatusInfoList {
    private static final String TAG = "UserOnlineStatusInfoList";
    private ArrayList<UserOnlineStatusInfo> userStatusInfoList = new ArrayList();

    public UserOnlineStatusInfoList(String platformInfo) {
        if (this.userStatusInfoList != null) {
            this.userStatusInfoList.clear();

            try {
                JSONObject jsonObject = new JSONObject(platformInfo);
                JSONArray jsonArray = jsonObject.optJSONArray("us");
                if (jsonArray != null && jsonArray.length() > 0) {
                    for(int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObjectUS = jsonArray.optJSONObject(i);
                        JSONArray jsonArrayPlatform = jsonObjectUS.optJSONArray("p");
                        if (jsonArrayPlatform != null && jsonArrayPlatform.length() > 0) {
                            for(int j = 0; j < jsonArrayPlatform.length(); ++j) {
                                UserOnlineStatusInfo userStatusInfo = new UserOnlineStatusInfo(jsonObjectUS, j);
                                if (this.userStatusInfoList != null) {
                                    this.userStatusInfoList.add(userStatusInfo);
                                }
                            }
                        } else {
                            this.userStatusInfoList.add(new UserOnlineStatusInfo(jsonObjectUS, 0));
                        }
                    }
                }
            } catch (JSONException var9) {
                RLog.e("UserOnlineStatusInfoList", "UserOnlineStatusInfoList", var9);
            }

        }
    }

    public ArrayList<UserOnlineStatusInfo> getList() {
        return this.userStatusInfoList;
    }
}
