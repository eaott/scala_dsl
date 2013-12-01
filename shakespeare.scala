import scala.collection.mutable.{HashMap, HashSet, Stack};
class Shakespeare {
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
	}
	class Value{}

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
    class CharacterCommaBuilder{
    	def apply(c:Character):CharacterListBuilder = new CharacterListBuilder
    }
    class CharacterListBuilder{
    	def AND:CharacterAndBuilder = new CharacterAndBuilder
    	def COMMA:CharacterCommaBuilder = new CharacterCommaBuilder
    }


    class EnterExit extends SceneContents{}
    class EnterClass{
    	def apply(c:Character):CharacterListBuilder = new CharacterListBuilder
    }
    class ExeuntClass{
    	def apply(c:Character):CharacterList = new CharacterList
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




	object AS extends AsClass{}
	object GREAT extends PositiveComparative{}
	object HORRIBLE extends NegativeComparative{}
	object IF_NOT extends Conditional{}
	object IF_SO extends Conditional{}
	object LEFT_BRACKET extends EnterExitStart{}
	object LESS extends LessClass{}
	object LET_US extends JumpPhraseBeginning{}
	object MORE extends MoreClass{}
	object THAN extends ThanClass{}
    object HEART extends HeartClass{}
    object LISTEN extends ListenClass{}
    object MIND extends MindClass {}
    object OPEN extends OpenClass{}
    object RECALL extends RecallBeginningClass{}
    object SPEAK extends SpeakClass{}
    object STATEMENT_SYMBOL extends StatementSymbolClass{}
    object YOUR extends YourClass{}

    class Act{
    	def apply(s:SceneRomanClass):SceneRomanClass = s
    }
	class ActRomanClass{
		def COLON:Act = {
			curAct = new Act
			curAct
		}
	}
	class Scene{
		def apply(e:EnterExitStart):EnterExitStart = e
		def apply(c:Character):Character = c
	}
	class SceneRomanClass{
		def COLON:Scene = new Scene
	}
	class Character extends Value{
		var name:String = "";
		def COLON:SentenceList = {
			curSpeaker = this
			new SentenceList
		}
	}

	var stmts = new Array[Line](0);
	var vals = HashMap.empty[Character, Stack[String]]

	var curSpeaker = new Character()
	var curAct = new Act()
	var characters = HashSet.empty[Character]
}

object tester extends Shakespeare{
	object ACT_I extends ActRomanClass{}
	object ACT_II extends ActRomanClass{}
	object SCENE_I extends SceneRomanClass{}
	object SCENE_II extends SceneRomanClass{}
	object ROMEO extends Character{
		name = "Romeo"
	}
	object JULIET extends Character{
		name = "Juliet"
	}

	def main(args: Array[String]): Unit = {
		ACT_I COLON SCENE_I COLON ROMEO COLON IF_SO COMMA SPEAK YOUR MIND STATEMENT_SYMBOL SCENE_II COLON JULIET COLON LET_US PROCEED_TO SCENE_II STATEMENT_SYMBOL LEFT_BRACKET ENTRE JULIET RIGHT_BRACKET ACT_II COLON SCENE_II COLON ROMEO COLON IF_SO COMMA SPEAK YOUR MIND STATEMENT_SYMBOL
	}
}