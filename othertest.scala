object test extends Shakespeare{
	object ACT_I extends ActRomanClass{
		id=1
	}
	object ACT_II extends ActRomanClass{
		id=2
	}
	object SCENE_I extends SceneRomanClass{
		id=1
	}
	object SCENE_II extends SceneRomanClass{
		id=2
	}
	object ROMEO extends Character{
		name = "Romeo"
	}
	object JULIET extends Character{
		name = "Juliet"
	}

	def main(args: Array[String]): Unit = {
		ACT_I COLON
		SCENE_I COLON
		LEFT_BRACKET ENTER ROMEO AND JULIET RIGHT_BRACKET
		ROMEO COLON 
			YOU ARE MY GOOD GOOD GOOD FRIEND STATEMENT_SYMBOL	// Juliet.val := 2 * 2 * 2 * 1 --> 8 
			YOU ARE AS GOOD AS THE_SUM_OF YOURSELF AND YOURSELF STATEMENT_SYMBOL // Juliet.val := Juliet.val + Juliet.val --> 16
			REMEMBER COMMA YOURSELF STATEMENT_SYMBOL // Juliet.push(Juliet.val) --> 16
			YOU ARE MY HORRIBLE HORRIBLE HORRIBLE ENEMY STATEMENT_SYMBOL // Juliet.val := 2 * 2 * 2 * -1 --> -8
			OPEN YOUR HEART STATEMENT_SYMBOL	// print_int(Juliet.val) --> -8
			RECALL STATEMENT_SYMBOL	// Juliet.val = Juliet.pop() --> 16
			OPEN YOUR HEART STATEMENT_SYMBOL	// print_int(Juliet.val) --> 16
		JULIET COLON
			YOU ARE MY HORRIBLE ENEMY STATEMENT_SYMBOL	// Romeo.val := 2 * -1 --> -2
			// (Romeo.val := Juliet.val + Romeo.val --> 14); print_int(Romeo.val) --> 14
			YOU ARE AS GOOD AS THE_SUM_OF MYSELF AND YOURSELF STATEMENT_SYMBOL OPEN YOUR HEART STATEMENT_SYMBOL
		RUN
	}
}