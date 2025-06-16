package Algorithm.Trees.LCA;
import java.util.*;

public class SqrRootDecomposition {
    private List<Integer> tree[];
    private int N, section;
    private int height[], ancestor[], parent[];
     
    public SqrRootDecomposition(List<Integer> tree[], int N){
        this.N = N;
        this.tree = tree;
        height = new int[N];
        ancestor = new int[N];
        parent = new int[N];
        init(0, 0, 0);
        System.out.println(Arrays.toString(height));
        System.out.println(Arrays.toString(parent));
        section = (int)Math.sqrt(Arrays.stream(height).max().getAsInt() + 1);
        System.out.println(section);
        calAncestor(0, 0);
        printTree();
    }
    
    private void init(int curr, int ht, int par){
        parent[curr] = par;
        height[curr] = ht;
        for(int nbr : tree[curr]){
            if(nbr != par){
                init(nbr, ht+1, curr);
            }
        }
    }

    private void calAncestor(int curr, int par){
        if(height[curr] < section){
            ancestor[curr] = 0;
        }else if(height[curr] % section == 0){
            ancestor[curr] = parent[curr];
        }else{
            ancestor[curr] = ancestor[parent[curr]];
        }
        for(int nbr : tree[curr]){
            if(nbr != par){
               calAncestor(nbr, curr);
            }
        }
    }
    
    public int findLCA(int x, int y){
        while(ancestor[x] != ancestor[y]){
            if(height[x] > height[y]){
                x = ancestor[x];
            }else{
                y = ancestor[y];
            }
        }
        while(x != y){
            if(height[x] > height[y]){
                x = parent[x];
            }else{
                y = parent[y];
            }
        }
        return x;
    }

    private void printTree(){
        Queue<Integer> q = new LinkedList<>();
        q.add(0);
        while(!q.isEmpty()){
            int len = q.size();
            for(int i=0; i<len; i++){
                int curr = q.remove();
                System.out.printf("curr : %d, parent : %d, height : %d, ancestor : %d\n", curr, parent[curr], height[curr], ancestor[curr]);
                for(int nbr : tree[curr]){
                    if(nbr != parent[curr]){
                        q.add(nbr);
                    }
                }
            }
            System.out.println();
        }
    }
}
