package cn.myframe.demo;

import com.alibaba.fastjson.JSON;

import java.util.*;

/**
 * @Author: ynz
 * @Date: 2019/11/29/029 14:00
 * @Version 1.0
 */
public class TreeDemo {
}

class TreeNode{
    int val;
    TreeNode left;
    TreeNode right;

    public List<List<Integer>> levelOrderBottom(TreeNode root) {
        List<List<Integer>> lists = new LinkedList<>();
        if (root == null) return lists;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            List<Integer> list = new ArrayList<>();
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (node.left != null) queue.add(node.left);
                if (node.right != null) queue.add(node.right);
                list.add(node.val);
            }
            lists.add(0, list);
        }
        return lists;
    }

    public List<List<Integer>> levelOrderBottom2(TreeNode root) {
        List<List<Integer>> list = new LinkedList<>();
        if(root == null){
            return list;
        }
        List<TreeNode> treeNodeList = new ArrayList<>();
        treeNodeList.add(root);
        level(treeNodeList,list);
        return list;

    }

    public void level(List<TreeNode> levelNode,List<List<Integer>> list){
        List<Integer> integers = new ArrayList<>();
        List<TreeNode> treeNodeList = new ArrayList<>();
        for(TreeNode treeNode : levelNode){
            if(treeNode ==  null){
                continue;
            }
           integers.add(treeNode.val);
            if(treeNode.left!=null){
                treeNodeList.add(treeNode.left);
            }
            if(treeNode.right!=null){
                treeNodeList.add(treeNode.right);
            }
        }
        list.add(0,integers);
        if(treeNodeList.size()>0){
            level(treeNodeList,list);
        }
    }



    public TreeNode(int value){
        val = value;
    }

    public int maxDepth(TreeNode root) {
        if(root == null){
            return 0;
        }
       return Math.max(maxDepth(root.left),maxDepth(root.right))+1;
    }

    public boolean isSameTree(TreeNode p, TreeNode q) {
        if(p == null && q == null){
            return true;
        }
        if(p == null || q == null || p.val != q.val){
            return false;
        }
        return isSameTree(p.left,q.right)&&isSameTree(p.right,q.left);
    }

    public boolean isSymmetric(TreeNode root) {
        if(root ==  null){
            return true;
        }
        return isSameTree(root.right, root.left);
    }

    public TreeNode createTree(){
        Integer[] nodeArr = new Integer[]{7,4,5,null,null,1,5};
        List<TreeNode> nodeList = new ArrayList<>();
        for(Integer integer : nodeArr){
            if(integer != null){
                nodeList.add(new TreeNode(integer));
            }else{
                nodeList.add(null);
            }
        }
        return null;

    }



    public static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<TreeNode>();

        stack.push(root);
        while (true) {
            root =  stack.pop();
            if(root == null){
                break;
            }
            if (root.left != null) {
                stack.push(root);
                stack.push(root.left);
                root.left = null;
            } else {
                list.add(root.val);
                if (root.right != null) {
                    stack.push(root.right);
                }
            }
            if (stack.isEmpty()){
                break;
            }

        }
        return list;
    }



    public static void main(String[] args) {
        TreeNode treeNode = new TreeNode(1);
        TreeNode treeNode1 = new TreeNode(2);
        TreeNode treeNode2 = new TreeNode(3);

    }
}