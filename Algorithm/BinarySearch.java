package Algorithm;
public class BinarySearch {

    public static void main(String args[]){
        int arr[] = {1,2,3,4,5,5,7,9};
        int idx = bs(arr, 5);
        System.out.println("Idx for value " + 5 + " is :  " + idx);
        idx = lowerBound(arr, 8);
        System.out.printf("lower bound for %d : %d \n", 5, idx);
        idx = upperBound(arr, 8);
        System.out.printf("upper bound for %d : %d \n", 5, idx);
    }

    private static int lowerBound(int arr[], int target){
        int lo = 0, hi = arr.length;
        while(lo < hi){
            int mid = lo + (hi - lo) / 2;
            if(arr[mid] < target){
                lo = mid + 1;
            }else{
                hi = mid;
            }
        }
        return lo;
    }

    private static int upperBound(int arr[], int target){
        int lo = 0, hi = arr.length;
        while(lo < hi){
            int mid = lo + (hi - lo) / 2;
            if(arr[mid] <= target){
                lo = mid + 1;
            }else{
                hi = mid;
            }
        }
        return lo;
    }

    private static int bs(int arr[], int target){
        int lo = 0, hi = arr.length-1;
        while(lo <= hi){
            int mid = lo + (hi - lo) / 2;
            if(arr[mid] == target){
                return mid;
            }
            if(arr[mid] > target){
                hi = mid - 1;
            }else{
                lo = mid + 1;
            }
        }
        return -1;
    }
}