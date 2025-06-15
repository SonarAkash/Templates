package Math;

import java.util.HashMap;
import java.util.Map;

public class Factors {
    public Map<Integer, Integer> factors(int num){
        if(num < 1) return null;
        /*
         * The smallest divisor a any number is always a prime number
         * 
         * The reason why we are going till sqrt(num) is that, because of the property that for a composite number (non-prime)
         * their always exits a number before sqrt(num) which is prime, so when we find the number which is prime , we then remove 
         * the multiples of that number (prime) from our curr num and when all the multiples of that prime is finished we get 
         * a new num and again because of the condition (sqrt(num)) in outer loop , we again a get a prime before sqrt(num) and repeat the same process again.
         */
        Map<Integer, Integer> primeFactors = new HashMap<Integer, Integer>();
        for(int i=2; i*i<num; i++){
            while(num % i == 0){
                num /= i;
                primeFactors.put(i, primeFactors.getOrDefault(i, 0) + 1);
            }
        }
        if(num > 1){
            // num is a prime number , and therefore don't have any prime before sqrt(num), so we have to add this prime number ourself
            primeFactors.put(num, primeFactors.getOrDefault(num, 0) + 1);
        }
        return primeFactors;
    }
}
