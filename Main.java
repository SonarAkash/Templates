
import java.util.*;

import DataStructure.CustomHashMap;

public class Main {
    public static void main(String[] args) {
      CustomHashMap<Integer, String> mp = new CustomHashMap<>();
      for(int i=0; i<5; i++){
        mp.put(i, "Present");
      }
      for(Map.Entry<Integer, String> it : mp){
        System.out.println(it.getKey() + "->" + it.getValue());
      }
    }
}