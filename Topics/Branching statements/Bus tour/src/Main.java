import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // start coding here
        int busHeight = scanner.nextInt();
        int numberOfBridges = scanner.nextInt();
        int bridgeNumber = 0;
        boolean crash = false;

        for (int i = 0; i < numberOfBridges; i++) {
            if (busHeight >= scanner.nextInt()) {
                bridgeNumber = i + 1;
                crash = true;
                break;
            }
        }

        System.out.println((!crash) ? "Will not crash" : String.format("Will crash on bridge %d", bridgeNumber));
    }
}