package com.ape.apecommon.utils;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: TODO
 * @date 2023/9/22 17:18
 */
public class UserAgentUtil {

    /**
     * 根据请求头来判断浏览器类型及操作系统类型
     * @param userAgent 请求头
     * @return
     */
    public static String getUserAgent(String userAgent) {
        if(userAgent==""||userAgent==null){
            userAgent="";
        }
        //主流应用靠前
        if (userAgent.contains("Windows")) {
            if (userAgent.contains("Windows NT 10.0")) {
                return "Windows 10";
            } else if (userAgent.contains("Windows NT 6.2")) {
                return "Windows 8";
            } else if (userAgent.contains("Windows NT 6.1")) {
                return"Windows 7";
            } else if (userAgent.contains("Windows NT 6.0")) {
                return "Windows Vista";
            } else if (userAgent.contains("Windows NT 5.2")) {
                return "Windows XP";
            } else if (userAgent.contains("Windows NT 5.1")) {
                return "Windows XP";
            } else if (userAgent.contains("Windows NT 5.01")) {
                return "Windows 2000";
            } else if (userAgent.contains("Windows NT 5.0")) {
                return "Windows 2000";
            } else if (userAgent.contains("Windows NT 4.0")) {
                return "Windows NT 4.0";
            } else if (userAgent.contains("Windows 98; Win 9x 4.90")) {
                return "Windows ME";
            } else if (userAgent.contains("Windows 98")) {
                return "Windows 98";
            } else if (userAgent.contains("Windows 95")) {
                return "Windows 95";
            } else if (userAgent.contains("Windows CE")) {
                return "Windows CE";
            }
        } else if (userAgent.contains("Mac OS X")) {
            if(userAgent.contains("iPhone")){
                return "iPhone";
            }
            else if (userAgent.contains("iPad")) {
                return "iPad";
            }else{
                return "Mac";
            }
        }else if(userAgent.contains("Android")){
            return "Android";
        }else if(userAgent.contains("Linux")){
            return "Linux";
        }else if(userAgent.contains("FreeBSD")){
            return "FreeBSD";
        }else if(userAgent.contains("Solaris")){
            return "Solaris";
        }
        return "其他";
    }

    public static String judgeBrowser(String userAgent) {
        if (userAgent.contains("Edge")) {
            return "Microsoft Edge";
        }else if(userAgent.contains("QQBrowser")){
            return "腾讯浏览器";
        }else if (userAgent.contains("Chrome")&&userAgent.contains("Safari")) {
            return "Chrome";
        } else if (userAgent.contains("Firefox")) {
            return "Firefox";
        }else if (userAgent.contains("360")) {
            return "360浏览器";
        }else if (userAgent.contains("Opera")) {
            return "Opera";
        }else if (userAgent.contains("Safari")&&!userAgent.contains("Chrome")) {
            return "Safari";
        }else if (userAgent.contains("MetaSr1.0")) {
            return "搜狗浏览器";
        }else if (userAgent.contains("TencentTraveler")) {
            return "腾讯浏览器";
        }else if (userAgent.contains("UC")) {
            return "UC浏览器";
        }else if (userAgent.contains("MSIE")) {
            if (userAgent.contains("MSIE 10.0")) {
                return "IE 10";
            } else if (userAgent.contains("MSIE 9.0")) {
                return "IE 9";
            } else if (userAgent.contains("MSIE 8.0")) {
                return "IE 8";
            } else if (userAgent.contains("MSIE 7.0")) {
                return "IE 7";
            } else if (userAgent.contains("MSIE 6.0")) {
                return "IE 6";
            }
        } else {
            return "其他";
        }
        return "其他";
    }
}
