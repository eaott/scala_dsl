scala_dsl
=========
This project is a Scala internal DSL for a variant of the Shakespeare Programming Language
(http://shakespearelang.sourceforge.net/) called, for lack of a better name, "Shakesembly".

To run, compile the DSL then run the test code:
::

    scalac shakespeare.scala
    scala othertest.scala

"Shakesembly" has some interesting aspects for an internal DSL, but some hindrances.
For many internal DSLs, creating variables involves either using a symbol - `name - or String
- "name". In the original language, only existing Shakespeare characters (from the plays) are allowed,
and must be specified in each file's header. Similarly, in Shakesembly, the user may create arbitrary new
characters:
::

    object ROMEO extends Character{}
    
Further, they must define the acts and scenes they want. Outside of that, they can use an all upper-case verion
of the language with a restricted set of adjectives, nouns, etc. (easily extended). Essentially, the code
executes like assembly with simple assignments and mathematical operations, character stacks, single value
output (chars or ints), jumps, and conditionals (uses result of previous statement - IF_SO corresponds to
the last value being equal to 0, IF_NOT is not equal to 0). The comments are removed from the language
for ease in parsing. Also, since the period and colon cannot be remapped, they are replaced by
whitespace-separated symbols STATEMENT_SYMBOL and COLON, similar to how they are represented in
the original grammar.

:code:`othertest.scala` contains an example of the language with comments to help for understanding evaluation.
