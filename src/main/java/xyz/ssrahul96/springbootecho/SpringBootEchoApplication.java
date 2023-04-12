package xyz.ssrahul96.springbootecho;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.SystemProperties;

import static xyz.ssrahul96.springbootecho.utils.DelayUtil.simulateDelay;

@SpringBootApplication
@Log4j2
public class SpringBootEchoApplication {

    public static void main(String[] args) {
        simulateDelay(SystemProperties.get("STARTUP_DELAY_SEC"));
        SpringApplication.run(SpringBootEchoApplication.class, args);
    }

}
