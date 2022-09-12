//import Heap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void CheckSorting() {
        for (int i = 0; i < 1000; i++) {
            int[] arr = getRandomArr(1000);
            int[] arrForCheck = arr.clone();
            Arrays.sort(arrForCheck);
            Assertions.assertArrayEquals(arrForCheck, Heap.heapSort(arr));
        }

    }

    @Test
    void CorrectOrderInHeap() {
        int size = 1000;
        for (int i = 0; i < 1000; i++) {
            Heap heap = new Heap(getRandomArr(size));
            for (int j = 1; j < heap.size(); j++) {
                assertTrue(heap.get(j) > heap.get((j - 1) / 2));
            }
        }
    }
}
