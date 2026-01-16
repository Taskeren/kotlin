// Copyright (c) 2011, the Dart project authors.  Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package org.jetbrains.kotlin.js.backend.ast

/**
 * A JavaScript parameter.
 */
class JsParameter(
    private var assignable: JsAssignable,
    isRest: Boolean
) : SourceInfoAwareJsNode(), HasName {
    var isRest: Boolean = isRest
        private set

    constructor(name: JsName) : this(JsAssignable.Named(name), false)
    constructor(assignable: JsAssignable) : this(assignable, false)

    override fun getName() = (assignable as? HasName)?.name

    override fun setName(name: JsName?) {
        (assignable as? HasName)?.name = name
    }

    override fun accept(v: JsVisitor) {
        v.visitParameter(this)
    }

    override fun traverse(v: JsVisitorWithContext, ctx: JsContext<*>) {
        v.visit(this, ctx)
        v.endVisit(this, ctx)
    }

    override fun deepCopy(): JsParameter {
        return JsParameter(assignable, isRest).withMetadataFrom(this)
    }
}
