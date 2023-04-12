package xyz.ssrahul96.springbootecho.utils;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.math.NumberUtils;

@Log4j2
public class DelayUtil {

    private DelayUtil() {
    }

    public static void simulateDelay(String delayInSec) {
        simulateDelay(NumberUtils.toInt(delayInSec));
    }

    public static void simulateDelay(int delayInSec) {
        if (delayInSec == 0) {
            return;
        }

        try {
            log.info("simulating delay for {} seconds", delayInSec);
            Thread.sleep(delayInSec * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
