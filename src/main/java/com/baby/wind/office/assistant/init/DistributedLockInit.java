package com.baby.wind.office.assistant.init;

import com.baby.wind.office.assistant.component.lock.DistributedLock;
import com.baby.wind.office.assistant.component.lock.ZkDistributedLock;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DistributedLockInit {

    @Bean
    public DistributedLock getDistributedLock(){
        return new ZkDistributedLock();
    }
}
