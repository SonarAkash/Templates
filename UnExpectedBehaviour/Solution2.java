package UnExpectedBehaviour;

public class Solution2 {
    /*
     * 
     * This solution  give TLE due to the nature of parameters used here
     * i.e target (val) is increasing always.
     */
    private int nums[], q[][];
    private Integer dp[][];

    public int minZeroArray(int[] nums, int[][] queries) {
        int n = nums.length;
        this.nums = nums;
        q = queries;
        int res = 0;
        for (int i = 0; i < n; i++) {
            dp = new Integer[1001][1001];
            if (nums[i] > 0) {
                int ans = rec(i, 0, 0);
                if (ans == Integer.MAX_VALUE)
                    return -1;
                res = Math.max(res, ans);
            }
        }
        return res;
    }

    private int rec(int nIdx, int qIdx, int val) {
        if (val == nums[nIdx]) {
            return qIdx;
        }
        if (qIdx >= q.length || val > nums[nIdx])
            return Integer.MAX_VALUE;
        if (dp[qIdx][val] != null) {
            return dp[qIdx][val];
        }
        int res = Integer.MAX_VALUE;
        if (q[qIdx][0] <= nIdx && q[qIdx][1] >= nIdx) {
            res = Math.min(res, rec(nIdx, qIdx + 1, val + q[qIdx][2]));
        }
        res = Math.min(res, rec(nIdx, qIdx + 1, val));
        return dp[qIdx][val] = res;
    }
}
