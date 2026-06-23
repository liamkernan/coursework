package datapractice1;

public class practice3 {

    public static void main(String[] args){

        int arr[] = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        int total = 1;

        for (int i = 1; i < arr.length; i++){
            int prev = arr[i - 1];
            if (prev != arr[i]){
                total++;
            }
        }

        int y = 1;
        int[] blah = new int[total];

        for (int z = 1; z < blah.length; z++) {
            for (int t = y; t < arr.length; t++) {
                int prev = arr[t - 1];
                if (prev != arr[t]) {
                    blah[z] = arr[t];
                    y = t + 1;
                    break;
                }
            }
        }

        for (int m = 0; m < blah.length; m++){
            System.out.println(blah[m]);
        }

    }

}
