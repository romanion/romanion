package hw65;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static hw65.Main.checkOneAndFourExistence;
import static hw65.Main.getAfterLast4Seq;

@RunWith(Parameterized.class)
public class MainTest {

    private int[] sourceArray;
    private int[] resultArray;
    private boolean result;


    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new int[]{1, 2, 3, 4, 5, 3, 2, 2}, new int[]{5, 3, 2, 2}, true},
                {new int[]{2, 3, 5, 3, 2, 2}, new int[]{}, false},
                {new int[]{2, 3, 5, 3, 2, 2, 4}, new int[]{}, true}
        });

    }

    public MainTest(int[] sourceArray, int[] resultArray, boolean result){
        this.sourceArray = sourceArray;
        this.result = result;
        this.resultArray = resultArray;
    }

    @Test
    public void getAfterLast4SeqTest() {
        Assert.assertArrayEquals(resultArray, getAfterLast4Seq(sourceArray));
    }

    @Test
    public void checkOneAndFourExistenceTest() {
        Assert.assertEquals(result, checkOneAndFourExistence(sourceArray));
    }
}
