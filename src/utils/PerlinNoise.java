package utils;

import java.util.Random;

public class PerlinNoise {

    private int[] p;

    public PerlinNoise(long seed) {
        p = new int[512];
        Random rand = new Random(seed);
        int[] p256 = new int[256];
        for (int i = 0; i < 256; i++) {
            p256[i] = i;
        }
        // Shuffle
        for (int i = 0; i < 256; i++) {
            int j = rand.nextInt(256);
            int temp = p256[i];
            p256[i] = p256[j];
            p256[j] = temp;
        }
        // Duplicate for 512
        for (int i = 0; i < 512; i++) {
            p[i] = p256[i % 256];
        }
    }

    private double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    private double grad(int hash, double x, double y) {
        int h = hash & 15;
        double u = h < 8 ? x : y;
        double v = h < 4 ? y : h == 12 || h == 14 ? x : 0;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    public double eval(double x, double y) {
        int X = (int) Math.floor(x) & 255;
        int Y = (int) Math.floor(y) & 255;
        
        x -= Math.floor(x);
        y -= Math.floor(y);
        
        double u = fade(x);
        double v = fade(y);
        
        int A = p[X] + Y, AA = p[A], AB = p[A + 1];
        int B = p[X + 1] + Y, BA = p[B], BB = p[B + 1];
        
        return lerp(v, lerp(u, grad(p[AA], x, y),
                                grad(p[BA], x - 1, y)),
                       lerp(u, grad(p[AB], x, y - 1),
                                grad(p[BB], x - 1, y - 1)));
    }
}
