package eu.nohkumado.nohutils.test;

import eu.nohkumado.nohutils.ReturnValue;

/**
 * Created by bboett on 14.12.17.
 * A test for the tester class
 */

public class UtilsTesterTest extends UtilsTester {
  protected static String testName = "UtilsTester";


  public UtilsTesterTest() {

  }//CTOR


  @Override
  public boolean test() {
    if (doTrans(true, "failed bool")) return false;
    ReturnValue<String> val = new ReturnValue<>();
    val.croak("failed retval");
    if (!doTrans(val)) return false;
    if (!assertTrue(true, "failed asserttrue")) ;
    if (!assertFalse(false, "failed assert false")) ;
    if (!assertTrue(true)) {
      error("failed assert true without msg");
      return false;
    }
    if (assertFalse(true)) {
      error("failed assert false without msg");
      return false;
    }
    if (assertNull("toto", "failed null")) return false;
    if (assertNotNull(null, "failed not null")) return false;
    if (assertNull("toto")) {
      error("failed assert Null without msg");
      return false;
    }
    if (assertNotNull(null)) {
      error("failed assert NotNull without msg");
      return false;
    }
    if (assertEquals("titi", "toto")) {
      error("failed assert obj equals without msg");
      return false;
    }
    if (assertNotEquals("toto", "toto")) {
      error("failed assert NotNull without msg");
      return false;
    }
    if (!assertArrayEquals(new String[]{
        "toto", "titi"
    }, new String[]{
        "toto", "titi"
    })) {
      error("failed assert ArrayEquals without msg");
      return false;
    }

    return true;
  }//public boolean doTrans(boolean result, StringBuilder msg)
}
