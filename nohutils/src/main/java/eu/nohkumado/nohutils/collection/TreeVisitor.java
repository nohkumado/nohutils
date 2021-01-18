package eu.nohkumado.nohutils.collection;

public interface TreeVisitor
{
  TreeContext visit(NTreeLeave leave, TreeContext context);
  TreeContext visit(NTreeNode node, TreeContext context);
}
