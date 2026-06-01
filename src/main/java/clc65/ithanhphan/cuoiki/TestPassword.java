package clc65.ithanhphan.cuoiki;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TestPassword implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;

    public TestPassword(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        System.out.println(
                passwordEncoder.encode("password")
        );
    }
}