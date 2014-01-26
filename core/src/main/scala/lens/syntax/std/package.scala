package lens.syntax

import lens.{Traversal, Setter}

import scalaz.std.option.optionInstance
import scalaz.std.list.listInstance

package object std {

  def option[A]: Setter[Option[A], A] = Setter[Option, A]

  def list[A]: Traversal[List[A], A] = Traversal[List, A]

}
