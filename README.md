scala_dsl
=========
For implicits, can't just compel an object to go to its "supertype". i.e., can't force
autoboxing. So, that means that application essentially happens from the right. Can
force changes through similar structure like Baysick's use of apply->tuple, but that
restricts order for the most part. Instead, may need to look at the grammar for application
questions. For example, finding the FIRST and FOLLOW sets of each symbol. After computing that,
should be able to write a FOLLOW method for each type of thing, with specific regard to
what type of object needs to be returned. The problem still lies with things like
String -> Comment. Might be able to reduce the grammar a bit by finding where
Comments are used, then replacing it with String, etc. Essentially,
take advantage of the fact that some 1-1 mappings exist by reducing the size of our
working set. Can potentially find some paths that are easy, and do those.
As stated, if 2 stack, conditional, loop, should be near-Turing-complete.

So, based on experiments, need each symbol S with element e in its FOLLOW set to be
class S_CLASS {
	object e extends E_CLASS {}
}
object S extends S_CLASS{}

Or similar construct with functions as possible so that it can support 'S e' without quotes,
etc. That way, successively calling "s s' s'' s''' s'''' PERIOD" would compile. Want functions
rather than objects wherever possible simply because that way we can create our parse tree.
Use few adjectives, characters, etc. at first, as long as pattern is clear for future
development.

STATUS:
Have scenes, acts, and most unconditional sentences. Need the Values and Constants including
mathematical operators, then can finish off unconditional sentences and call it a day from
the parsing point of view. Then, it's on to Eval. Sigh...


Notes: POSITIVE_NOUN = 1, NEGATIVE_NOUN = -1. ADJ = * 2



Differences - look at results of arithmetic for Comparisons
			- some minor changes to value definitions for simplicity
			- scenes have an assummed exeunt beforehand