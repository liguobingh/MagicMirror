package viomi.com.mojingface.base;

import java.util.Stack;

/**
 * <p>descript：管理全局acivity的管理类<p>
 * <p>author：randysu<p>
 * <p>create time：2018/10/4<p>
 * <p>update time：2018/10/4<p>
 * <p>version：1<p>
 */
public class AppManager {

    private static Stack<BaseActivity> activityStack;

    private static AppManager instance;

    public static AppManager getInstance() {
        if (instance == null) {
            synchronized (AppManager.class) {
                if (instance == null) {
                    instance = new AppManager();
                }
            }
        }

        return instance;
    }

    private AppManager() {

    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(BaseActivity activity){
        if(activityStack==null){
            activityStack=new Stack<BaseActivity>();
        }
        activityStack.add(activity);
    }
    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public BaseActivity currentActivity(){
        BaseActivity activity = activityStack.lastElement();
        return activity;
    }
    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity(){
        BaseActivity activity = activityStack.lastElement();
        if(activity!=null){
            activity.finish();
            activity=null;
        }
    }
    /**
     * 结束指定的Activity
     */
    public void finishActivity(BaseActivity activity){
        if(activity!=null){
            activityStack.remove(activity);
            activity.finish();
            activity=null;
        }
    }
    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls){
        for (BaseActivity activity : activityStack) {
            if(activity.getClass().equals(cls) ){
                finishActivity(activity);
            }
        }
    }
    /**
     * 结束所有Activity
     */
    public void finishAllActivity(){
        for (int i = 0, size = activityStack.size(); i < size; i++){
            if (null != activityStack.get(i)){
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 获得所有的Activity
     * @return
     */
    public Stack<BaseActivity> getAllActivities() {
        return activityStack;
    }

    /**
     * 根据名称获取Activity
     * @param cls
     * @return
     */
    public BaseActivity getActivityByName(Class<?> cls) {
        for (BaseActivity activity : activityStack) {
            if(activity.getClass().equals(cls) ){
                return activity;
            }
        }
        return null;
    }
}
