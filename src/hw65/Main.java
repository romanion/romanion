package hw65;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Arrays;

public class Main {

    public static int[] getAfterLast4Seq(int[] array){
        if(array != null && array.length > 0){
            int last4Index = -1;
            for (int i = 0; i < array.length; i++){
                if(array[i] == 4){
                    last4Index = i + 1;
                }

            }
            if(last4Index != -1){
                int[] newArray = new int[array.length - last4Index];
                for (int i = 0; i < newArray.length; i++){
                    newArray[i] = array[last4Index];
                    last4Index++;
                }

                return newArray;
            }
            else {
                throw new RuntimeException("Input array should contain 4");
            }

        }

        return null;
    }

    public static boolean checkOneAndFourExistence(int[] array){
        for (int number: array){
            if(number == 1 || number == 4){
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) throws InterruptedException {
        getAfterLast4Seq(new int[]{1, 2, 3, 4, 5, 3, 2, 2});
        System.out.println(checkOneAndFourExistence(new int[]{6, 2, 3}));
    }
}
