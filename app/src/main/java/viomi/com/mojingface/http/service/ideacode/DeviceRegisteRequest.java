package viomi.com.mojingface.http.service.ideacode;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: viomifaceinwall
 * @Package: com.viomi.viomifaceinwall.http.service.ideacode
 * @ClassName: DeviceRegisteRequest
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019/3/4 2:22 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/4 2:22 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class DeviceRegisteRequest {

    private double longitude;
    private double latitude;
    private String city_name;
    private String device_no;
    private String device_did;
    private String apk_version_code;
    private String apk_version_name;
    private String rom_version_name;
    private String android_version;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getDevice_no() {
        return device_no;
    }

    public void setDevice_no(String device_no) {
        this.device_no = device_no;
    }

    public String getDevice_did() {
        return device_did;
    }

    public void setDevice_did(String device_did) {
        this.device_did = device_did;
    }

    public String getApk_version_code() {
        return apk_version_code;
    }

    public void setApk_version_code(String apk_version_code) {
        this.apk_version_code = apk_version_code;
    }

    public String getApk_version_name() {
        return apk_version_name;
    }

    public void setApk_version_name(String apk_version_name) {
        this.apk_version_name = apk_version_name;
    }

    public String getRom_version_name() {
        return rom_version_name;
    }

    public void setRom_version_name(String rom_version_name) {
        this.rom_version_name = rom_version_name;
    }

    public String getAndroid_version() {
        return android_version;
    }

    public void setAndroid_version(String android_version) {
        this.android_version = android_version;
    }
}
