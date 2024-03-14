import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static final int COUNT = 100;
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

        Thread threadMax = new Thread(() -> {
            while (!Thread.interrupted()) {
                synchronized (sizeToFreq) {
                    try {
                        sizeToFreq.wait();
                        System.out.println("Лидер среди частот: " + Collections.max(sizeToFreq.keySet()));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        threadMax.start();

        for (int i = 0; i < COUNT; ++i) {
            Thread thread = new Thread(() -> {
                countR(generateRoute("RLRFR", COUNT));
                synchronized (sizeToFreq) {
                    sizeToFreq.notify();
                }
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        threadMax.interrupt();

        int maxFreq = Collections.max(sizeToFreq.keySet());
        System.out.println(sizeToFreq);
        System.out.println("Самое частое количество повторений " + maxFreq + " (встретилось " + sizeToFreq.get(maxFreq) + " раз)");
        System.out.println("Другие размеры: ");
        sizeToFreq.forEach((key, value) -> {
            if (key != maxFreq) System.out.println("- " + key + " (" + value + ") раз");
        });

    }
    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
    public static void countR(String commands) {
        int count = 0;
        for (char command : commands.toCharArray()) {
            if (command == 'R') {
                ++count;
            }
        }
        synchronized (sizeToFreq) {
            sizeToFreq.putIfAbsent(count, 0);
            sizeToFreq.put(count, sizeToFreq.get(count) + 1);
        }
    }
}
