package eu.nohkumado.nohutils.collection;

public class TreeVisitorImpl implements TreeVisitor
{
  @Override
  public TreeContext visit(NTreeLeave leave, TreeContext context)
  {
    if(context instanceof  TreeContextImpl)
      ((TreeContextImpl)context).add(leave.name());
    return context;
  }

  @Override
  public TreeContext visit(NTreeNode node, TreeContext context)
  {
    ((TreeContextImpl)context).add(node.name());
    return context;
  }
}
