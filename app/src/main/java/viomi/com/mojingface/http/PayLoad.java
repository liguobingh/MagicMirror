package viomi.com.mojingface.http;

import rx.functions.Func1;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiFaceInWall
 * @Package: com.viomi.viomifaceinwall.http
 * @ClassName: PayLoad
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019/3/2 5:27 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/2 5:27 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class PayLoad<T> implements Func1<BaseResponse<T>,T> {
    @Override
    public T call(BaseResponse<T> tBaseResponse) {
        return null;
    }

//    @Override
//    public T call(BaseResponse<T> tResponseResult) {
//        if(!tResponseResult.isSuccess()){
//            throw new Fault(tResponseResult.getCode(), tResponseResult.getDesc());
//        }
//        return tResponseResult.getResult();
//    }
}
