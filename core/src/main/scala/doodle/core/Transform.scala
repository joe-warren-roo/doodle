package doodle
package core

/** Representation of an affine transformation as an augmented matrix. */
final case class Transform(elements: Array[Double]) {
  def apply(point: Point): Point = {
    val x = point.x
    val y = point.y

    val newX = (x * elements(0)) + (y * elements(1)) + elements(2)
    val newY = (x * elements(3)) + (y * elements(4)) + elements(5)

    Point(newX, newY)
  }

  def apply(vec: Vec): Vec =
    this.apply(vec.toPoint).toVec

  def andThen(that: Transform): Transform = {
    val a = this.elements
    val b = that.elements
    Transform(
      Array(b(0) * a(0) + b(1) * a(3),
            b(0) * a(1) + b(1) * a(4),
            b(0) * a(2) + b(1) * a(5) + b(2),
            b(3) * a(0) + b(4) * a(3),
            b(3) * a(1) + b(4) * a(4),
            b(3) * a(2) + b(4) * a(5) + b(5),
            0,
            0,
            1)
    )
  }

  def scale(x: Double, y: Double): Transform =
    this.andThen(Transform.scale(x, y))

  def rotate(angle: Angle): Transform =
    this.andThen(Transform.rotate(angle))

  def translate(x: Double, y: Double): Transform =
    this.andThen(Transform.translate(x, y))

  def translate(v: Vec): Transform =
    this.andThen(Transform.translate(v))
}
object Transform {
  val identity = scale(1.0, 1.0)

  def scale(x: Double, y: Double): Transform =
    Transform(Array(x, 0, 0, 0, y, 0, 0, 0, 1))

  def rotate(angle: Angle): Transform =
    Transform(Array(angle.cos, -angle.sin, 0, angle.sin, angle.cos, 0, 0, 0, 1))

  def translate(x: Double, y: Double): Transform =
    Transform(Array(1, 0, x, 0, 1, y, 0, 0, 1))

  def translate(v: Vec): Transform =
    translate(v.x, v.y)

  /** Reflect horizontally (around the Y-axis) */
  val horizontalReflection: Transform =
    Transform(Array(1, 0, 0, 0, -1, 0, 0, 0, 1))

  /** Reflect vertically (around the X-axis) */
  val verticalReflection: Transform =
    Transform(Array(-1, 0, 0, 0, 1, 0, 0, 0, 1))

  /** Convert from the usual cartesian coordinate system (origin in the center, x
    * and y increase towards the top right) to usual screen coordinate system
    * (origin in the top left, x and y increase to the bottom right). */
  def logicalToScreen(width: Double, height: Double): Transform =
    // A composition of a reflection and a translation
    Transform(Array(1, 0, width / 2.0, 0, -1, height / 2.0, 0, 0, 1))

  /** Convert from the usual screen coordinate system (origin in the top left, x
    * and y increase to the bottom right) to the usual cartesian coordinate
    * system (origin in the center, x and y increase towards the top right). */
  def screenToLogical(width: Double, height: Double): Transform =
    // A composition of a reflection and a translation
    Transform(Array(1, 0, -width / 2.0, 0, -1, height / 2.0, 0, 0, 1))
}
