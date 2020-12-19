package com.nohkumado.nohutils.test;
import com.nohkumado.nohutils.collection.*;

public class NTreeNodeTest extends UtilsTester
{

	@Override
	public boolean test() throws NullPointerException
	{
		boolean result = true;
		error = new StringBuilder();
		NTreeNode<String> root = new NTreeNode<>();
		NTreeLeave<String> leave = new NTreeLeave<>();
		
		leave.set("toto");
		result &= doTrans(!"toto".equals(leave.get("").getContent()),"set/get failed expected 'toto' got '"+leave.get("")+"'");
		result &= doTrans(!"(toto)".equals(leave.toString("  ").trim()),"toString failed expected '(toto)' got '"+leave.toString("  ").trim()+"'");
		
		String[] names = new String[] {
			"one", "two","three"
		};
		String[] lname =  new String[]{
			"l1","l2","l3","l4","l5","l6"
		}; 
		int n = 0;
		for (String name : names) {
			NTreeNode<String> aNode = new NTreeNode<>();
			//log.append("created node ").append(aNode.toString()).append("\n");
			for (int x = 0; n < lname.length && x < 2; n++, x++) {
				NTreeLeave<String> aLeave = new NTreeLeave<String>().set(lname[n]);
				aNode.set(aLeave, lname[n]);
				//log.append(lname[n]).append(" ").append(aLeave).append(" added to ").append(aNode).append("\n");
			}
			root.set(aNode, name);
		}//for(int i = 0; i < names.length; i++)
		//log.append("filled root ").append(root).append("\n");
		String rootAsS = "[one-[l1-(l1)|l2-(l2)]|two-[l3-(l3)|l4-(l4)]|three-[l5-(l5)|l6-(l6)]]";
		result &= doTrans(!rootAsS.equals(root.toString("  ").trim()),"Node toString failed, expected '"+rootAsS+"' got "+root.toString("  ").trim());

		result &= doTrans(root.get("three/l6") == null ,"get failed "+root.get("three/l6"));
		if(result) result &= doTrans(!"(l6)".equals(root.get("three/l6").toString(" ").trim()),"get failed expected '(l6)' got "+root.get("three/l6").toString(" "));
		NTreeLeave<String> removed = (NTreeLeave<String>)root.remove("three/l6");
		result &= doTrans(!"(l6)".equals(removed.toString().trim()),"Node toString failed expected (l6) got "+removed);
		rootAsS = "[one-[l1-(l1)|l2-(l2)]|two-[l3-(l3)|l4-(l4)]|three-[l5-(l5)]]";
		result &= doTrans(!rootAsS.equals(root.toString().trim()),"Node removal failed, expected '"+rootAsS+"' got "+root.toString("  ").trim());
		
		return result;
	}//public boolean test()
}//public class NTreeNodeTest extends UtilsTester
