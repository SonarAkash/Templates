package Math;

public class NCR {

    final int N = 1_000_000;
    final int MOD = 1_000_000_007;

    private long fact[];
    private long invFact[];

    public NCR(){
        compute();
    }


    /* 
     * if we wanna compute (a/n) mod M, then it is computed as (a * n^-1) mod M, where n^-1 is modular 
     * multiplicative inverse and M is prime. By Fermat's theorem n^-1 = (n^(M - 1)) MOD M, which can be
     * found out using binary exponentition
    */

    private void compute(){
        this.fact = new long[N+1];
        this.invFact = new long[N+1];

        fact[0] = 1L;
        for(int i=1; i<=N; i++){
            fact[i] = (i * fact[i-1]) % MOD;
        }

        BinaryExpo bExpo = new BinaryExpo();
        invFact[N] = bExpo.modInverse(fact[N], MOD);
        for(int i=N-1; i>=0; i--){
            invFact[i] = (invFact[i+1] * (i+1)) % MOD;
        }
    }

    public long ncr(int n, int r){
        if(r > n) return 0L;
        return ((((fact[n] * invFact[r]) % MOD) * invFact[n-r]) % MOD);
    }

}