/*
 * Copyright (C) 2022 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.easy.dd2d.mapping

import scala.xml.Node

object Description extends BlockCitation {
  private val labelToPrefix = Map(
    "date" -> "Date",
    "valid" -> "Valid",
    "issued" -> "Issued",
    "modified" -> "Modified",
    "dateAccepted" -> "Date Accepted",
    "dateCopyrighted" -> "Date Copyrighted",
    "dateSubmitted" -> "Date Submitted",
    "coverage" -> "Coverage"
  )

  def toDescriptionValueObject(node: Node): JsonObject = {
    val m = FieldMap()
    m.addPrimitiveField(DESCRIPTION_VALUE, newlineToHtml(node.text))
    // TODO: add date subfield?
    m.toJsonObject
  }

  def toPrefixedDescription(node: Node): JsonObject = {
    val prefix = labelToPrefix.getOrElse(node.label, node.label)
    val m = FieldMap()
    m.addPrimitiveField(DESCRIPTION_VALUE, s"$prefix: ${ node.text }")
    m.toJsonObject
  }

  def newlineToHtml(description: String): String = {
    //OS agnostic newline and paragraph regex
    val newline = "\r\n|\n|\r"
    val paragraph = "(\r\n){2,}|\n{2,}|\r{2,}"
    description
      .trim
      .split(paragraph)
      .map(p => s"<p>$p</p>")
      .map(_.replaceAll(newline, "<br>"))
      .mkString
  }
}
