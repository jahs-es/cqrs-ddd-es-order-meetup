package com.jahs.shared.valueobject

import com.jahs.shared.noarg.NoArgsConstructor
import javax.persistence.Embeddable

@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@NoArgsConstructor
@Embeddable
annotation class ValueObject
