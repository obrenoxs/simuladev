package io.github.obrenoxs.simuladev.engine.util;

import org.springframework.stereotype.Component;

@Component
public class RandomGenerator {
    public double nextDouble() {
        return Math.random();
    }
}
