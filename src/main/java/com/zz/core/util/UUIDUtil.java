
package com.zz.core.util;

import java.util.UUID;

/**
 * UUID 工具
 */
public class UUIDUtil {

    public static String getUUID(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }
    
}
