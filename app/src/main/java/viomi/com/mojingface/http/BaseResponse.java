package viomi.com.mojingface.http;

import androidx.annotation.NonNull;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiFaceInWall
 * @Package: com.viomi.viomifaceinwall.http
 * @ClassName: BaseResponse
 * @Description: 网络请求基类
 * @Author: randysu
 * @CreateDate: 2019/3/2 5:24 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/2 5:24 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class BaseResponse<T> {

    private ResponseResult<T> mobBaseRes;

    public ResponseResult<T> getMobBaseRes() {
        return mobBaseRes;
    }

    public void setMobBaseRes(ResponseResult<T> mobBaseRes) {
        this.mobBaseRes = mobBaseRes;
    }

    @NonNull
    @Override
    public String toString() {
        return mobBaseRes.toString();
    }

    public class ResponseResult<T> {
        private int code;
        private String desc;

        private T result;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public T getResult() {
            return result;
        }

        public void setResult(T result) {
            this.result = result;
        }

        public boolean isSuccess(){
            return code == 100;
        }

        @NonNull
        @Override
        public String toString() {
            return "code:" + code + " desc:" + desc + " result:" + result.toString();
        }
    }

}
