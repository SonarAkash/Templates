package Algorithm;


public class ArrayHasher {
    private final long[] prefixHash;
    private final long[] power;
    private final int P = 31; // Prime base
    private final int MOD = 1_000_000_007;
    private final int[] arr;

    public ArrayHasher(int[] arr) {
        this.arr = arr;
        int n = arr.length;
        prefixHash = new long[n + 1];
        power = new long[n + 1];
        power[0] = 1;

        for (int i = 0; i < n; i++) {
            prefixHash[i + 1] = (prefixHash[i] * P + arr[i]) % MOD;
            power[i + 1] = (power[i] * P) % MOD;
        }
    }

    // Returns hash of arr[l..r] inclusive
    public long getHash(int l, int r) {
        return (prefixHash[r + 1] - (prefixHash[l] * power[r - l + 1]) % MOD + MOD) % MOD;
    }

    // Compares if arr[l1..r1] == arr[l2..r2]
    public boolean compareSegments(int l1, int r1, int l2, int r2) {
        if (r1 - l1 != r2 - l2) return false; // lengths must be equal
        return getHash(l1, r1) == getHash(l2, r2);
    }
}

