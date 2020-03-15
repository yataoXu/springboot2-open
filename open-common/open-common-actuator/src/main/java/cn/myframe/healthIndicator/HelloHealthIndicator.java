package cn.myframe.healthIndicator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * @Author: ynz
 * @Date: 2018/12/25/025 17:29
 * @Version 1.0
 */
@Component
public class HelloHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        int errorCode = check(); // perform some specific health check
        if (errorCode != 0) {
            return Health.down().withDetail("Error Code", errorCode)  .build();
        }
        return Health.up().build();
    }

    int check(){
        return 0;
    }
}
