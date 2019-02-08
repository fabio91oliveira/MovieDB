package oliveira.fabio.moviedbapp.util

import java.io.IOException

object NetworkUtil {

    @JvmStatic
    @Throws(InterruptedException::class, IOException::class)
    fun isConnected(): Boolean {
        val command = "ping -c 1 google.com"
        return Runtime.getRuntime().exec(command).waitFor() == 0
    }

}