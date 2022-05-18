package raf.si.bolnica.laboratory.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootstrapData implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("Laboratory service successfully running..");
    }
}
