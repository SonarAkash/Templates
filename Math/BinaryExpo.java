package Math;
public class BinaryExpo {
    private final long MOD = 1_000_000_007;
    private long binaryExpo(long a, long b, long M){
        long res = 1L;
        while(b > 0){
            if((b & 1) == 1){
                res = (res * a) % M;
            }
            a = (a * a) % M;
            b >>= 1;
        }
        return res;
    }
    public long modInverse(long a, long m){
        return binaryExpo(a, m - 2, m);
    }
    public long power(int a, int b){
        return binaryExpo(a, b, this.MOD);
    }
}