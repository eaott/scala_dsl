class A {
	class PI(v:Int) {
}

class PII(v:Int) {
}

implicit def d1(v:Int):PI = new PI(v)
implicit def d2(v:Int):PII = new PII(v)

}



object o extends A {
	def fn(a:PI) {
	println("fish")
}
def fnn(a:PII) {
	println("fish2")
}
	def main(args: Array[String]): Unit = {
	  fn 5
	  fnn 5
	}
	
}