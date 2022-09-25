import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;


public class HeapTest {
    int[] getRandomArr(int length) {
        Random random = new Random();
        int[] arr = new int[length];
        for (int i = 0; i < length; i++) {
            arr[i] = random.nextInt();
        }
        return arr;
    }

    @Test
    void testSorting() {
        for (int i = 0; i < 1000; i++) {
            int[] arr = getRandomArr(1000);
            int[] arrForCheck = arr.clone();
            Arrays.sort(arrForCheck);
            Assertions.assertArrayEquals(arrForCheck, Heap.heapSort(arr));
        }

    }

    @Test
    void testCorrectOrderInHeap() {
        int size = 1000;
        for (int i = 0; i < 1000; i++) {
            Heap heap = new Heap(getRandomArr(size));
            for (int j = 1; j < heap.size(); j++) {
                Assertions.assertTrue(heap.get(j) > heap.get((j - 1) / 2));
            }
        }
    }

    @Test
    void testThrows() {
        Heap heap = new Heap();
        boolean flag = false;
        try {
            heap.get(10);
        } catch (ArrayIndexOutOfBoundsException e) {
            flag = true;
        }
        Assertions.assertTrue(flag);

        flag = false;
        try {
            heap.get(-10);
        } catch (ArrayIndexOutOfBoundsException e) {
            flag = true;
        }
        Assertions.assertTrue(flag);

        flag = false;
        try {
            heap.extractMin();
        } catch (IndexOutOfBoundsException e) {
            flag = true;
        }
        Assertions.assertTrue(flag);
    }

    @Test
    void testForMin() {
        for (int i = 1; i < 100; i++) {
            int[] arr = getRandomArr(i);
            int x = Arrays.stream(arr).min().getAsInt();
            Heap heap = new Heap(arr);
            Assertions.assertEquals(heap.getMin(), x);
            Assertions.assertEquals(heap.size(), arr.length);
            x--;
            heap.add(x);
            Assertions.assertEquals(heap.getMin(), x);
            x = Arrays.stream(arr).max().getAsInt() + 5;
            heap.add(x);
            Assertions.assertEquals(heap.get(heap.size() - 1), x);
        }


    }
}
