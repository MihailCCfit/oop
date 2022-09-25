import java.util.ArrayList;
import java.util.List;

/**
 * Binary heap that stores integer values.
 * It uses ArrayList for storing data.
 *
 * @author Mihail Tsukanov
 * @version 0.2
 * @see Heap#add(int)
 * @see Heap#extractMin()
 * @see Heap#toArray()
 */

public class Heap {

    private List<Integer> data;
    private int sizeOfHeap;

    private void swap(int ind1, int ind2) {
        int tmp = data.get(ind1);
        data.set(ind1, data.get(ind2));
        data.set(ind2, tmp);
    }

    /**
     * Creates binary Heap
     * Started size is 0
     * ArrayList is used as data structure
     *
     * @see Heap#Heap(int[])
     */
    public Heap() {
        this.sizeOfHeap = 0;
        this.data = new ArrayList<>();
    }

    /**
     * Creates heap from arr elements.
     *
     * @param arr is the initial array, from which objects are added to heap.
     * @see Heap#siftDown(int)
     */
    public Heap(int[] arr) {
        this.sizeOfHeap = arr.length;
        this.data = new ArrayList<>();
        for (int i : arr) {
            this.data.add(i);
        }
        for (int i = sizeOfHeap / 2; i >= 0; i--) {
            siftDown(i);
        }

    }

    /**
     * Restores proper heap structure by raising lesser elements.
     *
     * @param index the index of element to start sifting up.
     */
    private void siftUp(int index) {

        int prev = (index - 1) / 2;
        while (data.get(prev) > data.get(index)) {

            int tmp = data.get(prev);
            data.set(prev, data.get(index));
            data.set(index, tmp);
            index = prev;
            prev = (index - 1) / 2;
            if (index == 0) break;
        }
    }

    /**
     * Restores proper heap structure by downing large elements.
     *
     * @param index the index of element to start sifting down.
     */
    private void siftDown(int index) {
        int next1 = index * 2 + 1;
        int next2 = index * 2 + 2;

        if (next2 < sizeOfHeap) {
            if (data.get(next2) < data.get(index)) {
                if (data.get(next1) < data.get(next2)) {
                    swap(next1, index);
                    siftDown(next1);
                } else {
                    swap(next2, index);
                    siftDown(next2);
                }
                return;
            }

        }
        if (next1 < sizeOfHeap) {
            if (data.get(next1) < data.get(index)) {
                swap(next1, index);
                siftDown(next1);
            }
        }

    }

//    /**
//     * Changes the position of element to correct position, for saving valid structure of heap.
//     *
//     * @param index the index of heap element, which could was changed.
//     * @see Heap#siftDown(int)
//     * @see Heap#siftUp(int)
//     */
//    private void correctionKey(int index) {
//        siftDown(index);
//        siftUp(index);
//    }

    /**
     * Adds element into heap.
     *
     * @param val the int element that will be added in heap.
     * @see Heap#siftUp(int)
     */
    public void add(int val) {
        data.add(val);
        siftUp(sizeOfHeap);
        sizeOfHeap++;
    }

    /**
     * Return minimal element from heap.
     *
     * @return Minimal element (minimal integer value) from heap.
     */
    public int getMin() {
        return data.get(0);
    }

    /**
     * Erase min value from heap with changing of element order.
     *
     * @return the minimal value
     * @throws IndexOutOfBoundsException if there is no element in heap.
     */
    public int extractMin() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException();
        }
        int res = data.get(0);
        sizeOfHeap--;
        swap(0, sizeOfHeap);
        data.remove(sizeOfHeap);
        siftDown(0);

        return res;
    }

    /**
     * Return size of heap.
     *
     * @return the size of heap.
     * @see Heap#toArray()
     */
    public int size() {
        return sizeOfHeap;
    }

    /**
     * Check heap for empty.
     *
     * @return {@code true} if this list contains at least one element.
     * @see Heap#sizeOfHeap
     */
    public boolean isEmpty() {
        return sizeOfHeap == 0;
    }

    /**
     * Creates sorted integer array with deconstruction the heap.
     * After this operation heap is empty.
     *
     * @return sorted intArray from heap.
     */
    public int[] toArray() {
        int[] arr = new int[sizeOfHeap];
        int index = 0;
        while (!isEmpty()) {
            arr[index++] = extractMin();
        }
        return arr;
    }

    /**
     * Sorts array using heap construction and deconstruction with extracting min value.
     *
     * @param arr is the array from which the sorted array will be created.
     * @return sorted array.
     * @see Heap#toArray()
     * @see Heap#Heap(int[])
     */
    public static int[] heapSort(int[] arr) {
        Heap heap = new Heap(arr);
        return heap.toArray();
    }

    /**
     * Return elements by index.
     *
     * @param index the index of element that will be returned.
     * @return the element by index.
     * @throws ArrayIndexOutOfBoundsException if index is invalid.
     */
    public int get(int index) {
        if (index < 0 || index > sizeOfHeap) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return data.get(index);
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
