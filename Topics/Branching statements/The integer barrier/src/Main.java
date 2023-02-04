import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // start coding here

        int sum = 0;

        int newValue = scanner.nextInt();

        while (newValue !=  0) {
            sum += newValue;

            if (sum >= 1000) {
                sum -= 1000;
                break;
            }
            newValue = scanner.nextInt();
        }

        scanner.nextLine();

        System.out.println(sum);
    }
}
