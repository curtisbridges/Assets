package com.curtisbridges.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

public class GenericTreeNode<T> implements TreeNode {
    private GenericTreeNode<T> parent;
    private T value;
    private List<GenericTreeNode<T>> children;
    
    public GenericTreeNode(T t) {
        value = t;
        children = new ArrayList<GenericTreeNode<T>>();
    }
    
    public T getValue() {
        return value;
    }
    
    @Override
    public GenericTreeNode<T> getChildAt(int childIndex) {
        return children.get(childIndex);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public GenericTreeNode<T> getParent() {
        return parent;
    }

    @Override
    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return children.size() == 0;
    }

    @Override
    public Enumeration<GenericTreeNode<T>> children() {
        return Collections.enumeration(children);
    }
}
