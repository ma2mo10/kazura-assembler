package psp.tests

class PspTests {
  @Test
  def templateTest(): Unit = {
    assertEquals("5=5", 5, 5)
    assertNotSame("new AnyRefs", new AnyRef, new AnyRef)
    assertSame("null", null, null)
  }
}
