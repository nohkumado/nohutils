package eu.nohkumado.nohutils.collection;
/* Copyright (C) 2016 Bruno Böttcher
 * nohkumado@gmail.com
 * https://sites.google.com/site/nokumado/
 *
 * All rights reserved
 *
 * Permission to use, copy, modify and distribute this material for
 * any purpose and without fee is hereby granted, provided that the
 * above copyright notice and this permission notice appear in all
 * copies, and that my name is not used in advertising or publicity
 * pertaining to this material without my specific, prior written
 * permission
 *
 * I MAKE NO REPRESENTATIONS AND EXTENDS NO WARRANTIES,
 * EXPRESS OR IMPLIED, WITH RESPECT TO THE SOFTWARE, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR ANY PARTICULAR PURPOSE, AND THE WARRANTY AGAINST
 * INFRINGEMENT OF PATENTS OR OTHER INTELLECTUAL PROPERTY RIGHTS.  THE
 * SOFTWARE IS PROVIDED "AS IS", AND IN NO EVENT SHALL I  BE LIABLE FOR
 * ANY DAMAGES, INCLUDING ANY LOST PROFITS OR OTHER INCIDENTAL OR
 * CONSEQUENTIAL DAMAGES RELATING  TO THE SOFTWARE.
 */


import java.util.ArrayList;

/**
 * Node of a n-dimensional tree
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal", "UnusedReturnValue", "ConstantConditions"})
public class BTreeAtom {
    protected BTreeAtomFactory factory;

    protected String name;
    protected BTreeAtom parent, left, right;
    public static final String TAG = "BTA";

    protected BTreeAtom(String name, BTreeAtomFactory f) {
        this.name = name.trim();
        factory = f;
    }

    /**
     * remove a subtree
     */
    public BTreeAtom remove_left() {
        BTreeAtom tmp = left;
        tmp.parent = null;
        left = null;
        return tmp;
    }//public BTreeAtom remove_left()

    public BTreeAtom remove_right() {
        BTreeAtom tmp = right;
        tmp.parent = null;
        right = null;
        return tmp;
    }//public BTreeAtom remove_left()

    /**
     * get an atom, use a / separated path to navigate in the tree
     */
    public BTreeAtom left() {
        return left;
    }

    public BTreeAtom right() {
        return right;
    }

    public BTreeAtom left(BTreeAtom tmp) {
        if(tmp != null)
        {
            tmp.parent = this;
            left = tmp;
        }
        return left;
    }

    public BTreeAtom right(BTreeAtom tmp) {
        tmp.parent = this;
        right = tmp;
        return right;
    }

    /**
     * return true if this is a leave
     */
    public boolean isNode() {
        return parent == null || (left != null || right != null);
    }//protected boolean isLeaf()

    /**
     * create a string representation of this tree
     * with a child separator
     */
    public String toString(String indent, String childSep) {
        StringBuilder result = new StringBuilder();
        String subindent;
        if (indent.length() > 0)
            subindent = indent + " ";
        else subindent = " ";
        result.append(name);
        if (childSep != null && childSep.length() > 0 && isNode()) result.append(" (");
        if (left != null) result.append(subindent).append(left.toString(indent));
        if (right != null) result.append(subindent).append(right.toString(indent));
        if (childSep != null && childSep.length() > 0 && isNode()) result.append(")");
        //result.append("\n");
        return result.toString();
    }

    /**
     * create a string representation of this tree
     */
    public String toString(String indent) {
        return toString(indent, "");
    }

    @Override
    public String toString() {
        return toString("");
    }//public String toString(String indent)

    /**
     * -------------------------------------------------------------------
     * make a recursive one on one copy of this structure
     * -------------------------------------------------------------------
     */
  /*public BTreeAtom clone(BTreeAtomFactory factory)
	 {

	 BTreeAtom cloned = factory.instantiate(name);

	 if (left != null) cloned.left(left.clone(factory)); 
	 if (right != null) cloned.right(right.clone(factory)); 
	 return cloned;
	 }// public java.lang.Object clone()

	 */
    public void copy(BTreeAtom root) {
        name = root.name;
        if (root.left != null) {
            left(factory.instantiate(root.left));
        }

        if (root.right != null) {
            right(factory.instantiate(root.right));
        }
    }//public void copy(GeneticAtom root)

    /**
     * -----------------------------------------------------------------
     * override the equals method of those damn thing, seems the default is
     * broken...
     * ------------------------------------------------------------------
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof BTreeAtom)) return false;
        BTreeAtom cmpTo = (BTreeAtom) obj;
        if (!name.equals(cmpTo.name)) return false;
        boolean toRet = true;
        if (left == null && cmpTo.left == null &&
                right == null && cmpTo.right == null) return toRet; //is leaf
        if (left != null && cmpTo.left != null) toRet &= left.equals(cmpTo.left);
        else toRet = false;
        if (right != null && cmpTo.right != null) toRet &= right.equals(cmpTo.right);
        else toRet = false;

        return toRet;
    }// public boolean equals(Object obj)

    public String name() {
        return name;
    }//public String name()

    public String name(String n) {
        name = n;
        return name;
    }//public String name()

    public ArrayList<BTreeAtom> walk() {
        return walk(null);
    }

    public ArrayList<BTreeAtom> walk(ArrayList<BTreeAtom> flatList) {
        if (flatList == null) flatList = new ArrayList<>();
        //if(parent == null) Log.e(TAG,"walk no parent for "+this);
        flatList.add(this);
        if (left != null) left.walk(flatList);
        if (right != null) right.walk(flatList);
        return flatList;
    }//public ArrayList<BTreeAtom> walk(ArrayList<BTreeAtom> flatList)


}//class
