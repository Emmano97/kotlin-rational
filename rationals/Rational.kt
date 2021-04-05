package rationals

import java.math.BigInteger

class Rational(private val numerator: BigInteger, private val denominator: BigInteger): Comparable<Rational>{

    override fun toString(): String {
        return when {
            denominator == 1.toBigInteger() || numerator.rem(denominator) == 0.toBigInteger() -> numerator.div(denominator).toString()
            else -> {
                val current = simplify(this)

                if(current.denominator < 0.toBigInteger() || (current.numerator < 0.toBigInteger() && current.denominator < 0.toBigInteger())){
                    "${current.numerator.negate()}/${current.denominator.negate()}"
                } else {
                    "${current.numerator}/${current.denominator}"
                }
            }
        }
    }


    operator fun plus(other: Rational): Rational = Rational(
        (numerator * other.denominator) + (denominator * other.numerator),
        denominator * other.denominator
    )


    operator fun minus(other: Rational): Rational = Rational(
        (numerator * other.denominator) - (denominator * other.numerator),
        denominator * other.denominator
    )


    operator fun unaryMinus(): Rational = Rational(
        numerator.negate(), denominator
    )


    operator fun times(other: Rational): Rational = Rational(
        (numerator * other.numerator),
        (denominator * other.denominator)
    )

    operator fun div(other: Rational): Rational = Rational(
        numerator * other.denominator,
        denominator * other.numerator
    )


    override operator fun compareTo(other: Rational): Int {
        return (numerator * other.denominator).compareTo(other.numerator * denominator)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as Rational

        val current = simplify(this)
        val secondNumerator = simplify(other)

        return current.numerator.toDouble().div(current.denominator.toDouble()) == (
                secondNumerator.numerator.toDouble().div(secondNumerator.denominator.toDouble()))

    }

    operator fun rangeTo(last: Rational): ClosedRange<Rational> {
        return object : ClosedRange<Rational> {
            override val endInclusive: Rational = last
            override val start: Rational = this@Rational
        }
    }

    operator fun contains(other: Rational) = numerator <= other.numerator &&
            denominator <= other.denominator

    fun step(firstNumerator: BigInteger, secondNumerator: BigInteger): BigInteger =
        if (secondNumerator != 0.toBigInteger())
            step(secondNumerator, firstNumerator % secondNumerator)
        else
            firstNumerator

    fun simplify(other: Rational): Rational {
        val step = step(other.numerator, other.denominator).abs()
        return Rational(other.numerator.div(step), other.denominator.div(step))
    }

    override fun hashCode(): Int {
        var result = numerator.hashCode()
        result = 31 * result + denominator.hashCode()
        return result
    }
}

infix fun Long.divBy(denominator: Long): Rational = Rational(
    this.toBigInteger(),
    denominator.toBigInteger())

infix fun Int.divBy(denominator: Int): Rational = Rational(
    this.toBigInteger(),
    denominator.toBigInteger())

infix fun BigInteger.divBy(denominator: BigInteger): Rational = Rational(
    this,
    denominator)

fun String.toRational(): Rational {
    val count = this.split("/").size
    val numerator = substringBeforeLast("/", ).toBigInteger()
    val denominator = substringAfterLast("/").toBigInteger()

    return when (count) {
        1 -> Rational(numerator, 1.toBigInteger())
        else -> Rational(numerator, denominator)
    }
}

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}


