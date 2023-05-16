package project.movein.backend


import android.content.ContentValues
import android.util.Log
import java.io.DataOutputStream
import java.net.Socket
import javax.inject.Inject

class BackendImp @Inject constructor():AppBackend {
    private val serverAddress = "192.168.139.203"
    private val serverPort = 80
    override fun sendData(message: String) {
        Thread {
            try {
                val socket = Socket(serverAddress, serverPort)
                val dataOutputStream = DataOutputStream(socket.getOutputStream())
                dataOutputStream.writeUTF(message)
                dataOutputStream.flush()
                socket.close()
                Log.d(ContentValues.TAG, "Data sent to server: $message")
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error: ${e.message}")
            }
        }.start()
    }
}

