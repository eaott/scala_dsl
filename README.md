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