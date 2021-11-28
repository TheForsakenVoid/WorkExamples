package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;


public class Friends {
        
    private static void cliqueCheck (Graph g, Person person, String school, boolean[] array, ArrayList<String> list){
        Queue<Person> queue = new Queue<Person>();
        queue.enqueue(person);
        
        while(!queue.isEmpty()){
            Person person2 = queue.dequeue();
            list.add(person2.name);
            int name = g.map.get(person2.name);
            Friend friend = g.members[name].first;
            array[name] = true;
            
            while(friend!=null){
                String str = g.members[friend.fnum].school;
                if(str == null){
                    friend=friend.next;
                }
            
                if(array[friend.fnum] == false && str.equals(school)){
                    queue.enqueue(g.members[friend.fnum]);                                        
                    array[friend.fnum] = true; 
                }
                friend = friend.next;
            }
        }
    }

    private static void connectChecker(Graph g, int curr, int prev, int start, boolean[] case1, boolean[] case2, int[] array1, int array2[], ArrayList<String> list){
        if(case1[curr])
            return;

        Friend friend = g.members[curr].first;
        array1[curr] = array1[prev]+1;
        array2[curr] = array1[curr];
        case1[curr] = true;

        while(friend!=null){
            if(case1[friend.fnum]){
                array2[curr] = Math.min(array2[curr],array1[friend.fnum] );
            }
            else{
                connectChecker(g, friend.fnum, curr, start, case1, case2, array1, array2, list);
                if(array1[curr]<=array2[friend.fnum] && !list.contains(g.members[curr].name)){
                    if(curr!=start || case2[curr]){
                        list.add(g.members[curr].name);
                    }
                }
                if(array1[curr]>array2[friend.fnum]){
                    array2[curr] = Math.min(array2[curr], array2[friend.fnum]);
                }
                case2[curr] = true;
            }
            friend = friend.next;
        }
    }

    public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
        try {
            Queue<Person> queue = new Queue<Person>();
            boolean [] arrayBool = new boolean [g.members.length];
            String [] arrayStr = new String[g.members.length];
            ArrayList<String> list = new ArrayList<String>();
            boolean bool = false;
            Person person1 = g.members[g.map.get(p1)];
            Person person2 = g.members[g.map.get(p2)];
            queue.enqueue(person1);
            
            for(int x=0; x<arrayStr.length;x++){
                arrayStr[x] = "N/A";
            }
    
            while(!queue.isEmpty()){
                Person temp = queue.dequeue();
                if(temp == person2){
                    bool = true; 
                }
                int name = g.map.get(temp.name);
                arrayBool[name] = true; 
                Friend friend = g.members[name].first;
    
                while(friend!=null){
                    if(!arrayBool[friend.fnum]){
                        queue.enqueue(g.members[friend.fnum]);
                        arrayStr[friend.fnum] = name+"";
                        arrayBool[friend.fnum] = true; 
                    }
                    friend = friend.next;
                }
            }
                
            if(!bool)
                return new ArrayList<String>();
                
            Stack <String> stack = new Stack<String>();
            int target = g.map.get(person2.name);
            String temp = arrayStr[target];
            stack.push(person2.name);
            
            while(!temp.equals("N/A")){
                stack.push(g.members[Integer.parseInt(temp)].name);
                temp = arrayStr[Integer.parseInt(temp)];
            }
            
            while(!stack.isEmpty()){
                list.add(stack.pop());
            }
            return list;
        }
        catch(NullPointerException e) {
            return new ArrayList<String>();
        }
    }
    
    public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();    
        boolean[] array = new boolean[g.members.length];
    
        for(int x=0; x<g.members.length; x++){
            try {
                ArrayList<String> list2 = new ArrayList<String>(); 
                Person person = g.members[x];
                int name = g.map.get(person.name);
                if(person.school.equals(school) && !array[name]){
                    cliqueCheck(g, person, school, array, list2);    
                    list.add(list2);
                }
            }
            catch(NullPointerException e) {                
            }
        }
        return list;
    }

    public static ArrayList<String> connectors(Graph g) {
        ArrayList<String> list = new ArrayList<String>();
        boolean[] arr1 = new boolean[g.members.length];
        boolean[] arr2 = new boolean[g.members.length];
        int[] arr3 = new int[g.members.length];
        int[] arr4 = new int[g.members.length];
        
        for(int x=0; x<g.members.length;x++){
            if(!arr1[x]){
                connectChecker(g, x, x, x, arr1, arr2, arr3, arr4, list);
            }
        }
        return list;
    }
            
}
