package datapractice1;

public class practice2 {

    public static void main(String[] args){

        int arr[] = {1, 2, 3, 4, 5, 6, 7};
        int updated[] = new int[arr.length];
        int k = 3;

        for (int i = 0; i < arr.length; i++){
            if (i + k > arr.length - 1){
                int u = k + i - (arr.length);
                updated[i] = arr[u];
            } else {
                updated[i] = arr[i + k];
            }
        }

        for (int i = 0; i < arr.length; i++){
            System.out.println(updated[i]);
        }

    }

}
