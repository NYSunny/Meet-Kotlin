package com.library.networklib.interceptors;

import com.library.networklib.sp.NetSP;

import java.util.Map;

/**
 * @author Johnny
 */
public class DefaultHeadersInterceptor extends BaseHeadersInterceptor {

    @Override
    public Map<String, String> buildHeaders() {
        return NetSP.getInstance().getNetHeaderParams();
    }
}
