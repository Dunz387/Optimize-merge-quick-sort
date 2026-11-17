public class Sorting_Algorithms {

    // Quick sort
    public static void quick_sort(Comparable[] a, int lo, int hi, int cutoff) {
        if (hi <= lo + cutoff - 1) {
            insertion_sort(a, lo, hi);
            return;
        }
        int j = partition(a, lo, hi);
        quick_sort(a, lo, j - 1, cutoff);
        quick_sort(a, j + 1, hi, cutoff);
    }

    public static void quick_sort(Comparable[] a, int cutoff) {
        quick_sort(a, 0, a.length - 1, cutoff);
    }

    public static int partition(Comparable[] a, int lo, int hi) {
        int i = lo;
        int j = hi + 1;
        Comparable v = a[lo];
        while (true) {
            while (less(a[++i], v)) {
                if (i == hi)
                    break;
            }
            while (less(v, a[--j])) {
                if (j == lo)
                    break;
            }
            if (i >= j)
                break;
            exchange(a, i, j);
        }
        exchange(a, lo, j);
        return j;
    }

    // Merge sort
    public static void merge_sort(Comparable[] a, Comparable[] aux, int lo, int hi, int cutoff) {
        if (hi <= lo + cutoff - 1) {
            insertion_sort(a, lo, hi);
            return;
        }
        int mid = lo + (hi - lo) / 2;
        merge_sort(a, aux, lo, mid, cutoff);
        merge_sort(a, aux, mid + 1, hi, cutoff);
        merge(a, aux, lo, mid, hi);
    }

    public static void merge_sort(Comparable[] a, int cutoff) {
        Comparable[] aux = new Comparable[a.length];
        merge_sort(a, aux, 0, a.length - 1, cutoff);
    }

    public static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }

        int i = lo;
        int j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid)
                a[k] = aux[j++];
            else if (j > hi)
                a[k] = aux[i++];
            else if (less(aux[j], aux[i]))
                a[k] = aux[j++];
            else
                a[k] = aux[i++];
        }
    }

    // Insertion sort
    public static void insertion_sort(Comparable[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++) {
            for (int j = i; j > lo && less(a[j], a[j - 1]); j--) {
                exchange(a, j, j - 1);
            }
        }
    }

    public static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    public static void exchange(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
