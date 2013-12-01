class Shakespeare {
	import scala.collection.mutable.{HashMap, HashSet, Stack};
	trait MainFollowTrait{
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
	}

	trait ValueFollowTrait {
		def apply(t:TheClass):TheClass = t
		def apply(m:MyClass):MyClass = m
		def apply(y:YourClass):YourClass = y
		def apply(n:NothingClass):NothingClass = n
        def THE:TheClass = THE
        def MY:MyClass = MY
        def YOUR:YourClass = YOUR
        def NOTHING:NothingClass = NOTHING

        def apply(p:Pronoun):Pronoun = p
        def YOURSELF:Pronoun = YOURSELF
        def MYSELF:Pronoun = MYSELF

        def apply(b:BinaryOp):BinaryOp = b
        def THE_DIFFERENCE_BETWEEN:BinaryOp = THE_DIFFERENCE_BETWEEN
        def THE_PRODUCT_OF:BinaryOp = THE_PRODUCT_OF
        def THE_QUOTIENT_BETWEEN:BinaryOp = THE_QUOTIENT_BETWEEN
        def THE_REMAINDER_OF_THE_QUOTIENT_BETWEEN:BinaryOp = THE_REMAINDER_OF_THE_QUOTIENT_BETWEEN
        def THE_SUM_OF:BinaryOp = THE_SUM_OF
	}
	class AndClass extends ValueFollowTrait{}
	class ValueBuilder extends ValueFollowTrait{}
	class GeneralStatement extends MainFollowTrait{}
	class Pronoun(c:Character){
		def apply(s:StatementSymbolClass):GeneralStatement = new GeneralStatement
		def apply(a:AndClass):ValueBuilder = new ValueBuilder
		def AND:ValueBuilder = new ValueBuilder
		def STATEMENT_SYMBOL:GeneralStatement = new GeneralStatement
	}
	class BinaryOp extends ValueFollowTrait{
		var fn:Function2[Int,Int,Int] = null;
		// FIXME followed by Value AND/Value STATEMENT_SYMBOL
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
    	def RIGHT_BRACKET:EnterExit = new EnterExit
    }
    class CharacterAndBuilder{
    	def apply(c:Character):CharacterList = new CharacterList
    }
    class CharacterListBuilder{
    	def AND:CharacterAndBuilder = new CharacterAndBuilder
    }


    class EnterExit extends SceneContents{}
    class EnterClass{
    	def apply(c:Character):CharacterListBuilder = new CharacterListBuilder
    }
    class ExeuntClass{
    	def apply(c:Character):CharacterListBuilder = new CharacterListBuilder
    }
    class ExitCharacter{
    	def RIGHT_BRACKET:EnterExit = new EnterExit
    }
    class ExitClass{
    	def apply(c:Character):ExitCharacter = new ExitCharacter
    }
    class EntreCharacter{
    	def RIGHT_BRACKET:EnterExit = new EnterExit
    }
    class EntreClass{
    	def apply(c:Character):EntreCharacter = new EntreCharacter
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
    	def STATEMENT_SYMBOL:InOut = new InOut
    }
    class OpenMindBuilder{
    	def STATEMENT_SYMBOL:InOut = new InOut
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
    	def STATEMENT_SYMBOL:InOut = new InOut
    }
    class SpeakBuilder{
    	def apply(m:MindClass):SpeakMindBuilder = new SpeakMindBuilder
    }
    class ListenHeartBuilder {
    	def apply(s:StatementSymbolClass):InOut = new InOut
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
		def apply(a:ActRomanClass):JumpBuilder = new JumpBuilder
		def apply(s:SceneRomanClass):JumpBuilder = new JumpBuilder
	}
	class JumpPhraseBeginning{
		def PROCEED_TO:JumpPhrase = new JumpPhrase
	}



	class RecallClass extends UnconditionalSentence{}
	class RecallBuilder{
		def apply(s:StatementSymbolClass):RecallClass = new RecallClass
	}
	class RecallBeginningClass{
		def YOURSELF:RecallBuilder = new RecallBuilder
	}

	class ConstantBuilder{
		def apply(a:AsClass):AsClass = a
		def apply(t:TheClass):TheClass = t
		def apply(y:YourClass):YourClass = y
		def apply(n:NothingClass):NothingClass = n
		def apply(m:MyClass):MyClass = m
	}
	class YouClass{
		def ARE:ConstantBuilder = new ConstantBuilder
	}
	class Equality extends ValueFollowTrait{

	}

	class Statement extends MainFollowTrait{

	}
	class UnarticulatedConstant{
		def STATEMENT_SYMBOL:Statement = new Statement
	}
	class UnarticulatedConstantBuilder{
		def GOOD:UnarticulatedConstantBuilder = this
		def HORRIBLE: UnarticulatedConstantBuilder = this
		def FRIEND:UnarticulatedConstant = new UnarticulatedConstant
		def ENEMY:UnarticulatedConstant = new UnarticulatedConstant
		def apply(a:PositiveComparative):UnarticulatedConstantBuilder = this
		def apply(a:NegativeComparative):UnarticulatedConstantBuilder = this
		def apply(n:PositiveNoun):UnarticulatedConstant = new UnarticulatedConstant
		def apply(n:NegativeNoun):UnarticulatedConstant = new UnarticulatedConstant
	}
	class TheClass extends UnarticulatedConstantBuilder{}
	class YourClass extends UnarticulatedConstantBuilder{}
	class MyClass extends UnarticulatedConstantBuilder{}
	class NothingClass{
		def STATEMENT_SYMBOL:Statement = new Statement
	}

	class PositiveNoun{}
	class NegativeNoun{}
	class RememberClass{
		def COMMA:ValueBuilder = new ValueBuilder
	}


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


    class Act(name:String){
    	def apply(s:SceneRomanClass):SceneRomanClass = s
    }
	class ActRomanClass{
		var name = ""
		def COLON:Act = {
			curAct = new Act(this.name)
			curAct
		}
	}
	class Scene{
		def apply(e:EnterExitStart):EnterExitStart = e
		def apply(c:Character):Character = c
	}
	class SceneRomanClass{
		var name = ""
		def COLON:Scene = {
			new Scene
		}
	}
	class Character{
		var name:String = "";
		var curVal:Int = 0;
		var stack:Stack[Int] = new Stack[Int]
		def COLON:SentenceList = {
			curSpeaker = this
			new SentenceList
		}
	}

	var stmts = new Array[Line](0);
	var vals = HashMap.empty[Character, Stack[String]]

	var curSpeaker:Character = null;
	var curListener:Character = null;
	var curAct:Act = null;
	var curScene:Scene = null;
}

object tester extends Shakespeare{
	object ACT_I extends ActRomanClass{
		name = "i"
	}
	object ACT_II extends ActRomanClass{
		name = "ii"
	}
	object SCENE_I extends SceneRomanClass{
		name = "i"
	}
	object SCENE_II extends SceneRomanClass{
		name = "ii"
	}
	object ROMEO extends Character{
		name = "Romeo"
	}
	object JULIET extends Character{
		name = "Juliet"
	}

	def main(args: Array[String]): Unit = {
		ACT_I COLON SCENE_I COLON LEFT_BRACKET ENTER ROMEO AND JULIET RIGHT_BRACKET ROMEO COLON YOU ARE MY GOOD GOOD GOOD FRIEND STATEMENT_SYMBOL YOU ARE AS GOOD AS THE_SUM_OF YOURSELF AND YOURSELF STATEMENT_SYMBOL REMEMBER COMMA YOURSELF STATEMENT_SYMBOL/*IF_SO COMMA SPEAK YOUR MIND STATEMENT_SYMBOL*/ SCENE_II COLON JULIET COLON LET_US PROCEED_TO ACT_II STATEMENT_SYMBOL LEFT_BRACKET EXIT JULIET RIGHT_BRACKET ACT_II COLON SCENE_II COLON ROMEO COLON SPEAK YOUR MIND STATEMENT_SYMBOL
	}
}