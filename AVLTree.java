/*
Jacqueline Saelee
COMP 282
Project #4
AVL Tree
AVLTree.java
*/

package avltree;

import java.util.Random;

class AVLNode<E>
{
   E item;   
   AVLNode<E> left;
   AVLNode<E> right;
   AVLNode<E> parent;
   
   // ADDED
   int height;
  
   public AVLNode ( E  x)
   {
      item = x; left = null; right = null; parent = null; height = 0;
      
   }
   
   public AVLNode (E x , AVLNode<E> left, AVLNode<E> right, AVLNode<E> parent)
   {
      item = x; 
      this.left = left; this.right = right; this.parent = parent;   
   }
   
   @Override
   public String toString()
   {
       String s = "(i:" + this.item + ", h:" + this.height + ")\n"; 
       return s;
   }
}

/*----------------class AVLTree ---------------------------*/
public class AVLTree<E extends Comparable<E>>
{
   private AVLNode<E> root;
   private int size;
  
   
   
   public AVLTree()
   {  root = null;  size = 0;  
   }
   
   /*---------------- public operations --------------------*/
   
      
        
   
   public int getSize()
   {  
      return size;
   }
   
        
   public boolean find( E x)
   {
      if( find(x,root) == null)
         return false;
      else
         return true;
   }
    
   
   public void preOrderTraversal()
   {
      preOrder (root);
      System.out.println();
   }
   
   public void inOrderTraversal()
   {
      inOrder (root);
      System.out.println();
   }
   
      
   public boolean insert( E x )
   {
   
      if( root == null)
      {
         root = new AVLNode(x, null, null, root);
         size++;
         return true;
      }    
       
      AVLNode<E> parent = null;
      AVLNode<E>  p = root;
      
      while (p != null)
      {
         if(x.compareTo(p.item) < 0)
         {
            parent = p; p = p.left; 
         }
         else if ( x.compareTo(p.item) > 0)
         {
            parent = p; p = p.right;
         }
         else  // duplicate value
            return false;  
      }
      
      //attach new node to parent
      AVLNode<E> insertedNode = new AVLNode<>(x, null, null, parent);
      if( x.compareTo(parent.item) < 0){
         parent.left = insertedNode;
         insertedNode.parent = parent;
         walk(parent.left);
      }
      else{
         parent.right = insertedNode;
         insertedNode.parent = parent;
         walk(parent.right);
      }
      size++; 
      return true;   
        
   }  //insert
   
   
   public boolean remove(E x)
   {
      if(root == null)
         return false;  //x is not in tree
     
      //find x
      AVLNode<E> p = find(x, root);
      if( p == null)
         return false;  //x not in tree
                  
      //Case: p has a right child child and no left child
      if( p.left == null && p.right != null)
         deleteNodeWithOnlyRightChild(p);
            
       //Case: p has a left child and has no right child
      else if( p.left !=null && p.right == null)
         deleteNodeWithOnlyLeftChild(p);
         
            //case: p has no children
      else if (p.left ==null && p.right == null)
         deleteLeaf(p);
                
      else //case : p has two children. Delete successor node
      {
         AVLNode<E> succ =  getSuccessorNode(p);
        
         p.item = succ.item;
           
          //delete succ node
         if(succ.right == null)
            deleteLeaf(succ);
         else
            deleteNodeWithOnlyRightChild(succ);
         
      }
      return true;         
   }   //remove
   
   
 
  /********************private methods ******************************/
  
        

   private AVLNode<E> find(E x, AVLNode<E> t)  
   {
      AVLNode<E> p = t;
      while ( p != null)
      {
         if( x.compareTo(p.item) <0)
            p = p.left;
         else if (x.compareTo(p.item) > 0)
            p = p.right;
         else  //found x
            return p;
      }
      return null;  //x is not found
   }
   
             
     /***************** private remove helper methods ***************************************/
   
   private void deleteLeaf( AVLNode<E> t)
   {
      if ( t == root)
         root = null;
      else
      {
         AVLNode<E>  parent = t.parent;
         if( t.item.compareTo(parent.item) < 0){
            parent.left = null;
            
            // RIGHT child is NOT null
            if(parent.right != null){
                parent.height = parent.right.height + 1;
            }
            // NO children
            else{
                parent.height = parent.height - 1;
            }
         }
         else{
            parent.right = null;
            
            // RIGHT child is NOT null
            if(parent.left != null){
                parent.height = parent.left.height + 1;
            }
            // NO children
            else{
                parent.height = parent.height - 1;
            }
         }
         
         // ADDED "walk"
         walk(parent);
      }
      size--;
   }
    
   private void deleteNodeWithOnlyLeftChild( AVLNode<E> t)
   {
      if( t == root)
      {
         root = t.left; root.parent = null; //WAS WRONG t.left.parent = root;
      }
      else
      {
         AVLNode<E> parent = t.parent;
         if( t.item.compareTo( parent.item)< 0)
         {
            parent.left = t.left;
            t.left.parent = parent;
             // ADDED "walk"
             walk(parent.left);
             //System.out.print("LEFT");
         }
         else
         {
            parent.right = t.left;
            t.left.parent = parent; 
            walk(parent.right);
            //System.out.print("p.RIGHT");
         }
    
      }
      size--;      
   }                  
     
   private void deleteNodeWithOnlyRightChild( AVLNode<E> t)
   {
      if( t == root)
      {
         root = t.right; root.parent = null; // WAS WRONG t.right.parent = root
         
      }
      else
      {
         AVLNode<E> parent = t.parent;
         if( t.item.compareTo(parent.item) < 0)
         {
            parent.left = t.right;
            t.right.parent = parent;
             //System.out.print("p.RIGHT");
             // ADDED "walk"
            walk(parent.left);  
         }
         else
         {
            parent.right = t.right;
            t.right.parent = parent;
             //System.out.print("p.RIGHT");
            // ADDED "walk"
            walk(parent.right);
         }   
      }
      size--;         
   }                  

   private AVLNode<E>  getSuccessorNode(AVLNode<E> t)
   {
     //only called when t.right != null
      AVLNode<E> parent = t;
      AVLNode<E> p = t.right;
      while (p.left != null)
      {
         parent = p; p = p.left; 
      }
      return p;
   }
     
   
               
   //private traversal methods      
           
         
   private void preOrder ( AVLNode<E> t)
   {
      if ( t != null)
      {
         System.out.print(t + " ");
         preOrder(t.left);
         preOrder(t.right);
      }
   }
     
   private void inOrder ( AVLNode<E> t)
   {
      if ( t != null)
      {
             
         inOrder(t.left);
         System.out.print(t + " " );
         inOrder(t.right);
      }
   }
   // ADDED FUNCTIONS
   public AVLNode<E> findMin(){
       if(root == null)
          System.exit(0);
       
       AVLNode<E> curr = root;
       while(curr.left != null){
          curr = curr.left; 
       }       
        return curr;
   }
   
   
      public AVLNode<E> findMax(){
       if(root == null)
          System.exit(0);
       
       AVLNode<E> curr = root;
       while(curr.right != null){
          curr = curr.right; 
       }       
        return curr;
   }
   
   public AVLNode<E> removeMin(){
       AVLNode<E> curr = this.findMin();
       this.remove(curr.item);
       return curr;
   }
   
    public AVLNode<E> removeMax(){
                
       AVLNode<E> curr = this.findMax();
       this.remove(curr.item);
       return curr;

   }
   
   
   
   public int getHeight(){
       return root.height;
   }

   // ADDED: Ancestor Walk
   public void walk( AVLNode<E> t ){
        
       // P4: ADDED
       // if NOT height-balanced, restructure.
       
       AVLNode<E> curr = t.parent;
       if(!this.isHeightBalanced(t)){
           restructure(t);
       }
       while(curr != null){
            AVLNode<E> rChild = curr.right;
            AVLNode<E> lChild = curr.left;
           
           // IF parent has two children
           if(rChild!= null && lChild != null){
               if(rChild.height > lChild.height){
                   curr.height = rChild.height + 1;
               }
               else
                   curr.height = lChild.height + 1;
           }
           // IF Parent only has RIGHT Child
           else if(rChild != null){
               curr.height = rChild.height + 1;
           }
           // IF Parent only has LEFT Child
           else if(lChild != null){
               curr.height = lChild.height + 1;
           }
           // Go to parent's parent
           //if(curr.parent != root)
            curr = curr.parent;
       }   
   } //Walk  
   
  // Driver View FUNCS
    
    public void insertShowAll(AVLTree<Integer> b,int n){
        Random rand = new Random();
        System.out.println("\tBST of random numbers: n = " + n + "\n");
        System.out.println("\tn\tlog2(n)\t   height\n\t- - - - - - - - - - - - - ");
        
        for(int i = 1; i <= 100; i ++){
            b.insert(rand.nextInt(1000000) + 1);
            System.out.println("\t" + i + "\t   " + (int)log2(i) + "\t     " + b.getHeight());
        }
    }
    
    public void insertRand(AVLTree<Integer> b, int n){
        Random rand = new Random();
        System.out.println("\tn\tlog2(n)\t   height\n\t- - - - - - - - - - - - - ");
        
        for(int i = 1; i <= n; i ++){
            b.insert(rand.nextInt(1000000) + 1);
            if((i % 100 == 0) && i <= 400)
                System.out.println("\t" + i + "\t   " + (int)log2(i) + "\t     " + b.getHeight());
            else if((i % 500 == 0) && i >= 500 && i <= 5000 )
                 System.out.println("\t" + i + "\t   " + (int)log2(i) + "\t     " + b.getHeight());
        }
    }  

    public static double log2(int n)
{
    return (Math.log(n) / Math.log(2));
}
    
    // ADDED
    // 2. Height Balanced
    private boolean isHeightBalanced(AVLNode<E> t){
        // Boolean currValue
        // Tree is empty
        if(this.root == null)
            return true;
        // NO Children
        if(t.right == null && t.left == null){
            return true;
        }
        // ONLY RIGHT Child
        else if(t.right != null && t.left == null){
            return(
                  isHeightBalanced(t.right) &&  
                  (Math.abs(-1 - t.right.height) <= 1));
        }
        // ONLY LEFT Child
        else if(t.right == null && t.left != null){
            return(
                    isHeightBalanced(t.left) &&
                    (Math.abs(-1 - t.left.height) <= 1));             
        }
        // TWO Children
        return(
                
               isHeightBalanced(t.left)  && 
               isHeightBalanced(t.right) && 
               (Math.abs(t.left.height - t.right.height) <= 1));
    }
    
    public boolean isHeightBalanced(){
        return isHeightBalanced(root);
    }
    
    // 3. Restructure
    // RETURNS "TOP" of restructured subtree 
    private AVLNode<E> restructure(AVLNode<E> t){
        // t is the "unbalanced" node
        // Initialize VARS
        AVLNode<E> ret; // Unbalanced Node
        AVLNode<E> savedParent = t.parent;
        AVLNode<E> y;  // Tallest Child of t
        AVLNode<E> z;  // Tallest Child of y
        AVLNode<E> t1, t2, t3, t4;
        
        // Find VARS
        y = findY(t);
        z = findZ(t, y);
        
        
        // Restructure for each case
     
        //CASE 1: T's LChild is Y
        //        Y's LChild is Z
        if(t.left == y && y.left == z){
            ret = case1(t, y, z, savedParent);
        }
        // CASE 2: T's RChild is Y
        //         Y's RChild is Z
        else if(t.right == y && y.right == z){
            ret = case2(t, y, z, savedParent);
        }
        // CASE 3: T's LChild is Y
        //         Y's RChild is Z
        else if(t.left == y && y.right == z){
            ret = case3(t, y, z, savedParent);
        }
        // CASE 4: T's RChild is Y
        //         Y's LChild is Z
        else
            ret = case4(t, y, z, savedParent);
        
        return ret;
    }
    
    private AVLNode<E> findY(AVLNode<E> x){
        // FIND Y
        AVLNode<E> y;
        //2 Children
        if(x.right != null && x.left != null){
            if(x.right.height > x.left.height){
                y = x.right;
            }
            else 
                y = x.left;      
        }
        // ONLY RIGHT Child
        else if(x.right != null && x.left == null){
            y = x.right;
        }
        // ONLY LEFT Child
        else if(x.right == null && x.left != null){
            y = x.left;
        }
        // NO Children ( May not be possible )
        else{
            y = null;
            System.out.println("Y is null.  Terminating");
            System.exit(0);
        }
        
        return y;
    }
    
    private AVLNode<E> findZ(AVLNode<E> x, AVLNode<E> y){
        // Setup VAR
        AVLNode<E> z;
        // FIND z
        // 2 Children
        if(y.right != null && y.left != null){
            // R > L
            if(y.right.height > y.left.height){
                z = y.right;
            }
            // R < L
            else if(y.right.height < y.left.height){
                z = y.left;
            }
            // R == L (Tie-Breaker)
            else{
                // If y is a Left Child of x
                if(x.left == y)
                    z = y.left;
                else
                    z = y.right;         
            }               
        }
        // ONLY RIGHT Child
        else if(y.right != null && y.left == null){
            z = y.right;
        }
        // ONLY LEFT Child
        else if(x.right == null && x.left != null){
            z = y.left;
        }
        // NO children
        else{
            z = null;
            System.out.println("Z is null.  Terminating");
            System.exit(0);
        }
        
        return z;
    }
    
    private AVLNode<E> case1(AVLNode<E> x, AVLNode<E> y, AVLNode<E> z, AVLNode<E> savedParent){
        // Initiatlize VARS
        AVLNode<E> ret, t1, t2, t3, t4;
        
        t1 = z.left;
        t2 = z.right;
        t3 = y.right;
        t4 = x.right;
        
        // Y is TOP
        ret = y;
        y.parent = savedParent;
        y.left = z;
        y.right = x;
        
        // Z LChild of Y
        z.parent = y;
        z.left = t1;
        z.right = t2;
        
        // X RChild of Y
        x.parent = y;
        x.left = t3;
        x.right = t4;
        
        // Re-Adjust Heights
        // z
        if(t1 != null) 
            walk(t1);
        else if(t2 != null) 
            walk(t2);
        else 
            z.height = 0;
        
        // x
        if(t3 != null) 
            walk(t3);
        else if(t4 != null) 
            walk(t4);
        else 
            x.height = 0;
        
        walk(z);
        walk(x);
        walk(y);
        
        return ret;
    }
    
    private AVLNode<E> case2(AVLNode<E> x, AVLNode<E> y, AVLNode<E> z, AVLNode<E> savedParent){
        // Initiatlize VARS
        AVLNode<E> ret, t1, t2, t3, t4;
        
        t1 = x.left;
        t2 = y.left;
        t3 = z.left;
        t4 = z.right;
        
        // Y is top
        ret = y;
        y.parent = savedParent;
        y.left = x;
        y.right = z;
        
        // X is LChild of Y
        x.parent = y;
        x.left = t1;
        x.right = t2;
        
        // Z is RChild of Y
        z.parent = y;
        z.left = t3;
        z.right = t4;
        
        // Re-Adjust Heights
        // x
        if(t1 != null) 
            walk(t1);
        else if(t2 != null) 
            walk(t2);
        else 
            x.height = 0;
        // z
        if(t3 != null) 
            walk(t3);
        else if(t4 != null) 
            walk(t4);
        else 
            z.height = 0;
        
        walk(x);
        walk(z);
        walk(y); // May be redundant
        
        return ret;
    }
    
     private AVLNode<E> case3(AVLNode<E> x, AVLNode<E> y, AVLNode<E> z, AVLNode<E> savedParent){
         // Initiatlize VARS
        AVLNode<E> ret, t1, t2, t3, t4;
        
        t1 = y.left;
        t2 = z.left;
        t3 = z.right;
        t4 = x.right;
        
        // Z is TOP
        ret = z;
        z.parent = savedParent;
        z.left = y;
        z.right = x;
        
        // Y is LChild
        y.parent = z;
        y.left = t1;
        y.right = t2;
        
        // X is RChild
        x.parent = z;
        x.left = t3;
        x.right = t4;
        
        // Re-Adjust Heights
        // y
        if(t1 != null)
            walk(t1);
        else if(t2 != null)
            walk(t2);
        
        else // y is leaf
            y.height = 0;
        // x
        if(t3 != null) 
            walk(t3);
        else if(t4 != null)
            walk(t4);
        else
            x.height = 0;
        
        walk(y);
        walk(x);
        walk(z);
        
        
        return ret;
     }
    
    private AVLNode<E> case4(AVLNode<E> x, AVLNode<E> y, AVLNode<E> z, AVLNode<E> savedParent){
         // Initiatlize VARS
        AVLNode<E> ret, t1, t2, t3, t4;
        
        t1 = x.left;
        t2 = z.left;
        t3 = z.right;
        t4 = y.right;
        
        // Z is TOP
        ret = z;
        z.parent = savedParent;
        z.left = x;
        z.right = y;
        
        //X is LChild
        x.parent = z;
        x.left = t1;
        x.right = t2;
        
        // Y is RChild
        y.parent = z;
        y.left = t3;
        y.right = t4;
        
        // Height Re-Adjust
        // x
        if(t1 != null)
            walk(t1);
        else if(t2 != null)
            walk(t2);
        else 
            x.height = 0;
        // y
        if(t3 != null)
            walk(t3);
        else if(t4 != null)
            walk(t4);
        else 
            y.height = 0;
        
        walk(x);
        walk(y);
        walk(z);
               
        return ret;
    }
}

