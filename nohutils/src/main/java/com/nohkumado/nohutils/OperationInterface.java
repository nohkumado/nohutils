/* Id: OperationInterface.java,v 1.1.1.1 2002/08/27 08:18:00 bboett Exp  -*- java -*-
 * Copyright (C) 1997 Bruno Böttcher
 * bboett@adlp.org
 * http://bboett.free.fr/~bboett
 *
 * All rights reserved
 *
 * Permission to use, copy, modify and distribute this material for
 * any purpose and without fee is hereby granted, provided that the
 * above copyright notice and this permission notice appear in all
 * copies, and that name is not used in advertising or publicity 
 * pertaining to this material without specific, prior written 
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

package com.nohkumado.nohutils;
import  java.util.*;

/** 
  schnelle OperationInterface

  Objet qui décrit une opération précise

 CHANGELOG 

 */
public interface  OperationInterface
{
  /* CTOR
   */
  //public OperationInterface(String[] rawdata, List<String> tableHeader);
  /** toString

    retourne une forme en forme de chaines de caractères de cette liste
    l'argument optionnel donne l'ordre et les colonnes à sortir
   */
  String toString();
  /**

    applique

    applique l'operation et génère la ou les lignes de journal
   */
  List applique(ShellI shell, HashMap<String, Object> dynamicVars);
  String name();
} // end class OperationInterface 
