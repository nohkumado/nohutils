package eu.nohkumado.nohutils.test;

import eu.nohkumado.nohutils.collection.NTreeLeave;
import eu.nohkumado.nohutils.collection.NTreeNode;

public class NTreeNodeTest extends UtilsTester
{

	@Override
	public boolean test() throws NullPointerException
	{
		boolean result = true;
		error = new StringBuilder();
		NTreeNode<String> root = new NTreeNode<String>().name("root");
		root.dump(false);
		NTreeLeave<String> leave = new NTreeLeave<>();
		
		leave.set("toto");
		result &= doTrans(!"toto".equals(leave.get("").getContent()),"set/get failed expected 'toto' got '"+leave.get("")+"'");
		result &= doTrans(!"(toto)".equals(leave.toString("  ").trim()),"toString failed expected '(toto)' got '"+leave.toString("  ").trim()+"'");
		
		String[] names = new String[] {
			"one", "two","three"
		};
		String[] subname =  new String[]{
			"l1","l2","l3","l4","l5","l6"
		}; 
		int n = 0;
		for (String name : names) {
			NTreeNode<String> aNode = new NTreeNode<String>().name(name);
			//log.append("created node ").append(aNode.toString()).append("\n");
			for (int x = 0; n < subname.length && x < 2; n++, x++) {
				NTreeLeave<String> aLeave = new NTreeLeave<String>().set(subname[n]); //create and set content
				aNode.set(aLeave, subname[n]);
				//log.append(subname[n]).append(" ").append(aLeave).append(" added to ").append(aNode).append("\n");
			}
			root.set(aNode, name);
		}//for(int i = 0; i < names.length; i++)
		//log.append("filled root ").append(root).append("\n");
		String target = "root[one[(l1),(l2)],two[(l3),(l4)],three[(l5),(l6)]]";
		String back = root.toString();
		back = back.replaceAll(" ","_");
		result &= doTrans(!target.equals(back),"Node toString failed, expected \n'"+target+"' got \n'"+back+"'");

		result &= doTrans(root.get("three/l6") == null ,"get failed "+root.get("three/l6"));
		if(result) result &= doTrans(!"(l6)".equals(root.get("three/l6").toString(" ").trim()),"get failed expected '(l6)' got "+root.get("three/l6").toString(" "));
		NTreeLeave<String> removed = (NTreeLeave<String>)root.remove("three/l6");
		result &= doTrans(!"(l6)".equals(removed.toString().trim()),"Node toString failed expected (l6) got "+removed);
		target = "root[one[(l1),(l2)],two[(l3),(l4)],three[(l5)]]";
		result &= doTrans(!target.equals(root.toString().trim()),"Node removal failed, expected '"+target+"' got "+root.toString("  ").trim());
		
		return result;
	}//public boolean test()
}//public class NTreeNodeTest extends UtilsTester
