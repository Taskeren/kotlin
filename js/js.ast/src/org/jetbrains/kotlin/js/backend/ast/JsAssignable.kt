/*
 * Copyright 2010-2026 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.js.backend.ast

sealed class JsAssignable : SourceInfoAwareJsNode() {
    abstract override fun deepCopy(): JsAssignable

    class Named(private var name: JsName) : JsAssignable(), HasName {
        override fun getName(): JsName = name

        override fun setName(name: JsName) {
            this.name = name
        }

        override fun accept(visitor: JsVisitor) {
            visitor.visitNamedAssignable(this)
        }

        override fun deepCopy(): Named {
            return Named(JsName(name.ident, name.isTemporary).apply {
                copyMetadataFrom(name)
            }).withMetadataFrom(this)
        }

        override fun traverse(
            visitor: JsVisitorWithContext,
            ctx: JsContext<*>,
        ) {
            visitor.visit(this, ctx)
            visitor.endVisit(this, ctx)
        }
    }

    class Pattern(pattern: JsExpression) : JsAssignable() {
        var pattern: JsExpression = pattern
            private set

        constructor(pattern: JsArrayLiteral) : this(pattern as JsExpression)
        constructor(pattern: JsObjectLiteral) : this(pattern as JsExpression)

        override fun accept(visitor: JsVisitor) {
            visitor.visitPatternAssignable(this)
        }

        override fun acceptChildren(visitor: JsVisitor) {
            visitor.accept(pattern)
        }

        override fun deepCopy(): Pattern {
            return Pattern(pattern.deepCopy()).withMetadataFrom(this)
        }

        override fun traverse(
            visitor: JsVisitorWithContext,
            ctx: JsContext<*>,
        ) {
            if (visitor.visit(this, ctx)) {
                visitor.accept(pattern)
            }
            visitor.endVisit(this, ctx)
        }
    }
}