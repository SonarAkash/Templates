package DataStructure;
import java.util.*;

public class CustomHashMap<K, V> implements Iterable<Map.Entry<K, V>>{
    private class Pair implements Map.Entry<K, V>{
        K key;
        V value;

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }

        Pair(K key, V value){
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object other){
            if(other == null) return false;
            if(this == other) return true;
            if(other.getClass() != this.getClass()) return false;
            
            @SuppressWarnings("unchecked")
            Pair p = (Pair)other;
            return Objects.equals(p.key, this.key);
        }

        @Override
        public int hashCode(){
            return Objects.hash(key, value);
        }
    }

    private final double lambda =  0.75;
    private int size = 10;
    private int pairs = 0;
    @SuppressWarnings("unchecked")
    private LinkedList<Pair>[] mp = new LinkedList[size];


    public CustomHashMap(){
        for(int i=0; i<size; i++){
            this.mp[i] = new LinkedList<>();
        }
    }

    public void put(K key, V value){
        Pair pair = new Pair(key, value);
        int bucketIndex = getBucketIndex(key);
        boolean pairFound = false;
        for(Pair p : mp[bucketIndex]){
            if(p.equals(pair)){
                pairFound = true;
                p.value = value;
                break;
            }
        }
        if(!pairFound){
            mp[bucketIndex].addLast(pair);
            pairs++;
            if(lambda > (pairs / size * 1.0)){
                resizeMap();
            }
        }
    }

    public V get(K key){
        int bucketIndex = getBucketIndex(key);
        for(Pair p : mp[bucketIndex]){
            if(p.key == key){
                return p.value;
            }
        }
        return null;
    }

    public boolean containsKey(K key){
        int bucketIndex = getBucketIndex(key);
        for(Pair p : mp[bucketIndex]){
            if(p.key == key){
                return true;
            }
        }
        return false;
    }

    private void resizeMap(){
        this.size += this.size;
        @SuppressWarnings("unchecked")
        LinkedList<Pair>[] newMap = new LinkedList[size];
        for(int i=0; i<size; i++){
            newMap[i] = new LinkedList<>();
        }
        for(LinkedList<Pair> list : mp){
            for(Pair p : list){
                K key = p.key;
                int bucketIndex = getBucketIndex(key);
                newMap[bucketIndex].addLast(p);
            }
        }
    }

    private int getBucketIndex(K key){
        return key.hashCode() % size;
    }



    @Override
    public Iterator<Map.Entry<K, V>> iterator(){
        return new CustomIterator();
    }

    public class CustomIterator implements Iterator<Map.Entry<K, V>>{
        private Pair curr = null;
        private int bucketIndex = 0;
        private int listIndex = 0;

        CustomIterator(){
            moveToNextNonEmptyBucket();
            if(bucketIndex < size){
                curr = mp[bucketIndex].get(listIndex);
            }
        }


        private void moveToNextNonEmptyBucket(){
            while(bucketIndex < size && (mp[bucketIndex] == null || mp[bucketIndex].isEmpty())){
                bucketIndex++;
            }
        }

        @Override
        public boolean hasNext(){
            return curr != null;
        }

        @Override
        public Map.Entry<K, V> next(){
            if (curr == null) {
                throw new NoSuchElementException();
            }
            Pair currPair = curr;
            int currListSize = mp[bucketIndex].size();
            if(currListSize - 1 > listIndex){
                listIndex++;
                curr = mp[bucketIndex].get(listIndex);
            }else if(size - 1 > bucketIndex){
                bucketIndex++;
                listIndex = 0;
                moveToNextNonEmptyBucket();
                if(bucketIndex < size){
                    curr = mp[bucketIndex].get(listIndex);
                }else{
                    curr = null;
                }
            }else{
                curr = null;
            }
            return currPair;
        }
    }

}
