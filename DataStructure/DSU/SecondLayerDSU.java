/* problem link : https://www.hackerearth.com/practice/data-structures/disjoint-data-strutures/basics-of-disjoint-data-structures/practice-problems/algorithm/team-up-2-8dc5d882/
               
       The problem involves a technique called second layer DSU, in which we create two dsu layer to eliminate the dependence
       of a layer on itself. For example if we are forming teams using union but what if we wanna change the team of a member
       to some other team, this is not possible in DSU since after forming a group, no member can't leave the group and even if 
       they do then it creates problem (if parent of the team / group changes it's team and goes from A to B, then who is the 
       new parent of A then ? and therefore we need to update parent for every member with new parent chosen for team A which
       is not feasible and very difficulty). To overcome this problem, we introduce a new layer called team . Now any person
       can directly point to the team rather than pointing to a person and also that team can point to it's parent team, making it
       easy for any member to leave a team/group and join another one. It also makes it easy for team to merge, for example
       if we wanna merge team C and D, we can directly point Team C to D or vice versa. 

    */
package DataStructure.DSU;

import java.io.BufferedReader;
import java.io.InputStreamReader;
class SecondLayerDSU {
    public static void main(String args[] ) throws Exception {
        //BufferedReader
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        while(T-- > 0){
            String s[] = br.readLine().split(" ");
            int N = Integer.parseInt(s[0]);
            int Q = Integer.parseInt(s[1]);
            UnionFind uf = new UnionFind(N);
            // System.out.println("T");
            while(Q-- > 0){
                s = br.readLine().split(" ");
                int op = Integer.parseInt(s[0]);
                int A = Integer.parseInt(s[1]);
                if(op == 2){
                    uf.printSizeNStrength(A);
                }else{
                    int B = Integer.parseInt(s[2]);
                    if(op == 3){
                        if(!uf.isConnected(A, B)){
                            uf.changeTeam(A, B);
                        }
                    }else{
                        uf.union(A, B);
                    }
                }
            }
        }
        


    }

    static class UnionFind{
        private int team[]; // team to team
        private int par[]; // child to team
        private int info[][]; // [0] = strength and [1] = members
        UnionFind(int N){
            team = new int[N + 1];
            par = new int[N + 1];
            info = new int[N + 1][2];
            for(int i=1; i<N+1; i++){
                team[i] = par[i] = i;
                info[i][0] = i;
                info[i][1] = 1;
            }
        }
        private void printSizeNStrength(int A){
            int teamA = find(par[A]);
            System.out.println(info[teamA][1] + " " + info[teamA][0]);
        }
        private void changeTeam(int x, int y){
            int TeamX = par[x];
            int parTeamX = find(TeamX);
            info[parTeamX][0] -= x;
            info[parTeamX][1]--;

            int TeamY = par[y];
            int parTeamY = find(TeamY);
            par[x] = parTeamY;
            info[parTeamY][0] += x;
            info[parTeamY][1]++;
        }
        private int find(int x){
            if(team[x] == x) return x;
            return team[x] = find(team[x]);
        }
        private void union(int x, int y){
            int parTeamX = find(par[x]), parTeamY = find(par[y]);
            if(parTeamX != parTeamY){
                if(info[parTeamX][1] > info[parTeamY][1]){
                    info[parTeamX][1] += info[parTeamY][1];
                    info[parTeamX][0] += info[parTeamY][0];
                    team[parTeamY] = parTeamX;
                }else if(info[parTeamX][1] < info[parTeamY][1]){
                    info[parTeamY][1] += info[parTeamX][1];
                    info[parTeamY][0] += info[parTeamX][0];
                    team[parTeamX] = parTeamY;
                }else{
                    info[parTeamX][1] += info[parTeamY][1];
                    info[parTeamX][0] += info[parTeamY][0];
                    team[parTeamY] = parTeamX;
                }
            }
        }
        private boolean isConnected(int x, int y){
            return find(par[x]) == find(par[y]);
        }
    }
}
