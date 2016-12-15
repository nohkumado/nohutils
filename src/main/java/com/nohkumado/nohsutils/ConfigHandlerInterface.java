/** Id: ConfigHandlerInterface.java,v 1.4 2005/09/30 16:24:48 bboett Exp  -*- java -*-
 *
 * NAME ConfigHandlerInterface 
 *
 * AUTHOR Bruno Böttcher <bboett at adlp.org> 
 *
 * SEE ALSO no docu at the moment 
 *
 * SYNOPSIS
 *
 * subclass  thing to get the setting method 
 *
 * DESCRIPTION 
 * 
 * This class is an entity that is given a target and then loads or saves the settings
 * accordingly
 * 
 * COPYRIGHT and LICENCE
 * 
 *  Copyright (c) 2004 Bruno Boettcher
 * 
 *  ConfigHandlerInterface.java is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; version 2 of the License.
 *
 *  This program is distributed in the hope that it will be importful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
    */

package com.nohkumado.nohsutils;

import java.util.*;
import java.util.prefs.*;

/**
 *
 * @author nohkumado
 */
public interface ConfigHandlerInterface extends Cloneable
{
    /**

      new, Constructor


      The constructor of a utils::configHandler object, initializes a number of datastructures that will be used later, creates a blessed reference

     * @param settings 
    */
    //public ConfigHandlerInterface(HashMap<String,Object> settings);
    /**

      init

      Here we really build up  object
   * @return
    */
    public boolean init();
    /**
      loadSettings

      load form the conf file the different bot settings
      means using the setting configpath all files ending with conf (and as soon as i figured out how to setup the same stuff in XML, also ending with +xml) the config directory is scanned for conf files and read in and added to the setings of the bot+ 

      TODO, leave the non loaded actors settings away+++

     * @param basepath 
     */
    //public String loadSettings(String filename) throws FileNotFoundException;
    /**

      dumpSettings 

      issue the actual settings of the bot
   * @param hash 
     * @return 
    */
    public String dumpSettings(Preferences hash);
    /**
      saveSettings 

      save actual bot settings to the conf file

     * @param Object>hash 
    */
    //public void saveSettings(Preferences hash);
    /** 
     * 
     */
    //public void saveSettings();
    /**

      setValidKeys 

      getter/setter for the hash of keys that should be accepted through shell
      arguments, if  isn't present any argument will be accepted+
   * @param hash 
    */
    public void setValidKeys(HashMap<String, Object>hash);
    /**

      container 

      getter/setter for the hash of settings we are manipulating+
   * @param hash 
     * @return 
    */
    public Preferences container(Preferences hash);
    /**

      container 

      getter/setter for the hash of settings we are manipulating+

     * @return 
    */
    public Preferences container();
    /**

      parseArgs 

      parse the incoming arguments, if allowedkeys hash is present, it is used to
      translate the keys to the sttings used in the program, otherwise the keyname is
      used as parameter name+ Also if the allowedkeys hash is present only the keys
      psecifiyed in there are allowed+ 

      using the hash implies: the keys are still with the prepended '-''s, if after
      the '-'s a no is detected it is thought as a negation setting if there isn't a
      valid value following+

     * @param arguments HashMap<String,Object>arguments 
    */
    public void parseArgs(HashMap<String,Object>arguments);
    public Preferences parseArgs(String[] arguments, ShellI mU);
    /**

      fetchNext 

      check out the next argument, if it isn't prepended with - it should be a value
      that we return, otherwise we push back the thing and return the logical
      attribute

     * @param arguments a hashmpp of arguments
     * @param bool 
    */
    public void fetchNext(HashMap<String,Object> arguments,boolean bool);
    /**

      est

      issue the type of  object

     * @return the type
    */
    public String est();

  
}//public class ConfigHandlerInterface implements Cloneable
