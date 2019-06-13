package viomi.com.mojingface.http.service.migu;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiFaceInWall
 * @Package: com.viomi.viomifaceinwall.model
 * @ClassName: MiguReportResponse
 * @Description: migu接口校验结果实体
 * @Author: randysu
 * @CreateDate: 2019/3/2 5:38 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/2 5:38 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class MiguReportResponse {

    private String resCode;
    private String resMsg;

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

}
