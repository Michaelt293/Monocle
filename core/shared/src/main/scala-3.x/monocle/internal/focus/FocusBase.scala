package monocle.internal.focus

import scala.quoted.Quotes

private[focus] trait FocusBase {
  val macroContext: Quotes 

  given Quotes = macroContext

  type Term = macroContext.reflect.Term
  type TypeRepr = macroContext.reflect.TypeRepr

  case class LambdaConfig(argName: String, lambdaBody: Term)

  enum FocusAction {
    case FieldSelect(name: String, fromType: TypeRepr, fromTypeArgs: List[TypeRepr], toType: TypeRepr)
    case KeywordSome(toType: TypeRepr)
    case KeywordAs(fromType: TypeRepr, toType: TypeRepr)
    case KeywordEach(fromType: TypeRepr, toType: TypeRepr, eachInstance: Term)
    case KeywordAt(fromType: TypeRepr, toType: TypeRepr, index: Term, atInstance: Term)
    case KeywordIndex(fromType: TypeRepr, toType: TypeRepr, index: Term, indexInstance: Term)

    override def toString(): String = this match {
      case FieldSelect(name, fromType, fromTypeArgs, toType) => s"FieldSelect($name, ${fromType.show}, ${fromTypeArgs.map(_.show)}, ${toType.show})"
      case KeywordSome(toType) => s"KeywordSome(${toType.show})"
      case KeywordAs(fromType, toType) => s"KeywordAs(${fromType.show}, ${toType.show})"
      case KeywordEach(fromType, toType, _) => s"KeywordEach(${fromType.show}, ${toType.show}, ...)"
      case KeywordAt(fromType, toType, _, _) => s"KeywordAt(${fromType.show}, ${toType.show}, ..., ...)"
      case KeywordIndex(fromType, toType, _, _) => s"KeywordIndex(${fromType.show}, ${toType.show}, ..., ...)"
    }
  }

  enum FocusError {
    case NotACaseClass(className: String, fieldName: String)
    case NotAConcreteClass(className: String)
    case DidNotDirectlyAccessArgument(argName: String)
    case NotASimpleLambdaFunction
    case CouldntUnderstandKeywordContext
    case UnexpectedCodeStructure(code: String)
    case CouldntFindFieldType(fromType: String, fieldName: String)
    case ComposeMismatch(type1: String, type2: String)
    case InvalidDowncast(fromType: String, toType: String)

    def asResult: FocusResult[Nothing] = Left(this)
  }

  type FocusResult[+A] = Either[FocusError, A]
}