package psp

import org.junit.{ Assert => A }

package object tests {
  type Test = org.junit.Test

  def assertEquals[A](message: String, expected: A, actual: A): Unit              = assertTrue(message, expected == actual)
  def assertFalse(message: String, condition: => Boolean): Unit                   = A.assertFalse(message, condition)
  def assertTrue(message: String, condition: => Boolean): Unit                    = A.assertTrue(message, condition)
  def assertSame[A <: AnyRef](message: String, expected: A, actual: A): Unit      = A.assertSame(message, expected, actual)
  def assertNotSame[A <: AnyRef](message: String, unexpected: A, actual: A): Unit = A.assertNotSame(message, unexpected, actual)
  def fail(message: String): Unit                                                 = A.fail(message)
}
