package hello

import scala.util.parsing.combinator._
import scala.util.parsing.combinator.RegexParsers
import scala.util.parsing.combinator.syntactical._

abstract class MTree
case class Start(exp: MTree) extends MTree
case class Exp(term: MTree, exp2: MTree) extends MTree
case class Exp2(or: String, exp: MTree) extends MTree
case class Term(factor: MTree, term: MTree) extends MTree
case class Factor(a: MTree, factor2: MTree) extends MTree
case class Factor2(optional: String, factor2: MTree) extends MTree
case class A(char: MTree, parenth: String, a2: MTree) extends MTree
case class A2(exp: MTree, char: String) extends MTree
case class Char(s: String) extends MTree
case class NIL() extends MTree

class MPParser extends JavaTokenParsers
{
  def start: Parser[MTree] = exp ^^ {case start => Start(start)}
  def exp: Parser[MTree] = term~exp2 ^^ {case term~exp2 => Exp(term,exp2)} | term ^^ {case term => Exp(term,NIL())}
  def exp2: Parser[MTree] = "|"~exp ^^ {case or~exp3 => Exp2(or,exp3)}
  def term: Parser[MTree] = factor~term ^^ {case factor~term2 => Term(factor,term2)} | factor ^^ {case factor => Term(factor,NIL())}
  def factor: Parser[MTree] = a~factor2 ^^ {case a~factor2 => Factor(a,factor2)} | a ^^ {case a => Factor(a,NIL())}
  def factor2: Parser[MTree] = "?"~factor2 ^^ {case opt~factor2 => Factor2(opt,factor2)} | "?" ^^ {case opt => Factor2(opt,NIL())}
  def a: Parser[MTree] = char ^^ {case char => A(char,"", NIL())} | "("~a2 ^^ {case char~a2 => A(NIL(),char,a2)}
  def a2: Parser[MTree] = exp~")" ^^ {case exp~closeParen => A2(exp,closeParen)}
  def char[Char] = ("a"|"b"|"c"|"d"|"e"|"f"|"g"|"h"|"i"|"j"|"k"|"l"|"m"|"n"|"o"|"p"|"q"|"r"|"s"|"t"|"u"|"v"|"w"|"x"|"y"|"z"          
                   |"0"|"1"|"2"|"3"|"4"|"5"|"6"|"7"|"8"|"9"|".") ^^ {case char => Char(char)}               
}

object MicroProject3 extends MPParser
{
   def check(tree1:MTree, tree2:MTree): Boolean = 
   {
     tree1 match
     {
       case Start(exp) =>
         {
           tree2 match
           {
             case Start(exp) => {check(tree1.asInstanceOf[Start].exp,tree2.asInstanceOf[Start].exp)}
           }
         }
       case Exp(term, Exp2(or,exp)) => 
         {
           tree2 match 
           {
             case Exp(term,NIL()) => {check(tree1.asInstanceOf[Exp].term, tree2.asInstanceOf[Exp].term) | check(tree1.asInstanceOf[Exp].exp2.asInstanceOf[Exp2].exp, tree2)}
             case Term(factor: Any,term: Any) => {check(tree1.asInstanceOf[Exp].term, tree2) | check(tree1.asInstanceOf[Exp].exp2, tree2)}
             case Factor(a, NIL()) => {check(tree1.asInstanceOf[Exp].term, tree2.asInstanceOf[Factor].a) | check(tree1.asInstanceOf[Exp].exp2, tree2.asInstanceOf[Factor].a)}
             case A(char,parenth,NIL()) => {check(tree1.asInstanceOf[Exp].term, tree2) | check(tree1.asInstanceOf[Exp].exp2.asInstanceOf[Exp2].exp, tree2)}
             case _ => false
           }
         }
       case Exp(term, NIL()) =>
         {
           tree2 match
           {
             case Exp(term,NIL()) => {check(tree1.asInstanceOf[Exp].term, tree2.asInstanceOf[Exp].term)}
             case Term(factor,term) => {check(tree1.asInstanceOf[Exp].term, tree2)}
             case A(char,parenth,NIL()) => {check(tree1.asInstanceOf[Exp].term, tree2)}
             case _ => false
           }
         }
       case Term(Factor(a,NIL()), Term(factor, term)) =>
         {
           tree2 match
           {
             case Term(Factor(a,NIL()), Term(factor,term)) => {check(tree1.asInstanceOf[Term].factor, tree2.asInstanceOf[Term].factor) && check(tree1.asInstanceOf[Term].term, tree2.asInstanceOf[Term].term)}
             case Term(Factor(a,NIL()), NIL()) => {false}
             case A(char,parenth,NIL()) => {check(tree1.asInstanceOf[Term].factor, tree2)}
             case _ => false
           }
         }
       case Term(Factor(a,factor2:Factor2), Term(factor,term)) =>
         {
           tree2 match
           {
             case Term(Factor(a,NIL()), factor2: Term) => check(tree1.asInstanceOf[Term].factor.asInstanceOf[Factor].a, tree2.asInstanceOf[Term].factor.asInstanceOf[Factor].a) && check(tree1.asInstanceOf[Term].term, tree2.asInstanceOf[Term].term)
             case Term(factor, NIL()) => check(tree1.asInstanceOf[Term].factor.asInstanceOf[Factor].a, tree2.asInstanceOf[Term].factor.asInstanceOf[Factor].a) || check(tree1.asInstanceOf[Term].term, tree2.asInstanceOf[Term].factor) || check(tree1.asInstanceOf[Term].term, tree2)
             case A(char,parenth,NIL()) => check(tree1.asInstanceOf[Term].factor, tree2)
             case _ => false
           }
         }
       case Term(Factor(a,factor2:Factor2), NIL()) =>
         {
           tree2 match
           {
             case Term(Factor(a,NIL()), Term(factor: Any, term: Term)) => check(tree1.asInstanceOf[Term].factor, tree2.asInstanceOf[Term].factor) && check(tree1.asInstanceOf[Term].term, tree2.asInstanceOf[Term].term) 
             case Term(Factor(a,NIL()), NIL()) => check(tree1.asInstanceOf[Term].factor.asInstanceOf[Factor].a, tree2.asInstanceOf[Term].factor.asInstanceOf[Factor].a)
             case A(char,parenth,NIL()) => check(tree1.asInstanceOf[Term].factor, tree2)
             case _ => false
           }
         }
       case Term(factor,NIL()) =>
         {
           tree2 match
           {
             case Term(factor,NIL()) => check(tree1.asInstanceOf[Term].factor, tree2.asInstanceOf[Term].factor)
             case Term(factor,term) => check(tree1.asInstanceOf[Term].factor, tree2)
             case A(char,parenth,NIL()) => check(tree1.asInstanceOf[Term].factor, tree2)
             case _ => false
           }
         }
       case Factor(a,NIL()) =>
         {
           tree2 match
           {
             case Factor(a,NIL()) => check(tree1.asInstanceOf[Factor].a, tree2.asInstanceOf[Factor].a)
             case Term(factor,term:Term) => check(tree1.asInstanceOf[Factor].a, tree2)
             case Term(factor,NIL()) => check(tree1.asInstanceOf[Factor].a, tree2)
             case A(char,parenth,NIL()) => check(tree1.asInstanceOf[Factor].a, tree2)
             case _ => false
           }
         }
       case A(Char(s),"(",A2(exp, char)) =>
         {
           tree2 match
           {
             case A(Char(s),"",NIL()) => check(tree1.asInstanceOf[A].char, tree2.asInstanceOf[A].char) && check(tree1.asInstanceOf[A].a2.asInstanceOf[A2].exp, tree2)
           }
         }
       case A(Char(s),"",NIL()) =>
         {
           tree2 match
           {
             case A(Char(s),"",NIL()) => check(tree1.asInstanceOf[A].char, tree2.asInstanceOf[A].char)
             case _ => false
           }
         }
       case A(NIL(),"(",A2(exp, char)) => check(tree1.asInstanceOf[A].a2.asInstanceOf[A2].exp, tree2)
       case Char(char) =>
         {
           tree2 match
           {
             case Char(charX) =>
               {
                 if (char == ".")
                 {
                   true
                 }
                 else
                 {
                   charX == char
                 }
               }
             case _ => false
           }
         }
     }
   }
   
    def main(args: Array[String])
    {
      print("Pattern? ")
      var pattern = scala.io.StdIn.readLine()
      
      print("String? ")
      var string = scala.io.StdIn.readLine()
      
      while(string != "exit")
      {
        var patternParse = parseAll(start,pattern).get
        var inputParse = parseAll(start,string).get
        var matches = check(patternParse,inputParse)
        if (matches)
        {
          println("String Match!! ")
          println()
          print("String? ('new' to change pattern) ")
        }
        else
        {
          println("Sorry No Match, Try Again! ")
          print("String? ")
        }
        string = scala.io.StdIn.readLine()
        
        if (string == "new")
        {
            print("New Pattern? ")
            pattern = scala.io.StdIn.readLine()
            patternParse = parseAll(start,pattern).get
            print("String? ")
            string = scala.io.StdIn.readLine()
            inputParse = parseAll(start,string).get
        }
      }
  }
}



