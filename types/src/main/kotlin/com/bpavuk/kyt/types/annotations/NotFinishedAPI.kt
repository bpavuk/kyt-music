package com.bpavuk.kyt.types.annotations

@RequiresOptIn(message = "This API is not finished and will fail with guarantee")
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.PROPERTY, AnnotationTarget.LOCAL_VARIABLE, AnnotationTarget.TYPEALIAS)
public annotation class NotFinishedAPI
