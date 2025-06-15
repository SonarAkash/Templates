package Math;

import java.util.Arrays;

public class Sieve {
    private final int N = 1_000_0000;
    private boolean isPrime[];

    /* 
     * Some Properties
     * 1. For a coprime number n the first smallest number other than 1 is alwasy a prime.
     * 2. For a coprime number there will always be a prime number before sqrt(n)
    */

    public Sieve(){
        this.isPrime = new boolean[N+1];
        Arrays.fill(isPrime, true);
        compute();
    }

    private void compute(){
        isPrime[0] = false;
        isPrime[1] = false;
        for(int i=2; i<=N; i++){
            if(isPrime[i]){
                for(int j=2*i; j<=N; j+=i){
                    isPrime[j] = false;
                }
            }
        }
    }

    public boolean checkPrime(int n){
        return isPrime[n];
    }
}
