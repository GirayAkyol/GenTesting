package my.example.project;

public class MyBinSearch {
    public int binarySearch(int[] arr, int target) {
        int low = 0;
        int high = arr.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            int midVal = arr[mid];

            if (midVal == target) {
                return mid;
            } else if (midVal > target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return -1;
    }

}
