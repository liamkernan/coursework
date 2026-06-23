import java.util.ArrayList;
import java.util.Collections;

public class ArrayListExamples {

    public static void main(String[] args) {

        ArrayList<String> names = new ArrayList<>();

        names.add("Ana");
        names.add("Ben");
        names.add("Carlos");

        System.out.println("Names list: " + names);

        String firstName = names.get(0);
        System.out.println("First name: " + firstName);

        System.out.println("Number of names: " + names.size());

        names.set(1, "Brenda");
        System.out.println("After update: " + names);

        if (names.contains("Carlos")) {
            System.out.println("Carlos is in the list.");
        }

        names.remove("Ana");
        names.remove(0);
        System.out.println("After removals: " + names);

        ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(10);
        numbers.add(25);
        numbers.add(5);
        numbers.add(40);

        System.out.println(" Numbers (for loop):");
        for (int i = 0; i < numbers.size(); i++) {
            System.out.println(numbers.get(i));
        }

        System.out.println(" Numbers (for-each loop):");
        for (int num : numbers) {
            System.out.println(num);
        }

        int sum = 0;
        for (int num : numbers) {
            sum += num;
        }
        System.out.println(" Sum of numbers: " + sum);

        int max = numbers.get(0);
        for (int num : numbers) {
            if (num > max) {
                max = num;
            }
        }
        System.out.println("Maximum value: " + max);

        Collections.sort(numbers);
        System.out.println("Sorted numbers: " + numbers);

        numbers.clear();
        System.out.println("Numbers after clear(): " + numbers);

        if (numbers.isEmpty()) {
            System.out.println("The numbers list is empty.");
        }
    }
}
