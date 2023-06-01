package com.team127.atom;

import com.team127.atom.util.MyWebSocket;
import io.github.asleepyfish.annotation.EnableChatGPT;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableCaching
@EnableChatGPT
public class AtomApplication {

    public static void main(String[] args) {
//        SpringApplication.run(AtomApplication.class, args);
        ConfigurableApplicationContext applicationContext =   SpringApplication.run(AtomApplication.class, args);
        MyWebSocket.setApplicationContext(applicationContext);
    }

}
