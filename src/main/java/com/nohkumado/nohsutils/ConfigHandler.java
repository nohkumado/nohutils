/**  *
 * COPYRIGHT and LICENCE
 *
 *  Copyright (c) 2004 Bruno Boettcher
 *
 *  ConfigHandler.java is free software; you can redistribute it and/or modify it under
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

import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.*;
import java.util.regex.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 *  * subclass thing to get the setting method This class is an entity that is
 * given a target and then loads or saves the settings accordingly
 *
 * @author nohkumado
 */
public class ConfigHandler implements Cloneable, ConfigHandlerInterface
{

  /**
   * the internationalization stuff
   */
  //ResourceBundle msg;

  protected Preferences settings = Preferences.userNodeForPackage(this.getClass());
  protected String type = "configHandler";
  protected String configName = "configHandler";

  /**
   *
   * new, Constructor
   *
   *
   * The constructor of a utils::configHandler object, initializes a number of
   * datastructures that will be used later, creates a blessed reference
   *
   */
  public ConfigHandler()
  {
    type = "abstract";
    //System.out.println("CTOR CH: default ");
    if (settings != null)
    {
      configName = settings.get("basename", "confighandler");
    }
  }// public void new·

  /**
   *
   * new, Constructor
   *
   *
   * The constructor of a utils::configHandler object, initializes a number of
   * datastructures that will be used later, creates a blessed reference
   *
   * @param base
   */
  public ConfigHandler(String base)
  {
    type = "abstract";
    configName = base;
    if (settings != null)
    {
      settings.put("basename", base);
    }
    //System.out.println("CTOR CH: default ");
  }// public void new·

  /**
   *
   * @param settings
   */
  public ConfigHandler(Preferences settings)
  {
    if (settings != null)
    {
      this.settings = settings;
      //System.out.println("CTOR CH: baseName: "+settings.get("baseName",null));
      configName = settings.get("basename", "confighandler");
    }// if (settings != null) 
    type = "abstract";
  }// public void new·

  /**
   *
   * init
   *
   * Here we really build up object
   *
   * @return
   */
  @Override
  public boolean init()
  {
    //System.out.println("init CH: baseName: "+settings.get("baseName",null));
    if (settings == null)
    {
      settings = Preferences.userNodeForPackage(this.getClass());
    }
//    if(msg == null)
//    {
//      Locale currentLocale = Locale.getDefault();
//      if(settings.get("lang",null) != null)
//      {
//	  String country = settings.get("country",null);
//	  String language = settings.get("lang",null);
//	  if(country == null) country = language.toUpperCase();
//	  else country = country.toUpperCase();
//	  currentLocale = new Locale(language, country);
//      }// if(settings.get("lang",null) != null)«me
//      try
//      {
//      msg = ResourceBundle.getBundle(configName,currentLocale);
//      }// try
//      catch(MissingResourceException e)
//      {
//	System.out.println("PROBLEM FINDING LANGUAGE RESSOURCES FOR "+configName);
//      try
//      {
//        msg = ResourceBundle.getBundle(configName);
//	System.out.println("LOADED DEFAULT");
//      }// try
//      catch(MissingResourceException f)
//      {
//	System.out.println("NOT EVEN LOADED DEFAULT....");
//      }// catch(MissingResourceException e)
//      msg = ResourceBundle.getBundle(configName);
//	System.out.println("LOADED DEFAULT");
//      }// catch(MissingResourceException e)
//    }// if(msg == null)

    //loadSettings(configName+".cfg");
    //TODO new  sys
    //String[] fileCandidates = {
    //  System.getProperty("user.home")+"/."+configName+"rc",
    //  "./."+configName+"rc",
    //  "./"+configName+".cfg",
    //};
    //for(int i = 0; i < fileCandidates.length; i++)
    //{
    //  //String configName  = System.getProperty("user.home")+"/.fiburc";
    //  String configName  = fileCandidates[i];
    //  File configFile = new File(configName);
    //  try
    //  {
    //    if(configFile.exists()) loadSettings(configName);
    //  }// try
    //  catch(FileNotFoundException e){}
    //  //configFile = new File("fibu.cfg");
    //  //if(configFile.exists()) loadSettings("fibu.cfg");
    //}// for(int i = 0; i < fileCandidates.length(); i++)
    return (true);
  }// public void init·

  /**
   *
   * loadSettings
   *
   * load form the conf file the different bot settings means using the setting
   * configpath all files ending with conf (and as soon as i figured out how to
   * setup the same stuff in XML, also ending with +xml) the config directory is
   * scanned for conf files and read in and added to the setings of the bot+    *
   * TODO, leave the non loaded actors settings away+++ TODO hmmm with the
   * migratin to the prefer"ences ssystem, i think e should drop this...
   *
   * @param fileName
   * @return
   * @throws java.io.FileNotFoundException
   */
  public String loadSettings(String fileName) throws FileNotFoundException
  {
    String unparsed = "";
    Pattern commentPattern = Pattern.compile("^\\S*#");
    Pattern pattern = Pattern.compile("^(\\S+)\\S*=\\S*(\\S+)");
    File confFile = new File(fileName);
    if (!confFile.exists())
    {
      return (unparsed);
    }
    Scanner scanner = new Scanner(confFile);
    String theLine;// = "";
    while (scanner.hasNextLine())
    {
      theLine = scanner.nextLine().trim();
      if (theLine.matches("^#"))
      {
      }
      else if (theLine.matches(".*?=.*"))
      {
        Matcher matcher = pattern.matcher(theLine);
        if (matcher.find())
        {
          String varName = matcher.group(1);
          String varValue = matcher.group(2);
          settings.put(varName, varValue);
        }// if(matcher.find())
      }// if(theLine.matches(".*?=.*"))
      else
      {
        unparsed += theLine + "\n";
      }//else if(theLine.matches(".*?=.*"))
    }// while(scanner.hasNextLine())
    return (unparsed);
  }// public void loadSettings 

  /**
   *
   * dumpSettings    *
   * issue the actual settings of the bot
   *
   * @param hash
   */
  @Override
  public String dumpSettings(Preferences hash)
  {
    String listing = "";
    if (hash == null)
    {
      hash = settings;
    }
    //TODO put the hash into a treemap or something sorted

    try
    {
      for (String i : hash.keys())
      {
        Object argl = hash.get(i, null);
        listing += i + " " + argl + "\n";
        //listing += "<i>\n"+.dumpSettings(hash.get(i);)+"\n</i>\n" if(\argl instanceof HASH);
      }// 
    }// try
    catch (java.util.prefs.BackingStoreException e)
    {
      listing += "\nERROR:" + e;
    }// catch(java.util.prefs.BackingStoreException e) 
    return listing;
  }

  /**
   *
   * saveSettings    *
   * save actual bot settings to the conf file TODO with the prefs migration
   * useless (to be checked)
   *
   * @param hash
   */
  public void saveSettings(HashMap<String, Object> hash)
  {
  }// public void saveSettings 

  /**
   *
   */
  public void saveSettings()
  {
    saveSettings(null);
  }// public void saveSettings 

  /**
   *
   * setValidKeys    *
   * getter/setter for the hash of keys that should be accepted through shell
   * arguments, if isn't present any argument will be accepted+
   *
   * @param hash
   */
  @Override
  public void setValidKeys(HashMap<String, Object> hash)
  {
    //if(hash && ref(hashref) eq 'HASH')
    //{
    //    set("allowedkeys",hashref);
    //}// if(hashref)
    //else
    //{
    //    //print("returning available allowed keys:"+Dumper(get("allowedkeys");)+"\n");
    //    return get("allowedkeys");;
    //}// else
  }// public void setValidKeys 

  /**
   *
   * parseArgs    *
   * parse the incoming arguments, if allowedkeys hash is present, it is used to
   * translate the keys to the sttings used in the program, otherwise the
   * keyname is used as parameter name+ Also if the allowedkeys hash is present
   * only the keys psecifiyed in there are allowed+    *
   * using the hash implies: the keys are still with the prepended '-''s, if
   * after the '-'s a no is detected it is thought as a negation setting if
   * there isn't a valid value following+
   *
   * TODO migrate to use of jopt
   *
   * @param argv
   * @return
   */
  public Preferences parseArgs(String argv[], ShellI mU)
  {
    Options options = new Options();

// add t optio
    options.addOption(Option.builder("l")
            .required(false)
            .longOpt("language")
            .desc(mU.msg("the_language_to_use"))
            .hasArg()
            .type(String.class)
            .argName("LANG")
            .build());
    options.addOption(Option.builder("c")
            .required(false)
            .longOpt("country")
            .desc(mU.msg("the_country_to_use"))
            .hasArg()
            .type(String.class)
            .argName("COUNTRY")
            .build());

    options.addOption(Option.builder("f")
            .required(false)
            .longOpt("file")
            .desc(mU.msg("a_character_file"))
            .hasArg()
            .type(String.class)
            .argName("FILE")
            .build());
    options.addOption(Option.builder("v")
            .required(false)
            .longOpt("verbose")
            .desc(mU.msg("verbose"))
            .build());

    options.addOption(Option.builder("h")
            .required(false)
            .longOpt("help")
            .desc(mU.msg("help"))
            .build());

    CommandLineParser parser = new DefaultParser();
    CommandLine cmd;
    try
    {
      cmd = parser.parse(options, argv);
    } 
    catch (org.apache.commons.cli.ParseException ex)
    {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("Quelkar", options);
      return settings;
    }
    
    
    if (cmd.hasOption("language"))
    {
      settings.put("lang", cmd.getOptionValue("language"));
    }
    if (cmd.hasOption("country"))
    {
      settings.put("country", cmd.getOptionValue("country"));
    }
     if (cmd.hasOption("file"))
    {
      settings.put("file", cmd.getOptionValue("file"));
    }
      if (cmd.hasOption("help"))
    {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("Quelkar", options);
    }
    
    return (settings);
  }//protected void parseArgs(String argv[])

  @Override
  public void parseArgs(HashMap<String, Object> arguments)
  {
    //TODO tranlsate from perl
    //while(scalar(arguments) >0)
    //{
    //  my key = shift(arguments);
    //  key = lc(key);
    //  my negate = FALSE;
    //  if(key =~ /(-+)no(+*)/)
    //  {
    //    key = 1+2;
    //    negate = TRUE;
    //  }//
    //  
    //  if(allowedkeys != null)
    //  {
    //    if(allowedkeys.get(key) != null || allowedkeys.get("no"+key) != null)
    //    {
    //      Object value = fetchNext(arguments,negate);
    //  if(negate && value ne TRUE)
    //  {
    //    key = "no"+key;
    //  }//
    //  settings.set(allowedkeys.get(key), value);
    //    }//
    //    else
    //    {
    //  print("invalid argument submitted, allowed are "+allowedkeys+"\n");
    //    }// else
    //  }// if(get("allowedkeys");)
    //  else
    //  {
    //    key =~ s/^-+//;
    //  value = fetchNext(arguments,negate);
    //  if(negate && value ne TRUE)
    //  {
    //    key = "no"+key;
    //  }//
    //  settings.get(key) = value;
    //  }// else
    //}// while
  }//public void parseArgs

  /**
   *
   * fetchNext    *
   * check out the next argument, if it isn't prepended with - it should be a
   * value that we return, otherwise we push back the thing and return the
   * logical attribute+
   *
   */
  @Override
  public void fetchNext(HashMap<String, Object> arguments, boolean bool)
  {

    //my value = shift(@arguments);
    //if(value =~ /^-/)
    //{
    //  unshift(@arguments,value);
    //  return bool;
    //}// if(key =~ /(-+)no(+*)/)
    //return value;
  }//public void fetchNext

  /**
   *
   * est
   *
   * issue the type of object
   *
   */
  @Override
  public String est()
  {
    return type;
  }//public void est

  /**
   *
   * container TODO this should be changed.... when chnaging E;g the name of the
   * conf file this should be inbtercepted, so the innards of the settings
   * mechanism should be hidden....
   *
   * getter/setter for the hash of settings we are manipulating+
   *
   * @param hash
   * @return
   */
  @Override
  public Preferences container(Preferences hash)
  {
    settings = hash;
    return (settings);
  }// public HashMap<String,Object> container(HashMap<String, Object>hash)

  /**
   *
   * container    *
   * getter/setter for the hash of settings we are manipulating+
   *
   * @return
   */
  @Override
  public Preferences container()
  {
    return (settings);
  }// public HashMap<String,Object> container()
}//public class ConfigHandler implements Cloneable
