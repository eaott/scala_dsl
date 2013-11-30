import scala.collection.mutable.{HashMap, HashSet, Stack};
object Shakespeare {
	class Character {
		var name:String = "";
		var valid:Boolean = false;
		def COLON:LineBuilder = new LineBuilder
	}
	class Line{}
	class LineBuilder{
		def apply(c:Conditional):Conditional = c
    	def apply(s:SpeakClass):SpeakClass = s
    	def apply(o:OpenClass):OpenClass = o
    	def apply(l:ListenClass):ListenClass = l
    	def apply(j:JumpPhraseBeginning):JumpPhraseBeginning = j
	}
	class Equality {}
	class Inequality {}
	class ThanClass {}
	class Comparative{
		def apply(t:ThanClass):Inequality = new Inequality
	}
	class NegativeComparative extends Comparative{}
	class PositiveComparative extends Comparative{}
	class MoreClass {
		def GREAT:PositiveComparative = {
			new PositiveComparative
		}
		def HORRIBLE:NegativeComparative = {
			new NegativeComparative
		}
	}
	class LessClass {
		def GREAT:NegativeComparative = {
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
		def GREAT:EqualityBuilder = new EqualityBuilder
		def HORRIBLE:EqualityBuilder = new EqualityBuilder
	}
    class StatementSymbolClass{}
    class SentenceList{
    	def apply(c:Conditional):Conditional = c
    	def apply(s:SpeakClass):SpeakClass = s
    	def apply(o:OpenClass):OpenClass = o
    	def apply(l:ListenClass):ListenClass = l
    	def apply(j:JumpPhraseBeginning):JumpPhraseBeginning = j
    }
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
    class YourClass{}
    class ListenHeartBuilder {
    	def apply(s:StatementSymbolClass):InOut = new InOut
    }
    class ListenYourBuilder {
    	def HEART:ListenHeartBuilder = new ListenHeartBuilder
    }
    class ListenBuilder{
    	def apply(y:YourClass):ListenYourBuilder = new ListenYourBuilder
    }

	class SentenceBuilder{
		// THE FOLLOW SET PARADIGM!!!
		def apply(s:SpeakClass):SpeakClass = s
		def apply(o:OpenClass):OpenClass = o
		def apply(l:ListenClass):ListenClass = l
		def apply(a:JumpPhraseBeginning):JumpPhraseBeginning = a
	}
	class Conditional{
		def COMMA:SentenceBuilder = new SentenceBuilder
	}
	class ActRomanClass{}
	class SceneRomanClass{}
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

	object ACT_I extends ActRomanClass{}
	object ACT_II extends ActRomanClass{}
	object AS extends AsClass{}
	object GREAT extends PositiveComparative{}
	object HORRIBLE extends NegativeComparative{}
	object IF_NOT extends Conditional{}
	object IF_SO extends Conditional{}
	object LESS extends LessClass{}
	object LET_US extends JumpPhraseBeginning{}
	object MORE extends MoreClass{}
	object SCENE_I extends SceneRomanClass{}
	object SCENE_II extends SceneRomanClass{}
	object THAN extends ThanClass{}
    object HEART extends HeartClass{}
    object LISTEN extends ListenClass{}
    object MIND extends MindClass {}
    object OPEN extends OpenClass{}
    object SPEAK extends SpeakClass{}
    object STATEMENT_SYMBOL extends StatementSymbolClass{}
    object YOUR extends YourClass{}
	object ROMEO extends Character{
		name = "Romeo"
	}
	object JULIET extends Character{
		name = "Juliet"
	}



	var stmts = new Array[Line](0);
	var vals = HashMap.empty[Character, Stack[String]]

	var curSpeaker = new Character()
	var characters = HashSet.empty[Character]

	// Continue the FOLLOW set idea. Going to be nutso. If I can get the
	// grammar to be regular and not CF, that should do it. Then, I can keep
	// a state during parsing that says what I just saw, but consolidate the rest.
	// essentially, by combining the follow set as a single look-ahead, I should
	// possibly be able to do it. the question is, since it broke on
	/*

	Can also look into using functions declared with no arguments (and no parens)
	*/

	def main(args: Array[String]): Unit = {
		ROMEO COLON IF_SO COMMA SPEAK YOUR MIND STATEMENT_SYMBOL SPEAK YOUR MIND STATEMENT_SYMBOL LET_US PROCEED_TO SCENE_I STATEMENT_SYMBOL
	}
	
}