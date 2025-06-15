package DataStructure;

public class SegmentTree {
    private final int N;
    private int sgTr[], lazy[];

    public SegmentTree(int arr[]){
        this.N = arr.length;
        this.sgTr = new int[4*N];
        this.lazy = new int[4*N]; // only for sum/xor/and etc queries
        buildTree(0, N-1, 0, arr);
    }

    private void buildTree(int l, int r, int idx, int arr[]){
        if(l == r){
            sgTr[idx] = arr[l];
            return;
        }
        int mid = l + (r - l) / 2;
        buildTree(l, mid, 2*idx + 1, arr);
        buildTree(mid+1, r, 2*idx + 2, arr);
        sgTr[idx] = Math.min(sgTr[2*idx + 1], sgTr[2*idx + 2]);
    }

    private int query(int lo, int hi, int l, int r, int idx){
        if(lo > r || hi < l) return Integer.MAX_VALUE;
        if(l <= lo && r >= hi){
            // complete overlap
            return sgTr[idx];
        }else{
            // partial overlap
            int mid = lo + (hi - lo) / 2;
            int leftQuery = query(lo, mid, l, r, 2*idx + 1);
            int rightQuery = query(mid+1, hi, l, r, 2*idx + 2);
            return Math.min(leftQuery, rightQuery); 
        }
    }

    private void pointUpdate(int lo, int hi, int idx, int point, int val){
        int mid = lo + (hi - lo) / 2;
        if(lo == hi){
            sgTr[idx] = val;
            return;
        }
        if(point <= mid){
            pointUpdate(lo, mid, 2*idx + 1, point, val);
        }else{
            pointUpdate(mid + 1, hi, 2*idx + 2, point, val);
        }
        sgTr[idx] = Math.min(sgTr[2*idx + 1], sgTr[2*idx + 2]);
    }

    public int query(int l, int r){
        return query(0, N-1, l, r, 0);
    }
    
    public void pointUpdate(int idx, int val){
        pointUpdate(0, N-1, 0, idx, val);
    }

    private void lazyUpdate(int lo, int hi, int idx, int l, int r, int val){
        if(hi < l || lo > r){
            return;
        }
        if(lo >= l && hi <= r){
            sgTr[idx] += val;
            if(2 * idx + 1 < sgTr.length){
                lazy[2 * idx + 1] += val;
            } 
            if(2 * idx + 2 < sgTr.length){
                lazy[2 * idx + 2] += val;
            }
            return;
        }else{
            int mid = lo + (hi - lo) / 2;
            lazyUpdate(lo, mid, 2 * idx + 1, l, r, val);
            lazyUpdate(mid + 1, hi, 2 * idx + 2, l, r, val);
        }

    }

    public void lazyUpdate(int l, int r, int val){
        lazyUpdate(0, N-1, 0, l, r, val);
    }

    private int lazyQuery(int lo, int hi, int idx, int l, int r){
        if(hi < l || lo > r){
            return 0;
        }
        if(lazy[idx] != 0){
            int val = lazy[idx];
            sgTr[idx] += val;
            if(2 * idx + 1 < sgTr.length){
                lazy[2 * idx + 1] += val;
            } 
            if(2 * idx + 2 < sgTr.length){
                lazy[2 * idx + 2] += val;
            }
            lazy[idx] = 0;
        }
        if(lo >= l && hi <= r){
            return sgTr[idx];
        }else{
            int mid = lo + (hi - lo) / 2;
            int leftQuery = lazyQuery(lo, mid, 2 * idx + 1, l, r);
            int rightQuery = lazyQuery(mid + 1, hi, 2 * idx + 2, l, r);
            return leftQuery + rightQuery;
        }
    }

    public int lazyQuery(int l, int r){
        return lazyQuery(0, N-1, 0, l, r);
    }
}
