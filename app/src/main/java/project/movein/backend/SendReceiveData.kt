package project.movein.backend
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.Socket

class SendReceiveData {

    private val TAG = "BACKEND"
    private val serverAddress = "192.168.184.203"
    private val serverPort = 9999


    fun sendData(
        message: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        Thread {
            try {
                val socket = Socket(serverAddress, serverPort)
                val dataOutputStream = DataOutputStream(socket.getOutputStream())
                dataOutputStream.writeUTF(message)
                dataOutputStream.flush()
                //  Log.d(TAG, "Data sent to server: $message")

                val bufferSize = 1024 // taille du buffer pour stocker la réponse
                val buffer = ByteArray(bufferSize)
                val inputStream = socket.getInputStream()
                var bytesRead: Int

                // Lire la réponse byte par byte dans un tableau de bytes
                bytesRead = inputStream.read(buffer)
                val response = String(buffer, 0, bytesRead)

                // Traiter la réponse du serveur
                //    Log.d(TAG, "Received response from server: $response")
                onSuccess(response)

                socket.close()

            } catch (e: Exception) {
                Log.e(TAG, "Error: ${e.message}")
                onError(e.message ?: "Unknown error")
            }
        }.start()
    }

   /* fun testNodes(test: String) {
        val nodes = test.split("|")
        var i = 0
        val nodeWithoutLastChar = nodes.dropLast(2)
        for (node in nodeWithoutLastChar) {
            i++
            var coordinates = node.split(",")
            val xd = coordinates[1].toInt()
            val yd = coordinates[2].toInt()
            println("Coordonnées du nœud : ($xd, $yd)")
            coordinates = nodes[i].split(",")
            val xa = coordinates[1].toInt()
            val ya = coordinates[2].toInt()
            println("Coordonnées du nœud : ($xa, $ya)")
        }
    }*/
}