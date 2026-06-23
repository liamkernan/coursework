package datapractice1;

public class practice1 {


    public static void main(String[] args){

        int arr[] = {1, 2, 3, 4, 5};

        for (int i = 0; i < arr.length /2; i++){
            int holder2 = arr[(arr.length - i) - 1];
            int holder = arr[i];

            arr[arr.length - i - 1] = holder;
            arr[i] = holder2;
        }

        for (int i = 0; i < arr.length; i++){
            System.out.println(arr[i]);
        }

    }

}
