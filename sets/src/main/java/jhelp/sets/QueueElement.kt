package jhelp.sets

internal class QueueElement<T>(val element: T, var next: QueueElement<T>?=null)