package necer.expandtextview;

/**
 * Created by zhuodao on 2016/5/19.
 */
public class ButtonUtils {

    private static long lastClickTime;

    public synchronized static boolean isFastClick(long duration) {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < duration) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
