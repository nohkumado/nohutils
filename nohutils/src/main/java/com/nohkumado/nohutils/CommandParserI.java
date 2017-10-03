/*
 * NAME CommandParserI 
 *
 * AUTHOR Bruno Boettcher <bboett at adlp+org> 
 *
 * SEE ALSO no docu at the moment 
 *
 * DESCRIPTION 
 * takes a line of text and tries to split it by hash value
 *
 * COPYRIGHT and LICENCE
 *
 *  Copyright (c) 2004 Bruno Boettcher
 *
 *  CommandParserI.java is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; version 2
 *  of the License+
 *
 *  This program is distributed in the hope that it will be importful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE+  See the
 *  GNU General Public License for more details+

 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc+, 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA+
*/
package com.nohkumado.nohutils;
import java.util.*;
@SuppressWarnings({"EmptyMethod", "UnusedParameters"})
public  interface CommandParserI //extends com.nohkumado.jtrader.EventGeneratorI
{

	void parseMode(String p0);


	void clearCmds();

  /** -------------------------- init --------------------------
   *
   */
  HashMap<String,CommandI>  feedCmds(HashMap<String, CommandI> cmds);
  /** -------------------------- setRessource --------------------------
   *
   */
  void  setRessource(Properties arg0);
  /** -------------------------- formatProperties --------------------------
   *
   */
  String  formatProperties(Properties arg0);
  /** -------------------------- parse --------------------------
   *
   */
  ArrayList<CommandI> parse(String line);
   /** 
    * find a command, means try key completion if not found directly clone the command and return it
    * 
    * @param token name of cmd
    * @return found cmd
    */
   CommandI findCmd(String token);
  /** -------------------------- getIntString --------------------------
   * setter/getter a reference on the actual shell
   */
  ShellI shell();
  void shell(ShellI s);
  /** -------------------------- getHelp --------------------------
   * compile the Help from the commands
   */
  String help();
}
