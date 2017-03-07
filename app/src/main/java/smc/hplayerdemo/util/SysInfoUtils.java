package smc.hplayerdemo.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Administrator on 2017/3/1.
 *
 * 获取手机系统和sim卡相关信息
 *
 */

public class SysInfoUtils {
    private static TelephonyManager telephonyManager = null;// 电话服务

    /**
     * 获取当前操作系统的语言
     *
     * @return String 系统语言
     */
    public static String getSysLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取手机型号
     *
     * @return String 手机型号
     */
    public static String getModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取操作系统的版本号
     *
     * @return String 系统版本号
     */
    public static String getSysRelease() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 读取sim卡序列号
     */
    public static String readSimSerialNum(Context con) {
        String number = getTelephonyManager(con).getSimSerialNumber();
        return number != null ? number : "";
    }

    /**
     * 获得电话管理实例对象
     *
     * @param con 上下文
     * @return 实例对象
     */
    private static TelephonyManager getTelephonyManager(Context con) {
        if (telephonyManager == null) {
            telephonyManager = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager;
        } else {
            return telephonyManager;
        }
    }

    /**
     * 读唯一的设备ID
     *
     * @param con 上下文
     * @return 唯一的设备ID IMEI GSM手机的 IMEI 和 CDMA手机的 MEID. 如果获取不到返回一个Mac地址
     */
    public static String readTelephoneSerialNum(Context con) {
        String telephoneSerialNumber = getTelephonyManager(con).getDeviceId();

        // 当IMEI号获取不到的时候，获取Mac地址
        WifiManager wifi = (WifiManager) con.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String telephoneMac = info.getMacAddress();

        return !TextUtils.isEmpty(telephoneSerialNumber) ? telephoneSerialNumber : telephoneMac;
    }

    /**
     * 读取设置的应用名称
     *
     * @return 设配名称
     */
    public static String getDeviceName() {
        return Build.MODEL;
    }

    /**
     * 获取系统当前时间，精确到秒
     *
     * @return 返回当前时间字符串
     */
    public static String getNowTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(Calendar.getInstance().getTime());
    }

    /**
     * 获取运营商信息
     *
     * @param con 上下文
     * @return String 运营商信息
     */
    public static String getCarrier(Context con) {
        TelephonyManager telManager = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telManager.getSubscriberId();
        if (imsi != null && imsi.length() > 0) {
            // 因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号
            if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
                return "China Mobile";
            } else if (imsi.startsWith("46001")) {
                return "China Unicom";
            } else if (imsi.startsWith("46003")) {
                return "China Telecom";
            }
        }
        return "未能识别";
    }

    /**
     * 获取SD卡剩余空间的大小
     *
     * @return long SD卡剩余空间的大小（单位：byte）
     */
    public static long getSDSize() {
        String str = Environment.getExternalStorageDirectory().getPath();
        StatFs localStatFs = new StatFs(str);
        long blockSize = localStatFs.getBlockSize();
        return localStatFs.getAvailableBlocks() * blockSize;
    }

    /**
     * 获取SD的路径
     *
     * @return
     */
    public static String getSDPath() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getPath();
        }
        return "";
    }

    /**
     * 电话状态
     *
     * @param con 上下文
     * @return 0：无活动 1：响铃 2：待机
     */
    public static int getPhoneState(Context con) {
        return getTelephonyManager(con).getCallState();
    }

    /**
     * 获得电话方位
     *
     * @param con 上下文
     * @return 方位对象
     */
    public static CellLocation getPhoneLoaction(Context con) {
        CellLocation cellLocation = getTelephonyManager(con).getCellLocation();
        return (CellLocation) (cellLocation != null ? cellLocation : "");
    }

    /**
     * 设备的软件版本号： the IMEI/SV(software version) for GSM phones.
     *
     * @param con 上下文
     * @return 不支持返回“not available”
     */
    public static String getDeviceSoftVersion(Context con) {
        String version = getTelephonyManager(con).getDeviceSoftwareVersion();
        return version != null ? version : "not available";
    }

    /**
     * 获得手机号
     *
     * @param con 上下文
     * @return 手机号 不支持就返回“12322344345”
     */
    public static String getPhoneNumber(Context con) {
        String number = getTelephonyManager(con).getLine1Number();
        return number != null ? number : "12322344345";
    }

    /**
     * 获得SIM卡提供的移动国家码和移动网络码.5或6位的十进制数字. SIM卡的状态必须是
     * SIM_STATE_READY(使用getSimState()判断).
     *
     * @param con 上下文
     * @return 例：46002
     */
    public static String getSimCode(Context con) {
        if (getTelephonyManager(con).getSimState() == 5) {
            String code = getTelephonyManager(con).getSimOperator();
            return code != null ? code : "";
        } else {
            return "";
        }
    }

    /**
     * 服务商名称：例如：中国移动、联通 SIM卡的状态必须是 SIM_STATE_READY(使用getSimState()判断).
     *
     * @param con 上下文
     * @return 服务商名称
     */
    public static String getSimPrivatorName(Context con) {
        if (getTelephonyManager(con).getSimState() == 5) {
            String name = getTelephonyManager(con).getSimOperatorName();
            return name != null ? name : "";
        } else {
            return "";
        }
    }

    /**
     * 唯一的用户ID 例如：IMSI(国际移动用户识别码) for a GSM phone. 需要权限：READ_PHONE_STATE
     *
     * @param con 上下文
     * @return
     */
    public static String getUserPhoneId(Context con) {
        return getTelephonyManager(con).getSubscriberId();
    }

    /**
     * 获取屏幕管理类
     *
     * @return DisplayMetrics 屏幕管理对象
     */
    public static DisplayMetrics getDisplayMetrics(Activity context) {
        DisplayMetrics displayMetrics = null;
        if (displayMetrics == null) {
            displayMetrics = new DisplayMetrics();
        }
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    /**
     * 获得当前app版本号
     */
    public static String getVersionName(Context con) {
        PackageInfo pinfo;
        try {
            pinfo = con.getPackageManager().getPackageInfo(con.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pinfo.versionName;
            // mVersionName = pinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
