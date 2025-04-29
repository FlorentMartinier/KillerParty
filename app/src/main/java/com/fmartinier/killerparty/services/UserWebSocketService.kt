import android.content.Context
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import android.util.Log
import com.fmartinier.killerparty.R
import com.fmartinier.killerparty.model.Party
import com.fmartinier.killerparty.services.PlayerService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ua.naiksoftware.stomp.dto.LifecycleEvent

class UserWebSocketService {

    private var stompClient: StompClient? = null
    private val compositeDisposable = CompositeDisposable()

    fun connect(
        sessionId: String,
        context: Context,
        playerService: PlayerService,
        party: Party,
        afterInsertFunctionToExecute: () -> Unit
    ) {
        val baseurl = context.getString(R.string.killer_back_api_host_base_url)
        val url = "wss://$baseurl/ws"
        stompClient = Stomp.over(
            Stomp.ConnectionProvider.OKHTTP,
            "wss://killer-party-back-2c78d956bd06.herokuapp.com/ws"
        )

        stompClient?.let { client ->
            compositeDisposable.add(
                client.lifecycle()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { lifecycleEvent ->
                        when (lifecycleEvent.type) {
                            LifecycleEvent.Type.OPENED -> {
                                Log.d("STOMP", "Connexion WebSocket ouverte")
                                // Maintenant que la connexion est ouverte, abonne-toi au topic
                                subscribeToTopic(client, sessionId, playerService, party, afterInsertFunctionToExecute)
                            }

                            LifecycleEvent.Type.ERROR ->
                                Log.e("STOMP", "Erreur WebSocket", lifecycleEvent.exception)

                            LifecycleEvent.Type.CLOSED ->
                                Log.d("STOMP", "Connexion WebSocket fermée")

                            LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT ->
                                Log.d("STOMP", "FAILED_SERVER_HEARTBEAT")
                        }
                    })

            // Connecte-toi au serveur
            client.connect()
        }
    }

    private fun subscribeToTopic(
        client: StompClient,
        sessionId: String,
        playerService: PlayerService,
        party: Party,
        afterInsertFunctionToExecute: () -> Unit
    ) {
        compositeDisposable.add(
            client.topic("/topic/session/$sessionId")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ stompMessage ->
                val jsonString = stompMessage.payload
                Log.d("STOMP", "Message reçu : $jsonString")

                val type = object : TypeToken<Map<String, String>>() {}.type
                val userMap = Gson().fromJson<Map<String, String>>(jsonString, type)
                val name = userMap["name"] ?: ""
                val phoneNumber = userMap["phoneNumber"] ?: ""
                playerService.insert(name, phoneNumber, party)
                afterInsertFunctionToExecute()
            }, { error ->
                Log.e("STOMP", "Erreur de souscription", error)
            })
        )
    }

    fun disconnect() {
        compositeDisposable.clear()
        stompClient?.disconnect()
    }
}
