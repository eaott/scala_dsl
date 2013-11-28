class Shakespeare {
	class Equality {
	}
	class AS_CLASS {
		def good(e:AS_CLASS):Equality = {
			new Equality
		}
	}
	object as extends AS_CLASS{}

	def main(args: Array[String]): Unit = {
	  as good as 
	}
	
}