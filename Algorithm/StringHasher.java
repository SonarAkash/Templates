
package Algorithm;


public class StringHasher {
    private final long[] prefixHash;
    private final long[] power;
    private final int P = 31; // base (can also use 53)
    private final int MOD = 1_000_000_007;
    private final String s;

    public StringHasher(String s) {
        this.s = s;
        int n = s.length();
        prefixHash = new long[n + 1];
        power = new long[n + 1];
        power[0] = 1;

        for (int i = 0; i < n; i++) {
            char ch = s.charAt(i);
            prefixHash[i + 1] = (prefixHash[i] * P + ch) % MOD;
            power[i + 1] = (power[i] * P) % MOD;
        }
    }

    // Returns hash of substring s[l..r] inclusive
    public long getHash(int l, int r) {
        return (prefixHash[r + 1] - (prefixHash[l] * power[r - l + 1]) % MOD + MOD) % MOD;
    }

    // Compares if s[l1..r1] == s[l2..r2]
    public boolean compareSubstrings(int l1, int r1, int l2, int r2) {
        if (r1 - l1 != r2 - l2) return false; // length mismatch
        return getHash(l1, r1) == getHash(l2, r2);
    }
}
