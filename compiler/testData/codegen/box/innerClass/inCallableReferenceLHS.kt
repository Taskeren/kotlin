// LANGUAGE: +ProperSupportOfInnerClassesInCallableReferenceLHS

open class A<X> {
    inner class B<Y> {
        fun foo(): String = "O"
        @Suppress("UNCHECKED_CAST")
        fun bar(): X = "K" as X
    }

    val refFoo = B<Int>::foo
}

class C: A<String>() {
    val refBar = B<Int>::bar
}

fun box(): String {
    return A<Char>().run {
        refFoo(B())
    } + C().run {
        refBar(B())
    }
}
