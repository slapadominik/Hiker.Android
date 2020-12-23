package com.hiker.presentation.chat

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar

import com.hiker.R
import com.hiker.domain.entities.Status
import com.hiker.domain.extensions.getJwtToken
import com.hiker.domain.extensions.getUserId
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import io.reactivex.Single
import kotlinx.android.synthetic.main.fragment_chat_view.*

class ChatView : Fragment() {

    companion object {
        fun newInstance() = ChatView()
    }

    private lateinit var viewModel: ChatViewViewModel
    private var messages: MutableList<ChatRoomMessage> = mutableListOf()
    private lateinit var adapter: ChatMessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, ChatRoomViewModelFactory()).get(ChatViewViewModel::class.java)




        val jwtToken = getJwtToken(requireActivity())
        chat_view_recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = ChatMessageAdapter(messages, requireContext())
        chat_view_recyclerView.adapter = adapter

        val hubConnection = HubConnectionBuilder.create("http://192.168.0.171:5000/chathub")
            .withAccessTokenProvider(Single.defer{ Single.just(jwtToken)})
            .build()
        hubConnection.on("MessageAdded", {msg ->
            activity?.runOnUiThread(java.lang.Runnable {
                messages.add(msg)
                adapter.notifyDataSetChanged()
            })
        }, ChatRoomMessage::class.java)
        hubConnection.start().blockingAwait()

        arguments?.let {
            val safeArgs = ChatViewArgs.fromBundle(it)
            hubConnection.send("JoinToChatRoom", safeArgs.tripId);
            setupClickListeners(safeArgs.tripId, hubConnection)
            viewModel.getChatRoomMessages(safeArgs.tripId).observe(this, Observer { response ->
                if (response.status == Status.SUCCESS){
                    messages.addAll(response.data!!.map { msg -> ChatRoomMessage(safeArgs.tripId, msg.content) })
                    adapter.notifyDataSetChanged()
                }
                else{
                    val snack = Snackbar.make(requireView(), response.message!!, Snackbar.LENGTH_LONG)
                    snack.show()
                }
             })
        }
    }

    private fun setupClickListeners(chatRoomId: Int, hubConnection: HubConnection){
        chat_view_send_button.setOnClickListener{
            if (hubConnection.connectionState == HubConnectionState.CONNECTED){
                hubConnection.send("SendMessage", ChatRoomMessage(chatRoomId, chat_view_message_input.text.toString()));
            }
        }
    }
}
