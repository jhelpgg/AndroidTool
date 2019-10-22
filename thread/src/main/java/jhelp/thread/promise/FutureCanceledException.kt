package jhelp.thread.promise

class FutureCanceledException(reason: String) : Exception("Future canceled because : $reason")