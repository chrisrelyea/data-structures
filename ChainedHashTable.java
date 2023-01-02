/*
 * ChainedHashTable.java
 *
 * Computer Science 112, Boston University
 * 
 * Modifications and additions by:
 *     name:
 *     email:
 */

import java.util.*;     // to allow for the use of Arrays.toString() in testing

/*
 * A class that implements a hash table using separate chaining.
 */
public class ChainedHashTable implements HashTable {
    /* 
     * Private inner class for a node in a linked list
     * for a given position of the hash table
     */
    private class Node {
        private Object key;
        private LLQueue<Object> values;
        private Node next;
        
        private Node(Object key, Object value) {
            this.key = key;
            values = new LLQueue<Object>();
            values.insert(value);
            next = null;
        }
    }
    
    private Node[] table;      // the hash table itself
    private int numKeys;       // the total number of keys in the table
        
    /* hash function */
    public int h1(Object key) {
        int h1 = key.hashCode() % table.length;
        if (h1 < 0) {
            h1 += table.length;
        }
        return h1;
    }
    
    /*** Add your constructor here ***/
    public ChainedHashTable(int size) {
        if (size < 0) {
            throw new IllegalArgumentException();
        }
        table = new Node[size];
        numKeys = 0;
    }




    
    /*
     * insert - insert the specified (key, value) pair in the hash table.
     * Returns true if the pair can be added and false if there is overflow.
     */
    public boolean insert(Object key, Object value) {
        /** Replace the following line with your implementation. **/
        if (key == null) {
            throw new IllegalArgumentException("key must be non-null");
        }
        int i = h1(key);

        if (table[i] == null) {
            table[i] = new Node(key, value);
            numKeys++;
        }

        else {
            Node currentNode = table[i];
            while (currentNode.next != null) {
                if (currentNode.key == key) {
                    break;
                }
                currentNode = currentNode.next;
            }
            if (currentNode.key == key) {
                currentNode.values.insert(value);
            }
            else if (currentNode.next == null) {
                currentNode.next = new Node (key, value);
                numKeys++;
            }

        }


        return true;
    }
    
    /*
     * search - search for the specified key and return the
     * associated collection of values, or null if the key 
     * is not in the table
     */
    public Queue<Object> search(Object key) {
        /** Replace the following line with your implementation. **/

        int i = h1(key);
        Node currentNode = table[i];
        while ((currentNode.key != key) && (currentNode.next != null)) {
            currentNode = currentNode.next;
        }
        if (currentNode.key == key) {
            return currentNode.values;
        }
        else {
            return null;
        }
    }
    
    /* 
     * remove - remove from the table the entry for the specified key
     * and return the associated collection of values, or null if the key 
     * is not in the table
     */
    public Queue<Object> remove(Object key) {
        /** Replace the following line with your implementation. **/
        int i = h1(key);

        if (table[i].key == key) {
            if (table[i].next == null) {
                Queue<Object> returnQueue = table[i].values;
                table[i] = null;
                numKeys--;
                return returnQueue;
            }
            else {
                Queue<Object> returnQueue = table[i].values;
                table[i] = table[i].next;
                numKeys--;
                return returnQueue;
            }
        }

        else {

        Node currentNode = table[i].next;
        Node prevNode = table[i];

        while ((currentNode.key != key) && (currentNode.next != null)) {
            prevNode = currentNode;
            currentNode = currentNode.next;
        }

        if (currentNode.key == key) {
            Queue<Object> values = currentNode.values;
            prevNode.next = currentNode.next;
            numKeys--;
            return values;

        }
        else {
            return null;
        }
    }




    }
    
    
    /*** Add the other required methods here ***/
    
    
    public int getNumKeys() {
        return this.numKeys;
    }

    public double load() {
        return ((double) this.getNumKeys() / table.length);
    }

    public Object[] getAllKeys() {
        Object[] returnArray;
        returnArray = new Object[this.getNumKeys()];
        int arrInd = 0;

        for (int i = 0; i < table.length-1; i++) {
            if (table[i] != null) {
                Node currentNode = table[i];
                while (currentNode != null) {
                    returnArray[arrInd] = currentNode.key;
                    arrInd++;
                    currentNode = currentNode.next;
                }
            }
        }
        return returnArray;
    }

    public void resize(int newSize) {
        if (newSize < this.table.length) {
            throw new IllegalArgumentException();
        }
        else if (newSize == this.table.length) {
            return;
        }
        else {
            ChainedHashTable resizedTable = new ChainedHashTable(newSize);

            for (int i = 0; i < table.length-1; i++) {
                if (table[i] != null) {
                    Node currentNode = table[i];
                    while (currentNode != null) {
                        resizedTable.insert(currentNode.key, currentNode.values);
                        currentNode = currentNode.next;
                    }
                }
            }
            this.table = resizedTable.table;
        }
    }



    /*
     * toString - returns a string representation of this ChainedHashTable
     * object. *** You should NOT change this method. ***
     */
    public String toString() {
        String s = "[";
        
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) {
                s += "null";
            } else {
                String keys = "{";
                Node trav = table[i];
                while (trav != null) {
                    keys += trav.key;
                    if (trav.next != null) {
                        keys += "; ";
                    }
                    trav = trav.next;
                }
                keys += "}";
                s += keys;
            }
        
            if (i < table.length - 1) {
                s += ", ";
            }
        }       
        
        s += "]";
        return s;
    }

    public static void main(String[] args) {
    System.out.println();
    System.out.println("Testing first method: insert");
    System.out.println();
    System.out.println("Test #1 on insert (using hash table from Problem 7.3");
    System.out.println("Expected return value:");
    System.out.println("true");
    ChainedHashTable table = new ChainedHashTable(5);
    table.insert("howdy", 15);
    System.out.println("Actual return value:");
    System.out.println(table.insert("goodbye", 10));
    System.out.println("Expected updated hash table:");
    System.out.println("[{howdy}, null, null, {goodbye}, null]");
    System.out.println("Actual updated hash table:");
    System.out.println(table);

    System.out.println();
    System.out.println("Test #2 on insert (using hash table from Problem 7.3, adding third key");
    System.out.println("Expected return value:");
    System.out.println("true");
    System.out.println("Actual return value:");
    System.out.println(table.insert("apple", 5));
    System.out.println("Expected updated hash table:");
    System.out.println("[{apple; howdy}, null, null, {goodbye}, null]");
    System.out.println("Actual updated hash table:");
    System.out.println(table);
    System.out.println();

    System.out.println("Testing second method: search");
    System.out.println();
    System.out.println("Test #1 on search (using hash table from Problem 7.3)");
    System.out.println("Expected return value:");
    System.out.println("{5}");
    System.out.println("Actual return value:");
    System.out.println(table.search("apple"));
    System.out.println("Test #2 on search (using hash table from Problem 7.3)");
    System.out.println("Expected return value:");
    System.out.println("{10}");
    System.out.println("Actual return value:");
    System.out.println(table.search("goodbye"));

    System.out.println();
    System.out.println("Testing third method: remove");
    System.out.println();
    System.out.println("Test #1 on remove (using hash table from Problem 7.3)");
    System.out.println("Expected return value:");
    System.out.println("{5}");
    System.out.println("Actual return value:");
    System.out.println(table.remove("apple"));
    System.out.println("Expected updated array:");
    System.out.println("[{howdy}, null, null, {goodbye}, null]");
    System.out.println("Actual updated array:");
    System.out.println(table);
    System.out.println();
    System.out.println("Test #2 on remove (using hash table from Problem 7.3)");
    System.out.println("Expected return value:");
    System.out.println("{10}");
    System.out.println("Actual return value:");
    System.out.println(table.remove("goodbye"));
    System.out.println("Expected return array:");
    System.out.println("[{howdy}, null, null, null, null]");
    System.out.println("Actual return array:");
    System.out.println(table);
    System.out.println();
    System.out.println("Testing fourth method: getNumKeys");
    System.out.println();
    System.out.println("Test #1 on getNumKeys using hash table from Problem 7.3:");
    ChainedHashTable table2 = new ChainedHashTable(5);
    table2.insert("howdy", 15);
    table2.insert("goodbye", 10);
    table2.insert("apple", 5);
    System.out.println("Expected return value:");
    System.out.println("3");
    System.out.println("Actual return value:");
    System.out.println(table2.getNumKeys());
    System.out.println();
    System.out.println("Test #2 on getNumKeys using hash table from Problem 7.3 with one key removed:");
    System.out.println("Expected return value:");
    System.out.println("2");
    System.out.println("Actual return value:");
    table2.remove("howdy");
    System.out.println(table2.getNumKeys());

    System.out.println();
    System.out.println("Testing fifth method: load");
    System.out.println();
    System.out.println("Test #1 on load using hash table from Problem 7.5:");
    ChainedHashTable table3 = new ChainedHashTable(5);
    table3.insert("howdy", 15);
    table3.insert("goodbye", 10);
    table3.insert("apple", 5);
    table3.insert("pear", 6);
    System.out.println("Expected return value:");
    System.out.println("0.8");
    System.out.println("Actual return value:");
    System.out.println(table3.load());
    System.out.println();
    System.out.println("Test #2 on load using hash table from Problem 7.5 with one key added:");
    table3.insert("hello", 8);
    System.out.println("Expected return value:");
    System.out.println("1.0");
    System.out.println("Actual return value:");
    System.out.println(table3.load());

    System.out.println();
    System.out.println("Testing fifth method: getAllKeys");
    System.out.println();
    System.out.println("Test #1 on getAllKeys using hash table from Problem 7.6:");
    System.out.println();
    ChainedHashTable table4 = new ChainedHashTable(5);
    table4.insert("howdy", 15);
    table4.insert("goodbye", 10);
    table4.insert("apple", 5);
    table4.insert("howdy", 25); 
    Object[] keys = table4.getAllKeys();
    System.out.println("Expected return array:");
    System.out.println("[howdy, apple, goodbye]");
    System.out.println("Actual return array:");
    System.out.println(Arrays.toString(keys));
    System.out.println();
    System.out.println("Test #2 on getAllKeys using hash table from Problem 7.6 with duplicate key added:");
    table4.insert("howdy", 26); 
    System.out.println("Expected return array:");
    System.out.println("[howdy, apple, goodbye]");
    System.out.println("Actual return array:");
    System.out.println(Arrays.toString(keys));
    System.out.println();
    System.out.println("Testing fifth method: resize");
    System.out.println();
    System.out.println("Test #1 on resize using hash table from Problem 7.7:");
    System.out.println();
    ChainedHashTable table5 = new ChainedHashTable(5);
    table5.insert("howdy", 15);
    table5.insert("goodbye", 10);
    table5.insert("apple", 5);
    table5.resize(7);
    System.out.println("Expected new table:");
    System.out.println("[null, {apple}, null, null, null, {howdy}, {goodbye}]");
    System.out.println("Actual new table:");
    System.out.println(table5);
    System.out.println();
    System.out.println("Test #2 on resize using hash table from Problem 7.7 increased size:");
    System.out.println();
    table5.resize(10);
    System.out.println("Expected new table:");
    System.out.println("[{apple}, null, null, null, null, {howdy}, null, null, null, null]");
    System.out.println("Actual new table:");
    System.out.println(table5);



    





    


    




    }
}
