import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // start coding here
        int limit = scanner.nextInt();

        for (int i = 1; i > 0; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.printf("%d ", i);

                if (--limit <= 0) {
                    break;
                }
            }
            if (limit <= 0) {
                break;
            }
        }
    }
}