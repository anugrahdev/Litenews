package com.anugrahdev.litenews.utils

import java.io.IOException

class ApiException(message:String) : IOException(message)
class NoConnectivityException(message: String):IOException(message)