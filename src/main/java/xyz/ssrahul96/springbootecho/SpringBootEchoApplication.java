package xyz.ssrahul96.springbootecho;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.SystemProperties;

@SpringBootApplication
@Log4j2
public class SpringBootEchoApplication {

    public static void main(String[] args) {
        simulateDelay();
        SpringApplication.run(SpringBootEchoApplication.class, args);
    }

    private static void simulateDelay() {
        int delayInSec = NumberUtils.toInt(SystemProperties.get("STARTUP_DELAY_SEC"));

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
