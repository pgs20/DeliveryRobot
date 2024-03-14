import java.util.*;

public class Main {
    public static final int COUNT = 50;
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < COUNT; ++i) {
            Thread thread = new Thread(() -> {
                int freq = countR(generateRoute("RLRFR", 100));
                synchronized (sizeToFreq) {
                    sizeToFreq.putIfAbsent(freq, 0);
                    sizeToFreq.put(freq, sizeToFreq.get(freq) + 1);
                }
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        int maxFreq = Collections.max(sizeToFreq.keySet());
        System.out.println(sizeToFreq);
        System.out.println("Самое частое количество повторений " + maxFreq + " (встретилось " + sizeToFreq.get(maxFreq) + " раз)");
        System.out.println("Другие размеры: ");
        sizeToFreq.forEach((key, value) -> {
            if (key != maxFreq) System.out.println("- " + key + "(" + value + ") раз");
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
    public static int countR(String commands) {
        int count = 0;
        for (char command : commands.toCharArray()) {
            if (command == 'R') {
                ++count;
            }
        }
        return count;
    }
}
