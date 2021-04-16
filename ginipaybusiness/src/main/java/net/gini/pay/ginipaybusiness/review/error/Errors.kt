package net.gini.pay.ginipaybusiness.review.error

class NoBankSelected : Throwable("No Bank Selected")
class NoProviderForPackageName(packageName: String) : Throwable("No Provide for package $packageName")