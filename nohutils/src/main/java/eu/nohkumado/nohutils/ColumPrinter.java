package eu.nohkumado.nohutils;

import eu.nohkumado.nohutils.foreign.MultiColumnPrinter;

/**
 Example usage:
 * <PRE>
 *  MyPrinter  mp = new MyPrinter(3, 2, "-");
 *  String    oneRow[] = new String [ 3 ];
 *  oneRow[0] = "User Name";
 *  oneRow[1] = "Email Address";
 *  oneRow[2] = "Phone Number";
 *  mp.addTitle(oneRow);
 *
 *  oneRow[0] = "Bob";
 *  oneRow[1] = "bob@foo.com";
 *  oneRow[2] = "123-4567";
 *  mp.add(oneRow);
 *
 *  oneRow[0] = "John";
 *  oneRow[1] = "john@foo.com";
 *  oneRow[2] = "456-7890";
 *  mp.add(oneRow);
 *  mp.print();
 * </PRE>
 *
 * <P>
 * The above would print:
 * <P>
 * <PRE>
 *  --------------------------------------
 *  User Name  Email Address  Phone Number
 *  --------------------------------------
 *  Bob        bob@foo.com    123-4567
 *  John       john@foo.com   456-7890
 * </PRE>
 *
*/
@SuppressWarnings({"SameParameterValue", "WeakerAccess"})
public class ColumPrinter extends MultiColumnPrinter
{
  protected StringBuilder sb = new StringBuilder();
	/**
	 * Creates a new MultiColumnPrinter class.
	 *  @param numCol number of columns
	 * @param gap gap between each column
	 * @param border character used to frame the titles
	 * @param align type of alignment within columns
	 */
	public ColumPrinter(int numCol, int gap, String border,
											int align)
	{
		super(numCol, gap, border, align, MultiColumnPrinter.DEFAULT_SORT);
	}

	/**
	 * Creates a new sorted MultiColumnPrinter class.
	 *  @param numCol number of columns
	 * @param gap gap between each column
	 * @param border character used to frame the titles
	 */
	public ColumPrinter(int numCol, int gap, String border)
	{
		this(numCol, gap, border, MultiColumnPrinter.LEFT);
	}

	/**
	 * Creates a sorted new MultiColumnPrinter class using LEFT alignment.
	 * @param numCol number of columns
	 *
	 */
	public ColumPrinter(int numCol)
	{
		this(numCol, 2, "-");
	}

	/**
	 * Creates a sorted new MultiColumnPrinter class using LEFT alignment
	 * and with no title border.
	 *
	 * @param numCol number of columns
	 * @param gap gap between each column
	 */
	public ColumPrinter(int numCol, int gap)
	{
		this(numCol, gap, null);
	}

	@Override
	public void doPrint(String str)
	{
		sb.append(str);
	}

	@Override
	public void doPrintln(String str)
	{
		sb.append(str).append("\n");
	}

	@Override
	public void clear()
	{
		super.clear();
		sb = new StringBuilder();
	}

	@Override
	public String toString()
	{
		return sb.toString();
	}

}
