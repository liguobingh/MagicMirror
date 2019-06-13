package viomi.com.mojingface.http.service.migu;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiFaceInWall
 * @Package: com.viomi.viomifaceinwall.http.service.migu
 * @ClassName: MiguReportRequest
 * @Description: migu设备上报请求参数
 * @Author: randysu
 * @CreateDate: 2019/3/2 5:41 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/2 5:41 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class MiguReportRequest {

    private String chCode;
    private String smartDeviceId;
    private String toneQuality;
    private String openTime;

    public MiguReportRequest() {

    }

    public MiguReportRequest(String chCode, String smartDeviceId, String toneQuality, String openTime) {
        this.chCode = chCode;
        this.smartDeviceId = smartDeviceId;
        this.toneQuality = toneQuality;
        this.openTime = openTime;
    }

    public String getChCode() {
        return chCode;
    }

    public void setChCode(String chCode) {
        this.chCode = chCode;
    }

    public String getSmartDeviceId() {
        return smartDeviceId;
    }

    public void setSmartDeviceId(String smartDeviceId) {
        this.smartDeviceId = smartDeviceId;
    }

    public String getToneQuality() {
        return toneQuality;
    }

    public void setToneQuality(String toneQuality) {
        this.toneQuality = toneQuality;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }
}
