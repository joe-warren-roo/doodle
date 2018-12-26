package doodle
package syntax

import doodle.core.Normalized
import org.scalacheck._
import org.scalacheck.Prop._

class NormalizedSpec extends Properties("Normalized syntax") {
  property(".normalized") =
    forAll{ (d: Double) =>
      if(d >= 1.0) d.normalized ?= Normalized.MaxValue
      else if(d <= 0.0) d.normalized ?= Normalized.MinValue
      else d.normalized ?= Normalized.clip(d)
    }
}
