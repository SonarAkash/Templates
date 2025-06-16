package Algorithm.Trees.LCA;
import java.util.*;

public class BinaryLifiting {

    private List<Integer> tree[];
    private int timer, H;
    private int inTime[], outTime[];
    private int up[][];

    public BinaryLifiting(List<Integer> tree[], int N){
        this.tree = tree;
        timer = 0;
        H = (int)(Math.log(N)/Math.log(2));
        inTime = new int[N];
        outTime = new int[N];
        up = new int[N][H+1];
        dfs(0, 0);
    }
    
    private void dfs(int curr, int par){
        inTime[curr] = ++timer;
        up[curr][0] = par;
        for(int i=1; i<=H; i++){
            up[curr][i] = up[up[curr][i-1]][i-1];
        }
        for(int nbr : tree[curr]){
            if(nbr != par){
                dfs(nbr, curr);
            }
        }
        outTime[curr] = ++timer;
    }
     
    private boolean isAncestor(int u, int v){
        return inTime[u] <= inTime[v] && outTime[u] >= outTime[v];
    }

    public int lcaQuery(int u, int v){
        if(isAncestor(u, v)){
            return u;
        }else if(isAncestor(v, u)){
            return v;
        }else{
            for(int i=H; i>=0; i--){
                if(!isAncestor(up[u][i], v)){
                    u = up[u][i];
                }
            }
        }
        return up[u][0];
    }
}