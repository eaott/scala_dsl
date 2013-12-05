/**
Used to provide a Scala internal DSL based largely on the Shakespeare Programming Language,
but modified somewhat for a DSL/BASIC/assembly-like construction.
*/
class Shakespeare {
	import scala.collection.mutable.{HashMap, HashSet, Stack};
	import scala.collection.immutable.{TreeMap};

	// used for jump PCs
	private var index = Tuple3(0,0,0)
	// store all lines as Act->Scene->Line where line has all necessary data
	private var stmts = new java.util.TreeMap[Int, java.util.TreeMap[Int, java.util.List[Execution]]]();

	// used to keep track of characters on stage
	private var curSpeaker:Character = null;
	private var curListener:Character = null;
	private var curCharacters = HashSet.empty[Character];

	// used to keep track of what act and scene we are in during parsing
	private var curAct:Act = null;
	private var curScene:Scene = null;

	// keeps track of last value computed
	private var lastVal:Int = 0;


	/**
		Used largely for stringing commands together on a single line. More like normal Shakespeare
		language which essnetially treats all whitespace the same to read more like English,
		separated on multiple lines like this comment.

		This only allows full commands on each line, but does permit putting many together.
	*/
	class MainFollowTrait{
		def apply(c:Conditional):Conditional = c
    	def apply(s:SpeakClass):SpeakClass = s
    	def apply(o:OpenClass):OpenClass = o
    	def apply(l:ListenClass):ListenClass = l
    	def apply(j:JumpPhraseBeginning):JumpPhraseBeginning = j
    	def apply(r:RecallBeginningClass):RecallBeginningClass = r
    	def apply(e:EnterExitStart):EnterExitStart = e
    	def apply(c:Character):Character = c
    	def apply(s:SceneRomanClass):SceneRomanClass = s
    	def apply(a:ActRomanClass):ActRomanClass = a
    	def apply(y:YouClass):YouClass = y
    	def apply(r:RememberClass):RememberClass = r

    	/**
    		When RUN is the given statement, start executing at first act/scene.
    	*/
    	def apply(r:RunClass):Unit = {
    		var actIndex:Int = stmts.firstKey();
    		while(actIndex <= stmts.lastKey() && actIndex > 0){
    			var sceneIndex:Int = stmts.get(actIndex).firstKey()
    			while(sceneIndex <= stmts.get(actIndex).lastKey() && sceneIndex > 0){
    				var lineIndex:Int = 0
    				while(lineIndex < stmts.get(actIndex).get(sceneIndex).size()){
    					index = Tuple3(actIndex, sceneIndex, lineIndex);
    					// execution could include a jump - test for this and use that PC if changed
    					stmts.get(actIndex).get(sceneIndex).get(lineIndex).go()
    					if (index._1 != actIndex || index._2 != sceneIndex || index._3 != lineIndex)
    					{
    						actIndex = index._1
    						sceneIndex = index._2
    						lineIndex = index._3
    					}
    					else
    					{
    						lineIndex = lineIndex + 1
    					}
    				}
    				sceneIndex = stmts.get(actIndex).higherKey(sceneIndex)
    			}
    			actIndex = stmts.higherKey(actIndex)
    		}
    	}
	}

	/**
		Used specifically for cases where a Value needs to follow the previous word.
		Two versions are given for each to account for the "parity" of the word within the line which
		gets evaluated as a.b(c).d(e).f(g)...
	*/
	class ValueFollowTrait {
		def apply(t:TheClass):TheClass = t
		def apply(m:MyClass):MyClass = m
		def apply(y:YourClass):YourClass = y
		def apply(n:NothingClass):NothingClass = n
        def THE:TheClass = {new TheClass() }
        def MY:MyClass = {new MyClass() }
        def YOUR:YourClass = {new YourClass() }
        def NOTHING:NothingClass = {new NothingClass() }

        def apply(p:Pronoun):Pronoun = p
        def YOURSELF:Pronoun = {new Pronoun(curListener) }
        def MYSELF:Pronoun = {new Pronoun(curSpeaker) }

        def apply(b:BinaryOp):BinaryOp = {curOp = b; isBinary = true; b}
        def THE_DIFFERENCE_BETWEEN:BinaryOp = {curOp = new BinaryOp(); curOp.fn = (a:Int, b:Int)=>(a - b); isBinary = true;curOp}
        def THE_PRODUCT_OF:BinaryOp = {curOp = new BinaryOp(); curOp.fn = (a:Int, b:Int)=>(a * b); isBinary = true;curOp}
        def THE_QUOTIENT_BETWEEN:BinaryOp = {curOp = new BinaryOp(); curOp.fn = (a:Int, b:Int)=>(a / b); isBinary = true;curOp}
        def THE_REMAINDER_OF_THE_QUOTIENT_BETWEEN:BinaryOp = {curOp = new BinaryOp(); curOp.fn = (a:Int, b:Int)=>(a % b); isBinary = true;curOp}
        def THE_SUM_OF:BinaryOp = {curOp = new BinaryOp(); curOp.fn = (a:Int, b:Int)=>(a + b); isBinary = true;curOp}
	}

	/*********************************************************************************
	Class definitions follow, all based around the grammar construct they represent.
	**********************************************************************************/

	var curOp:BinaryOp = null;
	var val1:ValueExec = null;
	class AndClass extends ValueFollowTrait{}
	class ValueBuilder extends ValueFollowTrait{}
	class GeneralStatement extends MainFollowTrait{}
	class FoolClass{}
	object FOOL extends FoolClass{}
	class Pronoun(c:Character){
		def apply(s:FoolClass):Pronoun = { this }
		def apply(a:AndClass):ValueBuilder = {
			val1 = new CharacterExec(c)
			new ValueBuilder
		}
		def AND:ValueBuilder = {
			val1 = new CharacterExec(c)
			new ValueBuilder
		}
		def STATEMENT_SYMBOL:GeneralStatement = {
			var exec:Execution = null;
			val ch:CharacterExec = new CharacterExec(c)
			if (isBinary)
			{
				if (isRemember)
				{
					exec = new RememberExec(curListener, new BinOpExec(curOp.fn, val1, ch))
				}
				else{
					exec = new AssignmentExec(curListener, new BinOpExec(curOp.fn, val1, ch))
				}
			}
			else
			{
				if(isRemember)
				{
					exec = new RememberExec(curListener, ch)
				}
				else{
					exec = new AssignmentExec(curListener, ch)
				}
			}
			stmts.get(curAct.num).get(curScene.num).add(exec)
			new GeneralStatement()
		}

	}
	class BinaryOp extends ValueFollowTrait{
		var fn:Function2[Int,Int,Int] = null;
	}

	/* Used to create SceneContents - once you have either an EnterExit or a
		Line, can create another one
	*/
	class SceneContents{
		def apply(e:EnterExitStart):EnterExitStart = e
		def apply(c:Character):Character = c
		def apply(a:ActRomanClass):ActRomanClass = a
		def apply(s:SceneRomanClass):SceneRomanClass = s
	}


	class CharacterList{
	def RIGHT_BRACKET:EnterExit = new EnterExit()
	}
	class CharacterAndBuilder(b:Boolean){
	def apply(c:Character):CharacterList = {
		if (b)
		{
			curCharacters.add(c);
			if (curCharacters.size > 2)
				throw new IllegalStateException("Too many characters");
		}
		else
			curCharacters.remove(c);
		new CharacterList
	}
	}
	class CharacterListBuilder(b:Boolean){
	def AND:CharacterAndBuilder = new CharacterAndBuilder(b)
	}


    class EnterExit extends SceneContents{}
    class EnterClass{
    	def apply(c:Character):CharacterListBuilder = {
    		curCharacters.add(c);
    		if (curCharacters.size > 2)
    			throw new IllegalStateException("Too many characters");
    		new CharacterListBuilder(true)
    	}
    }
    class ExeuntClass{
    	def apply(c:Character):CharacterListBuilder = {
    		curCharacters.remove(c);
    		new CharacterListBuilder(false)
    	}
    }
    class ExitCharacter{
    	def RIGHT_BRACKET:EnterExit = new EnterExit
    }
    class ExitClass{
    	def apply(c:Character):ExitCharacter = {
    		curCharacters.remove(c);
    		new ExitCharacter
    	}
    }
    class EntreCharacter{
    	def RIGHT_BRACKET:EnterExit = new EnterExit
    }
    class EntreClass{
    	def apply(c:Character):EntreCharacter = {
    		curCharacters.add(c);
    		if (curCharacters.size > 2)
    			throw new IllegalStateException("Too many characters")
    		new EntreCharacter
    	}
    }
    class EnterExitStart{
    	// many chars
    	def ENTER:EnterClass = new EnterClass
    	// single char
    	def ENTRE:EntreClass = new EntreClass
    	// many chars
    	def EXEUNT:ExeuntClass = new ExeuntClass
    	// single char
    	def EXIT:ExitClass = new ExitClass
    }


	class Line extends SceneContents{}

	class Inequality {}
	class ThanClass {}
	class Comparative{
		def apply(t:ThanClass):Inequality = new Inequality
	}
	class NegativeComparative extends Comparative{}
	class PositiveComparative extends Comparative{}
	class MoreClass {
		def GOOD:PositiveComparative = {
			new PositiveComparative
		}
		def HORRIBLE:NegativeComparative = {
			new NegativeComparative
		}
	}
	class LessClass {
		def GOOD:NegativeComparative = {
			new NegativeComparative
		}
		def HORRIBLE:PositiveComparative = {
			new PositiveComparative
		}
	}
	class EqualityBuilder {
		def apply(a:AsClass):Equality = {
			new Equality
		}
	}
	class AsClass{
		def GOOD:EqualityBuilder = new EqualityBuilder
		def HORRIBLE:EqualityBuilder = new EqualityBuilder
	}

    class StatementSymbolClass{}
    /* Used for building up SentenceLists
       Once you have one, can have another one.*/
    class SentenceList extends MainFollowTrait{}
    class Sentence extends SentenceList{}
    class UnconditionalSentence extends Sentence{}
    class InOut extends UnconditionalSentence{}
    class HeartClass{}
    class MindClass{}
    class OpenHeartBuilder{
    	def STATEMENT_SYMBOL:InOut = {
    		stmts.get(curAct.num).get(curScene.num).add(new PrintExec(true,curListener))
    		new InOut
    	}
    }
    class OpenMindBuilder{
    	def STATEMENT_SYMBOL:InOut = {
    		stmts.get(curAct.num).get(curScene.num).add(new ReadExec(false, curListener))
    		new InOut
    	}
    }
    class OpenClass{
    	def YOUR:OpenBuilder = new OpenBuilder
    }
    class SpeakClass{
    	def YOUR:SpeakBuilder = new SpeakBuilder
    }
    class ListenClass{
    	def TO:ListenBuilder = new ListenBuilder
    }
    class OpenBuilder{
    	def apply(h:HeartClass):OpenHeartBuilder = new OpenHeartBuilder
    	def apply(m:MindClass):OpenMindBuilder = new OpenMindBuilder
    }
    class SpeakMindBuilder{
    	def STATEMENT_SYMBOL:InOut = {
    		stmts.get(curAct.num).get(curScene.num).add(new PrintExec(false, curListener))
    		new InOut
    	}
    }
    class SpeakBuilder{
    	def apply(m:MindClass):SpeakMindBuilder = {
    		new SpeakMindBuilder
    	}
    }
    class ListenHeartBuilder {
    	def apply(s:StatementSymbolClass):InOut = {
    		stmts.get(curAct.num).get(curScene.num).add(new ReadExec(true, curListener))
    		new InOut
    	}
    }
    class ListenYourBuilder {
    	def HEART:ListenHeartBuilder = new ListenHeartBuilder
    }
    class ListenBuilder{
    	def apply(y:YourClass):ListenYourBuilder = new ListenYourBuilder
    }

    /* Used for building  */
	class UnconditionalSentenceBuilder extends MainFollowTrait{}
	class Conditional{
		def COMMA:UnconditionalSentenceBuilder = new UnconditionalSentenceBuilder
	}





	class JumpClass extends UnconditionalSentence{}
	class JumpBuilder{
		def STATEMENT_SYMBOL:JumpClass = new JumpClass
	}
	class JumpPhrase{
		def apply(a:ActRomanClass):JumpBuilder = {
			stmts.get(curAct.num).get(curScene.num).add(new ActJumpExec(a.id))
			new JumpBuilder
		}
		def apply(s:SceneRomanClass):JumpBuilder = {
			stmts.get(curAct.num).get(curScene.num).add(new SceneJumpExec(s.id))
			new JumpBuilder
		}
	}
	class JumpPhraseBeginning{
		def PROCEED_TO:JumpPhrase = new JumpPhrase
	}



		class RecallClass extends UnconditionalSentence{}
		class RecallBeginningClass{
			def STATEMENT_SYMBOL:RecallClass = {
				stmts.get(curAct.num).get(curScene.num).add(new RecallExec(curListener))
				new RecallClass
			}
		}

	var isRemember:Boolean = false;
	var isBinary:Boolean = false;

	class ConstantBuilder{
		def apply(a:AsClass):AsClass = a
		def apply(t:TheClass):TheClass = t
		def apply(y:YourClass):YourClass = y
		def apply(n:NothingClass):NothingClass = n
		def apply(m:MyClass):MyClass = m
	}
	class YouClass{
		def ARE:ConstantBuilder = {
			isRemember = false; isBinary = false;
			new ConstantBuilder
		}
	}
	class Equality extends ValueFollowTrait{}
	class Statement extends MainFollowTrait{}
	class UnarticulatedConstant(n:Int){
		def STATEMENT_SYMBOL:Statement = {
			var exec:Execution = null;
			val c:ConstantExec = new ConstantExec(n)
			if (isBinary)
			{
				if (isRemember)
				{
					exec = new RememberExec(curListener, new BinOpExec(curOp.fn, val1, c))
				}
				else{
					exec = new AssignmentExec(curListener, new BinOpExec(curOp.fn, val1, c))
				}
			}
			else
			{
				if(isRemember)
				{
					exec = new RememberExec(curListener, c)
				}
				else{
					exec = new AssignmentExec(curListener, c)
				}
			}
			stmts.get(curAct.num).get(curScene.num).add(exec)
			new Statement
		}
	}
	// Used to build up the constant value using only GOOD, HORRIBLE (*2) and FRIEND(1), ENEMY(-1)
	class UnarticulatedConstantBuilder{
		var num = 1
		def GOOD:UnarticulatedConstantBuilder = {num = num * 2; this}
		def HORRIBLE: UnarticulatedConstantBuilder = {num = num * 2; this}
		def FRIEND:UnarticulatedConstant = {var t = new UnarticulatedConstant(num); num = 1; t}
		def ENEMY:UnarticulatedConstant = {var t = new UnarticulatedConstant(num * -1); num = 1; t}
		def apply(a:PositiveComparative):UnarticulatedConstantBuilder = {num = num * 2; this}
		def apply(a:NegativeComparative):UnarticulatedConstantBuilder = {num = num * 2; this}
		def apply(n:PositiveNoun):UnarticulatedConstant = {var t = new UnarticulatedConstant(num); num = 1; t}
		def apply(n:NegativeNoun):UnarticulatedConstant = {var t = new UnarticulatedConstant(num * -1); num = 1; t}
	}
	class TheClass extends UnarticulatedConstantBuilder{}
	class YourClass extends UnarticulatedConstantBuilder{}
	class MyClass extends UnarticulatedConstantBuilder{}
	// Used for the 0 value
	class NothingClass{
		def STATEMENT_SYMBOL:Statement = {
			var exec:Execution = null;
			val c:ConstantExec = new ConstantExec(0)
			if (isBinary)
			{
				if (isRemember)
				{
					exec = new RememberExec(curListener, new BinOpExec(curOp.fn, val1, c))
				}
				else{
					exec = new AssignmentExec(curListener, new BinOpExec(curOp.fn, val1, c))
				}
			}
			else
			{
				if(isRemember)
				{
					exec = new RememberExec(curListener, c)
				}
				else{
					exec = new AssignmentExec(curListener, c)
				}
			}
			stmts.get(curAct.num).get(curScene.num).add(exec)
			new Statement
		}
	}

	class PositiveNoun{}
	class NegativeNoun{}
	class RememberClass{
		def COMMA:ValueBuilder = {isRemember = true; isBinary = false;
			new ValueBuilder
		}
	}

	// Create objects from the classes so that they can be used without parentheses, etc. in the DSL
	object REMEMBER extends RememberClass{}
	object FRIEND extends PositiveNoun {}
	object ENEMY extends NegativeNoun{}
	object THE extends TheClass{}
	object NOTHING extends NothingClass{}
	object MY extends MyClass{}
	object AND extends AndClass{}
	object AS extends AsClass{}
	object GOOD extends PositiveComparative{}
	object HORRIBLE extends NegativeComparative{}
	object IF_NOT extends Conditional{}
	object IF_SO extends Conditional{}
	object LEFT_BRACKET extends EnterExitStart{}
	object LESS extends LessClass{}
	object LET_US extends JumpPhraseBeginning{}
	def YOURSELF:Pronoun = new Pronoun(curListener)
	def MYSELF:Pronoun = new Pronoun(curSpeaker)
	object MORE extends MoreClass{}
	object THAN extends ThanClass{}
    object HEART extends HeartClass{}
    object LISTEN extends ListenClass{}
    object MIND extends MindClass {}
    object OPEN extends OpenClass{}
    object RECALL extends RecallBeginningClass{}
    object SPEAK extends SpeakClass{}
    object STATEMENT_SYMBOL extends StatementSymbolClass{}
    object YOU extends YouClass{}
    object YOUR extends YourClass{}

    object THE_DIFFERENCE_BETWEEN extends BinaryOp {
    	fn = (a:Int, b:Int)=>(a - b);
    }
    object THE_PRODUCT_OF extends BinaryOp {
    	fn = (a:Int, b:Int)=>(a * b);
    }
    object THE_QUOTIENT_BETWEEN extends BinaryOp {
    	fn = (a:Int, b:Int)=>(a / b);
    }
    object THE_REMAINDER_OF_THE_QUOTIENT_BETWEEN extends BinaryOp {
    	fn = (a:Int, b:Int)=>(a % b);
    }
    object THE_SUM_OF extends BinaryOp {
    	fn = (a:Int, b:Int)=>(a + b);
    }


    class Act(id:Int){
    	var num:Int = id;
    	def apply(s:SceneRomanClass):SceneRomanClass = s
    }
	class ActRomanClass{
		var id = 0;
		def COLON:Act = {
			curAct = new Act(this.id)
			stmts.put(id, new java.util.TreeMap[Int, java.util.List[Execution]]())
			curAct
		}
	}
	class Scene(id:Int){
		var num:Int = id;
		def apply(e:EnterExitStart):EnterExitStart = e
		def apply(c:Character):Character = c
	}
	class SceneRomanClass{
		var id = 0;
		def COLON:Scene = {
			curScene = new Scene(this.id)
			stmts.get(curAct.num).put(id, new java.util.ArrayList[Execution]())
			curCharacters.clear()
			curScene
		}
	}

	/**
	Keep track of characters and throw exceptions if more than 2 characters are on stage at a time.
	*/
	class Character{
		var name:String = "";
		var curVal:Int = 0;
		var stack:Stack[Int] = new Stack[Int]
		def COLON:SentenceList = {
			if (!curCharacters.contains(this))
				throw new IllegalStateException("Character talking who isn't on stage")
			curSpeaker = this;
			var temp = curCharacters.clone();
			temp.remove(curSpeaker);
			if (temp.size > 1)
				throw new IllegalStateException("Too many people present")
			if (temp.first == None)
				curListener = null;
			else
				curListener = temp.first;
			new SentenceList
		}
	}

	/**
	Used to represent the various kinds of statements
	*/
	abstract class Execution{
		def go():Unit
	}
	case class AssignmentExec(c:Character, v:ValueExec) extends Execution
	{
		def go():Unit = {c.curVal = v.value()}
	}
	case class RecallExec(c:Character) extends Execution
	{
		def go():Unit = {c.curVal = c.stack.pop()}
	}
	case class RememberExec(c:Character, v:ValueExec) extends Execution
	{
		def go():Unit = {c.stack.push(v.value())}
	}
	case class EndExec extends Execution
	{
		def go():Unit = {exit}
	}
	case class ActJumpExec(num:Int) extends Execution
	{
		def go():Unit = {
			var firstIndex:Int = stmts.get(num).firstKey()
			index = Tuple3(num, firstIndex, 1)
		}
	}
	case class SceneJumpExec(num:Int) extends Execution
	{
		def go():Unit = {
			index = Tuple3(index._1, num, 1)
		}
	}
	case class CondExec(b:Boolean, e:Execution) extends Execution
	{
		def go():Unit = {
			if (b)
			{
				if (lastVal != 0)
					e.go()
			}
			else
				if (lastVal == 0)
					e.go()
		}
	}
	case class PrintExec(isNum:Boolean, c:Character) extends Execution
	{
		def go():Unit = {
			if (isNum)
				println(c.curVal)
			else
				println(c.curVal.toChar)
		}
	}
	case class ReadExec(isNum:Boolean, c:Character) extends Execution
	{
		def go():Unit = {
			var line:String = readLine
			if (isNum)
				c.curVal = java.lang.Integer.parseInt(line)
			else
				c.curVal = line.charAt(0).toInt
		}
	}

	/**
	Used to represent integer values coming from different sources.
	*/
	abstract class ValueExec
	{
		def value():Int
	}
	case class CharacterExec(c:Character) extends ValueExec
	{
		def value():Int = c.curVal
	}
	case class ConstantExec(v:Int) extends ValueExec
	{
		def value():Int = v
	}
	case class BinOpExec(fn:Function2[Int,Int,Int], v1:ValueExec, v2:ValueExec) extends ValueExec
	{
		def value():Int = fn(v1.value(), v2.value())
	}
	class RunClass{}
	object RUN extends RunClass{}
}

