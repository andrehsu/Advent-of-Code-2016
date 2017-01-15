package day11

import scala.collection.mutable.ListBuffer

object Prog {
	
	def main(args: Array[String]): Unit = {
		run
	}
	
	sealed class Item(val element: String, val purpose: Char) {
		def isCounterpart(other: Item): Boolean = other.element.equals(element) && other.purpose != purpose
		
		override def toString: String = element.toUpperCase.substring(0, 2) + purpose
	}
	
	val inventory: ListBuffer[Map[String, Item]] = ListBuffer()
	
	def canCoexist(items: List[Item]): Boolean = {
		val pairing = items.groupBy(_.element)
		val singles = pairing.values.filter(_.size == 1).flatten
		singles.groupBy(_.purpose).size <= 1
	}
	
	def canBeMovedUp(items: (Item, Item), floor: Int): Boolean = items match {
		case (x, y) if x.isCounterpart(y) => true
		case (x, y) =>
			val nextFloor = inventory(floor + 1) + (x.toString -> x) + (y.toString -> y)
			val thisFloor = inventory(floor) - x.toString - y.toString
			canCoexist(nextFloor.values.toList) && canCoexist(thisFloor.values.toList)
	}
	
	def canBeMovedDown(item: Item, floor: Int): Boolean = {
		val nextFloor = inventory(floor - 1) + (item.toString -> item)
		val thisFloor = inventory(floor) - item.toString
		canCoexist(nextFloor.values.toList) && canCoexist(thisFloor.values.toList)
	}
	
	def moveUp(items: (Item, Item), floor: Int): Any = {
		val (x, y) = items
		inventory(floor) -= (x.toString, y.toString)
		inventory(floor + 1) += ((x.toString -> x), (y.toString -> y))
	}
	
	def moveDown(item: Item, floor: Int): Any = {
		inventory(floor) -= item.toString
		inventory(floor - 1) += (item.toString -> item)
	}
	
	def getCounterparts(floor: Int): Option[List[Item]] = {
		val pairs = inventory(floor).groupBy(_._2.element).values.filter(_.size == 2)
		if (pairs.nonEmpty) Option(List(pairs.head.head._2, pairs.head.last._2))
		else Option.empty
	}
	
	def allPairs(floor: Int): List[(Item, Item)] = {
		inventory(floor).values.toList.combinations(2).toList.map(x => (x.head, x.last))
	}
	
	def getNonCounterparts(floor: Int): Option[List[Item]] = {
		val solutions = allPairs(floor).filter(x => canBeMovedUp(x, floor))
		if (solutions.isEmpty) Option.empty else Option(List(solutions.head._1, solutions.head._2))
	}
	
	def getPair(floor: Int): List[Item] = {
		// Heuristic:
		// Always take two items when going up
		// If there are two items of same element (counterparts) use them
		// else pick some other valid combination
		getCounterparts(floor).getOrElse(getNonCounterparts(floor).get)
	}
	
	def getSingle(floor: Int): Item = {
		// Heuristic:
		// Always bring just one item down
		// Take the item that has a counterpart on floor below
		// If none exists just take some valid item
		val singles = inventory(floor).values.filter(x => canBeMovedDown(x, floor))
		val singlesWithCounterpart = singles.filter(
			s => inventory(floor - 1).values.exists(v => v.isCounterpart(s))
		)
		if (singlesWithCounterpart.nonEmpty) singlesWithCounterpart.head else singles.head
	}
	
	def itemCount: Int = inventory.foldLeft(0)((x, i) => x + i.values.size)
	
	def canGoDown(floor: Int): Boolean = {
		floor > 0 && inventory(floor - 1).values.nonEmpty
	}
	
	def canGoUp(floor: Int): Boolean = {
		floor < 3 && inventory(floor + 1).values.size < itemCount
	}
	
	def solve(): Any = {
		var e = 0
		var moves = 0
		var upwards = true
		while (inventory.last.values.size < itemCount) {
			while (upwards) {
				if (canGoUp(e)) {
					val pair = getPair(e)
					moveUp((pair.head, pair.last), e)
					println("MOVED " + pair + " UP TO FLOOR #" + (e + 2))
					moves = moves + 1
					e = e + 1
				} else {
					upwards = false
				}
			}
			while (!upwards) {
				if (canGoDown(e)) {
					val single = getSingle(e)
					moveDown(single, e)
					println("MOVED " + single + " DOWN TO FLOOR #" + e)
					moves = moves + 1
					e = e - 1
				} else {
					upwards = true
				}
			}
		}
		println("TOTAL MOVES: " + moves)
	}
	
	def run = {
		val items = List[Map[String, Item]](
			Map(
				"PRG" -> new Item("promethium", 'G'),
				"PRM" -> new Item("promethium", 'M')
			), Map(
				"COG" -> new Item("cobalt", 'G'),
				"CUG" -> new Item("curium", 'G'),
				"RUG" -> new Item("ruthenium", 'G'),
				"PLG" -> new Item("plutonium", 'G')
			), Map(
				"COM" -> new Item("cobalt", 'M'),
				"CUM" -> new Item("curium", 'M'),
				"RUM" -> new Item("ruthenium", 'M'),
				"PLM" -> new Item("plutonium", 'M')
			), Map()
		)
		
		// Part One
		items.foreach(i => inventory += i)
		solve()
		
		// Part Two
		inventory.remove(0, 4)
		items.foreach(i => inventory += i)
		inventory(0) += (
			("ELG" -> new Item("Elerium", 'G')), ("ELM" -> new Item("Elerium", 'M')),
			("DIM" -> new Item("Dilithium", 'M')), ("DIG" -> new Item("Dilithium", 'G')))
		//solve()
	}
}