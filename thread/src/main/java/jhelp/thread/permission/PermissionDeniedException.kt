package jhelp.thread.permission

class PermissionDeniedException(permission: String) : Exception("The permission $permission is denied")
