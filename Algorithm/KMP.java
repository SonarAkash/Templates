package Algorithm;

import java.util.ArrayList;
import java.util.List;

public class KMP {
    public List<int[]> kmpSearch(String text, String pattern){
        if(text == null || pattern == null || text.length() < pattern.length()) return new ArrayList<>();
        int lps[] = calLps(pattern);
        List<int[]> patternMatch = new ArrayList<>();
        int j = 0, i = 0;
        while(i < text.length()){
            if(text.charAt(i) == pattern.charAt(j)){
                j++;
                i++;
                if(j == pattern.length()){
                    patternMatch.add(new int[]{i - pattern.length(), i - 1});
                    j = lps[j-1];
                }
            }else{
                if(j != 0){
                    j = lps[j-1];
                }else{
                    i++;
                }
            }
        }
        return patternMatch;

    }
    private int[] calLps(String pattern){
        int n = pattern.length();
        int lps[] = new int[n];
        lps[0] = 0;
        int len = 0;
        int i = 1;
        while(i < n){
            if(pattern.charAt(i) == pattern.charAt(len)){
                len++;
                lps[i] = len;
                i++;
            }else{
                if(len != 0){
                    len = lps[len-1];
                }else{
                    lps[i] = len;
                    i++;
                }
            }
        }
        return lps;
    }
}
